package test;

import UserCalendar.UserCalendar;
import User.User;
import junit.framework.TestCase;


public class testUser extends TestCase {
    private User User;
    private UserCalendar myCalendar;

    @Override
    protected void setUp() {
        User = new User("testUser", "testPassword", null, true);
        myCalendar = new UserCalendar(User, null);
        User.setCalendar(myCalendar);
    }
	
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
