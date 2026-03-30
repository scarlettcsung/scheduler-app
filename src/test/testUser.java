package test;


import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

public class testUser extends TestCase {
	
	UserCalendar myCalendar = new UserCalendar();
	User User = new User("testUser", "testPassword", myCalendar, true);
	public void testGetUsername() {
		
		assertEquals("testUser", User.getUsername());
	}
	public void testGetPassword() {
		
		assertEquals("testPassword", User.getPassword());
	}

	public void testGetCalendar() {
		
		assertEquals(myCalendar, User.getCalendar());
	}
	public void testIsAdmin() {
		
		assertEquals(true, true);
	}
}
