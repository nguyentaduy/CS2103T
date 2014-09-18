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
	public void testAdd() {
		assertEquals("add fails", "added to text.txt: \"something\"\n", 
				TextBuddy.executeCommand("add something"));
	}

	@Test
	public void testDisplay() {
		
		TextBuddy.executeCommand("add one thing");
		TextBuddy.executeCommand("add two things");
		assertEquals("display fails", "1 one thing\n2 two things\n", 
				TextBuddy.executeCommand("display"));
	}
	
	
	
}
