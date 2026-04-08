package test;

import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

public class testUser extends TestCase {
    private User normalUser;
    private User adminUser;
    private UserCalendar myCalendar;

    @Override
    protected void setUp() {
        normalUser = new User("testUser", "testPassword", null);
        myCalendar = new UserCalendar(normalUser, null);
        normalUser.setCalendar(myCalendar);
        adminUser = new AdminUser("admin", "admin", null);
    }

    public void testGetUsername() {
        assertEquals("testUser", normalUser.getUsername());
    }

    public void testGetPassword() {
        assertEquals("testPassword", normalUser.getPassword());
    }

    public void testGetCalendar() {
        assertEquals(myCalendar, normalUser.getCalendar());
    }

    public void testNormalUserCannotAccessAdminPanel() {
        assertFalse(normalUser.canAccessAdminPanel());
    }

    public void testAdminUserCanAccessAdminPanel() {
        assertTrue(adminUser.canAccessAdminPanel());
    }
}
