package test;

import junit.framework.TestCase;
import user.AdminUser;
import user.User;
import user.calendar.UserCalendar;

/**
 * Unit tests for {@link user.User} and {@link user.AdminUser}.
 *
 * @author SN GI NJ
 * @version 1
 */
public class testUser extends TestCase {
    private User normalUser;
    private User adminUser;
    private UserCalendar myCalendar;

    @Override
    protected void setUp() {
        normalUser = new User("testUser", "testPassword", null);
        myCalendar = new UserCalendar(null);
        normalUser.setCalendar(myCalendar);
        adminUser = new AdminUser("admin", "admin", null);
    }

    //test the get username
    public void testGetUsername() {
        assertEquals("testUser", normalUser.getUsername());
    }

    //test getpassword
    public void testGetPassword() {
        assertEquals("testPassword", normalUser.getPassword());
    }

    //test get calendar
    public void testGetCalendar() {
        assertEquals(myCalendar, normalUser.getCalendar());
    }

    //test acces to admin being denied as user
    public void testNormalUserCannotAccessAdminPanel() {
        assertFalse(normalUser.canAccessAdminPanel());
    }

    //test admin user can access admin panel
    public void testAdminUserCanAccessAdminPanel() {
        assertTrue(adminUser.canAccessAdminPanel());
    }
    
    //test delete user while user is null
    public void testCanDeleteUserNull() {
    	assertFalse(normalUser.canDeleteUser(null));
    }
    
    //test the admin can delete user
    public void testAdminCanDeleteExistingUser() {
        User targetUser = new User("targetUser", "password", null);
        assertTrue(adminUser.canDeleteUser(targetUser));
    }
    
    //test the admin cannot delete user
    public void testAdminCannotDeleteNullUser() {
        assertFalse(adminUser.canDeleteUser(null));
    }
}
