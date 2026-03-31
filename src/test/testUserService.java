package test;

import User.User;
import UserService.UserService;
import junit.framework.TestCase;

public class testUserService extends TestCase {

    private UserService userService;

    protected void setUp() {
        userService = new UserService();
    }

    public void testRegisterUser() {
        String username = "testUser";

        boolean result = userService.registerUser(username, "testPassword");

        assertEquals(result, true);
    }

    public void testRegisterUserReturnsFalseWhenUserAlreadyExists() {
        String username = "testUser";
        boolean result = userService.registerUser(username, "anotherPassword");

        assertEquals(result, false);
    }

    public void testDeleteUser() {
        String username = "testUser";

        boolean result = userService.deleteUser(username);

        assertEquals(result, true);
    }

    public void testDeleteUserReturnsFalseWhenUserDoesNotExist() {
        String username = "missingUser";

        boolean result = userService.deleteUser(username);

        assertEquals(result, false);
    }
}
