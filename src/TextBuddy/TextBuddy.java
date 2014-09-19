package TextBuddy;

/**
 /**
 * This class is used to manipulate the file. Main functions include: 
 * add texts, delete texts, display texts, clear all texts and write 
 * changes back to file before exiting. This class accepts some not full commands 
 * (add and delete) in case users don't provide arguments
 * The command format is given by the example interaction below:
 * 		java  TextBuddy mytextfile.txt)
 Welcome to TextBuddy. mytextfile.txt is ready for use
 command: add little brown fox
 added to mytextfile.txt: "little brown fox"
 command: display
 1. little brown fox
 command: add jumped over the moon
 added to mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 2. jumped over the moon
 command: delete 2
 deleted from mytextfile.txt: "jumped over the moon"
 command: display
 1. little brown fox
 command: clear
 all content deleted from mytextfile.txt
 command: display
 mytextfile.txt is empty
 command: exit
 * @author Nguyen Ta Duy
 * Team id; T16-2j
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddy {

	private static final String MSG_TOO_MANY_FILE = "More than one file name\n";
	private static final String MSG_ASK_FOR_INDEX_TO_DELETE = "Please give line number to Delete. To cancel command, press enter: ";
	private static final String MSG_ADD_EMPTY = "Provide message to add. To cancel command, press enter: ";
	private static final String FILE_NAME_NOT_FOUND = "File name not found\n";
	private static final String MSG_COMMAND = "command: ";
	private static final String WELCOME_MSG = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MSG_ADD = "added to %s: \"%s\"\n";
	private static final String MSG_DELETE = "deleted from %s: \"%s\"\n";
	private static final String MSG_CLEAR = "all content deleted from %s\n";
	private static final String MSG_EMPTY_FILE = "%s is empty\n";
	private static final String MSG_INVALID_COMMAND_FORMAT = "Invalid command format\n";
	private static final String MSG_INVALID_NUMBER_FORMAT = "Invalid number format\n";
	private static final String MSG_NUMBER_OUT_OF_RANGE = "%d is out of range. Please enter a number from %d to %d\n";
	private static final String MSG_DELETE_EMPTY = "%s is already empty\n";
	private static final String LINE_DISPLAY = "%d %s\n";
	private static final String ERROR_MSG = "Error: %s\n";

	private static final int START_INDEX = 0;
	private static final int POSITIVE_START_INDEX = 1;
	private static final String ENDLINE_CHAR = "\n";

	// These are the possible command types
	enum COMMAND {
		ADD, DELETE, DISPLAY, CLEAR, EXIT, SORT, INVALID
	}

	// This variable is declared for the file that user is manipulating
	private static File file;

	// This variable is declared to store the information while user
	// want to store in the file
	private static ArrayList<String> list;

	// This variable is for writing back to the file
	private static BufferedWriter writer;

	/*
	 * This variable is declared for the whole class (instead of declaring it
	 * inside the readUserCommand() method to facilitate automated testing using
	 * the I/O redirection technique. If not, only the first line of the input
	 * text file will be processed.
	 */
	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		welcomeValidArgument(args); // welcome users if they provide a valid
									// file name

		while (true) {
			showToUser(MSG_COMMAND);
			String command = readUserCommand();
			executeCommand(command);
		}
	}

	private static String readUserCommand() {
		String command = scan.nextLine();
		return command;
	}

	/** Welcome when user provide valid file name */

	public static void welcomeValidArgument(String[] args) {
		if (args.length <= 0) {
			showToUser(ERROR_MSG, FILE_NAME_NOT_FOUND);
			System.exit(0);
		} else if (args.length > 1) {
			showToUser(ERROR_MSG, MSG_TOO_MANY_FILE);
			System.exit(0);
		} else {
			String fileName = args[START_INDEX];
			showToUser(WELCOME_MSG, fileName);
			initialize(fileName);
		}
	}

	protected static void initialize(String fileName) {
		list = new ArrayList<String>();
		file = new File(fileName);
		loadFromFile(file);
	}

	private static void loadFromFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					file.getName()));
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		} catch (IOException e) {
		}
	}

	/** This function is used for executing the command user type in */
	protected static String executeCommand(String s) {
		COMMAND command = determineCommandType(s);
		switch (command) {
		case ADD:
			return addText(removeFirstWord(s));
		case DISPLAY:
			return display();
		case DELETE:
			return deleteText(removeFirstWord(s));
		case CLEAR:
			return clear();
		case SORT:
			return sort();
		case EXIT:
			exit();
		default:
		}
		return showToUser(ERROR_MSG, MSG_INVALID_COMMAND_FORMAT);

	}

	/** This function is to determine the command type user type in */
	private static COMMAND determineCommandType(String s) {
		String[] a = s.trim().split(" ");
		switch (a[0].toLowerCase()) {
		case "add":
			return COMMAND.ADD;
		case "delete":
			return COMMAND.DELETE;
		case "display":
			return COMMAND.DISPLAY;
		case "clear":
			return COMMAND.CLEAR;
		case "sort":
			return COMMAND.SORT;
		case "exit":
			return COMMAND.EXIT;
		default:
			return COMMAND.INVALID;
		}
	}

	protected static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	protected static String getFirstWord(String userCommand) {
		return userCommand.trim().split(" ")[START_INDEX];
	}

	/**
	 * This function is to execute ADD command. If user provides empty text or
	 * text containing only white spaces, ask them to provide (again) text
	 * 
	 * This returns a String which is displayed to user (for unit test)
	 * 
	 */

	private static String addText(String texts) {
		if (texts.isEmpty()) {
			texts = addNotEmptyText();
		}
		if (texts.isEmpty())
			return null;
		list.add(texts);
		return showToUser(MSG_ADD, file.getName(), texts);
	}

	private static String addNotEmptyText() {
		showToUser(MSG_ADD_EMPTY);
		return scan.nextLine();
	}

	/**
	 * This function is to execute DELETE command. If users don't specify line
	 * number to delete, ask them (again)
	 * 
	 * This returns a String which is displayed to user (for unit test)
	 */

	private static String deleteText(String index) {
		if (list.isEmpty()) {
			// if file is empty then tell user Empty file
			return showToUser(MSG_DELETE_EMPTY, file.getName());

		}

		String lineNumberToDelete;

		if (index.isEmpty()) { // users don't provide index
			lineNumberToDelete = askForIndexToDelete();
		} else {
			lineNumberToDelete = index;
		}

		if (lineNumberToDelete.isEmpty()) {
			// if reach here, user want to cancel command
			return null;
		}
		if (!isInteger(lineNumberToDelete)) {
			// if users don't provide and integer number
			return showToUser(ERROR_MSG, MSG_INVALID_NUMBER_FORMAT);

		}

		int lineNum = Integer.parseInt(lineNumberToDelete);

		if (lineNum < POSITIVE_START_INDEX || lineNum > list.size()) {
			// if users don't provide a number in range
			return showToUser(MSG_NUMBER_OUT_OF_RANGE, lineNum,
					POSITIVE_START_INDEX, list.size());
		} else {

			String deletedLine = list.get(lineNum - 1);
			list.remove(lineNum - 1);
			return showToUser(MSG_DELETE, file.getName(), deletedLine);
		}
	}

	// This function is called when users don't provide a
	private static String askForIndexToDelete() {
		showToUser(MSG_ASK_FOR_INDEX_TO_DELETE);
		return scan.nextLine();
	}

	// This function check whether a string provided is an integer
	private static boolean isInteger(String index) {
		try {
			Integer.parseInt(index);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * This function is to execute display command. If the file is empty, tell
	 * them it's empty This returns a String which is displayed to user (for
	 * unit test)
	 */
	private static String display() {
		if (list.isEmpty()) {
			return showToUser(MSG_EMPTY_FILE, file.getName());
		}
		return displayLines();
	}

	private static String displayLines() {
		int i = POSITIVE_START_INDEX;
		String toDisplay = "";
		for (String s : list) {
			toDisplay += showToUser(LINE_DISPLAY, i, s);
			i++;
		}
		return toDisplay;
	}

	/**
	 * This function is to execute Clear command This returns a String which is
	 * displayed to user (for unit test)
	 */

	private static String clear() {
		list.clear();
		return showToUser(MSG_CLEAR, file.getName());
	}

	
	/**
	 * sortList. This function does sorting on the ArrayList list
	 */

	private static void sortList() {
		Collections.sort(list);
	}

	/**
	 * This function does sorting and return a String containing sorted texts
	 * (commands)
	 * 
	 * This returns a String which is displayed to user (for unit test)
	 */
	protected static String sort() {
		if (list.isEmpty()) {
			return showToUser(MSG_EMPTY_FILE, file.getName());
		}
		sortList();
		return listToString(list);
	}

	/**
	 * This method takes in an ArrayList<String> and return a string
	 */
	private static String listToString(ArrayList<String> list) {
		String lines = "";
		if (!list.isEmpty()) {
			for (String s : list) {
				lines += s;
				lines += ENDLINE_CHAR;
			}
		}
		return lines;
	}
	
	/**
	 * This function is to execute exit command Before exiting, we write back to
	 * the file
	 */

	private static void exit() {
		writeBackToFile();
		System.exit(0);
	}

	/** This function is to write to the file before exiting */
	private static void writeBackToFile() {
		try {
			writer = new BufferedWriter(new FileWriter(file.getName()));
			for (String s : list) {
				writer.write(s + "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** This two functions are to print out messages to user */
	public static String showToUser(String message) {
		System.out.print(message);
		return message;
	}

	public static String showToUser(String message, Object... args) {
		System.out.print(String.format(message, args));
		return String.format(message, args);
	}

}
