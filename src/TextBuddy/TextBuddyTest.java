package TextBuddy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {

	/**
	 * Initialize a new file empty named text.txt when start each test
	 */
	@Before
	public void initialize() {
		String a = "text.txt";
		TextBuddy.initialize(a);
		TextBuddy.executeCommand("clear");
	}

	/**
	 * These following two test cases are normal cases for sort
	 */

	@Test
	public void testSort_1() {

		TextBuddy.executeCommand("add one thing");
		TextBuddy.executeCommand("add two things");
		TextBuddy.executeCommand("add 1");
		TextBuddy.executeCommand("add something else");
		String test = "";
		test += "1\n";
		test += "one thing\n";
		test += "something else\n";
		test += "two things\n";
		assertEquals("sort fails", test, TextBuddy.executeCommand("sort"));
	}

	@Test
	public void testSort_2() {
		TextBuddy.executeCommand("add bcda");
		TextBuddy.executeCommand("add cdba");
		TextBuddy.executeCommand("add dcba");
		TextBuddy.executeCommand("add acbd");
		String testResult = "";
		testResult += "acbd\n";
		testResult += "bcda\n";
		testResult += "cdba\n";
		testResult += "dcba\n";
		assertEquals("sort fails", testResult, TextBuddy.executeCommand("sort"));
	}

	/**
	 * Sort on empty file
	 */
	@Test
	public void testSort_3() {
		assertEquals("sort fails", "text.txt is empty\n",
				TextBuddy.executeCommand("sort"));
	}

	@Test
	public void testSearch_1() {
		TextBuddy.executeCommand("add one thing");
		TextBuddy.executeCommand("add two things");
		TextBuddy.executeCommand("add 1");
		TextBuddy.executeCommand("add something else");

		String testResult = "two things\n";
		assertEquals("sort fails", testResult,
				TextBuddy.executeCommand("search two"));
	}

	// Not found
	@Test
	public void testSearch_2() {
		TextBuddy.executeCommand("add one thing");
		TextBuddy.executeCommand("add two things");
		TextBuddy.executeCommand("add 1");
		TextBuddy.executeCommand("add something else");

		String testResult = "No line contains key word\n";
		assertEquals("search fails", testResult,
				TextBuddy.executeCommand("search three"));
	}

	@Test
	public void testSearch_3() {
		TextBuddy.executeCommand("add little brown fox");
		TextBuddy.executeCommand("add jumped over the moon");
		TextBuddy.executeCommand("add and jumped over the moon one more time");
		TextBuddy.executeCommand("add but fell miserbly");

		String testResult = "but fell miserbly\n";
		assertEquals("search fails", testResult,
				TextBuddy.executeCommand("search fell miserbly"));
	}

	/**
	 * Search in an empty file
	 */
	@Test
	public void testSearch_4() {
		assertEquals("search fails", "text.txt is empty\n",
				TextBuddy.executeCommand("search fell miserbly"));

	}

	/**
	 * Search with empty keyword
	 */
	@Test
	public void testSearch_5() {
		TextBuddy.executeCommand("add little brown fox");
		TextBuddy.executeCommand("add jumped over the moon");
		TextBuddy.executeCommand("add and jumped over the moon one more time");
		TextBuddy.executeCommand("add but fell miserbly");
		assertEquals("search fails", "Empty keyword\n",
				TextBuddy.executeCommand("search"));

	}

}
