package test;

import UserService.UserService;
import junit.framework.TestCase;

public class testUserService extends TestCase {

    private UserService userService;

    protected void setUp() {
        userService = new UserService();
    }

    public void testRegisterUser() {
		userService.deleteUser("testUser");
        boolean result = userService.registerUser("testUser", "testPassword");

        assertEquals(result, true);
    }

    public void testRegisterUserReturnsFalseWhenUserAlreadyExists() {
    	userService.registerUser("testUser", "testPassword");
        boolean result = userService.registerUser("testUser", "anotherPassword");

        assertEquals(result, false);
    }

    public void testDeleteUser() {
    	userService.registerUser("testUser", "testPassword");
        boolean result = userService.deleteUser("testUser");

        assertEquals(result, true);
    }

    public void testDeleteUserReturnsFalseWhenUserDoesNotExist() {
    	userService.deleteUser("testUser");
        boolean result = userService.deleteUser("missingUser");

        assertEquals(result, false);
    }
}
