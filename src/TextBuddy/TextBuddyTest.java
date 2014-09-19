package TextBuddy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {
	
	
	@Before
	public void initialize() {
		String a = "text.txt";
		TextBuddy.initialize(a);
		TextBuddy.executeCommand("clear");
	}
	
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

	
	
	
}
