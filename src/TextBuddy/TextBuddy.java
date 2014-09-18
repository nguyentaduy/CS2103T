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
import java.util.Scanner;

public class TextBuddy {

	private static final String MSG_TOO_MANY_FILE = "More than one file name";
	private static final String MSG_ASK_FOR_INDEX_TO_DELETE = 
			"Please give line number to Delete. To cancel command, press enter: ";
	private static final String MSG_ADD_EMPTY = 
			"Provide message to add. To cancel command, press enter: ";
	private static final String FILE_NAME_NOT_FOUND = "File name not found";
	private static final String MSG_COMMAND = "command: ";
	private static final String WELCOME_MSG = "Welcome to TextBuddy. %s is ready for use";
	private static final String MSG_ADD = "added to %s: \"%s\"";
	private static final String MSG_DELETE = "deleted from %s: \"%s\"";
	private static final String MSG_CLEAR = "all content deleted from %s";
	private static final String MSG_DISPLAY_EMPTY = "%s is empty";
	private static final String MSG_INVALID_COMMAND_FORMAT = "Invalid command format";
	private static final String MSG_INVALID_NUMBER_FORMAT = "Invalid number format";
	private static final String MSG_NUMBER_OUT_OF_RANGE = 
			"%d is out of range. Please enter a number from %d to %d";
	private static final String MSG_DELETE_EMPTY = "%s is already empty";
	private static final String LINE_DISPLAY = "%d. %s";
	private static final String ERROR_MSG = "Error: %s";

	// These are the possible command types
	enum COMMAND {
		ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID
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
			String fileName = args[0];
			showToUser(WELCOME_MSG, fileName);
			initialize(fileName);
		}
	}

	private static void initialize(String fileName) {
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
	private static void executeCommand(String s) {
		COMMAND command = determineCommandType(s);
		switch (command) {
		case ADD:
			addText(removeFirstWord(s));
			break;
		case DISPLAY:
			display();
			break;
		case DELETE:
			deleteText(removeFirstWord(s));
			break;
		case CLEAR:
			clear();
			break;
		case EXIT:
			exit();
		default:
			showToUser(ERROR_MSG, MSG_INVALID_COMMAND_FORMAT);
			break;
		}
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
		case "exit":
			return COMMAND.EXIT;
		default:
			return COMMAND.INVALID;
		}
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		return userCommand.trim().split(" ")[0];
	}

	/**
	 * This function is to execute ADD command. If user provides empty text or
	 * text containing only white spaces, ask them to provide (again) text
	 */

	private static void addText(String texts) {
		if (texts.isEmpty()) {
			texts = addNotEmptyText();
		}
		if (texts.isEmpty())
			return;
		list.add(texts);
		showToUser(MSG_ADD, file.getName(), texts);
	}

	private static String addNotEmptyText() {
		showToUser(MSG_ADD_EMPTY);
		return scan.nextLine();
	}

	/**
	 * This function is to execute DELETE command. If users don't specify line
	 * number to delete, ask them (again)
	 */

	private static void deleteText(String index) {
		if (list.size() == 0) {
			// if file is empty then tell user Empty file
			showToUser(MSG_DELETE_EMPTY, file.getName());
			return;
		}

		String lineNumberToDelete;

		if (index.isEmpty()) { // users don't provide index
			lineNumberToDelete = askForIndexToDelete();
		} else {
			lineNumberToDelete = index;
		}

		if (lineNumberToDelete.isEmpty()) {
			// if reach here, user want to cancel command
			return;
		}
		if (!isInteger(lineNumberToDelete)) {
			// if users don't provide and integer number
			showToUser(ERROR_MSG, MSG_INVALID_NUMBER_FORMAT);
			return;
		}

		int lineNum = Integer.parseInt(lineNumberToDelete);

		if (lineNum < 1 || lineNum > list.size()) {
			// if users don't provide a number in range
			showToUser(MSG_NUMBER_OUT_OF_RANGE, lineNum, 1, list.size());
		} else {
			showToUser(MSG_DELETE, file.getName(), list.get(lineNum - 1));
			list.remove(lineNum - 1);
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
	 * them it's empty
	 */
	private static void display() {
		if (list.isEmpty()) {
			showToUser(MSG_DISPLAY_EMPTY, file.getName());
			return;
		}
		int i = 1;
		for (String s : list) {
			showToUser(LINE_DISPLAY, i, s);
			i++;
		}
	}

	/**
	 * This function is to execute Clear command
	 */

	private static void clear() {
		list.clear();
		showToUser(MSG_CLEAR, file.getName());
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
	public static void showToUser(String message) {
		System.out.print(message);
	}

	public static void showToUser(String message, Object... args) {
		System.out.println(String.format(message, args));
	}

}


