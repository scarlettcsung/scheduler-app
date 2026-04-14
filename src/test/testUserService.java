package test;

import UserService.UserService;
import junit.framework.TestCase;
import Authentication.Authentication;
import Repository.UserRepository;

/**
 * Unit tests for {@link UserService.UserService}.
 *
 * @author SN GI
 * @version 1
 */
public class testUserService extends TestCase {

    private UserService userService;
    private Authentication authentication;

    // setUp() runs automatically BEFORE every single test method
    protected void setUp() {
        // 1. Create a fresh, totally empty repository for THIS specific test
        UserRepository testRepo = new UserRepository();
        
        // 2. Inject it into the service
        userService = new UserService(testRepo);
    }

    public void testRegisterUser() {
        
        boolean result = userService.registerUser("testUser", "testPassword");
        assertEquals(true, result); 
    }

    public void testRegisterUserReturnsFalseWhenUserAlreadyExists() {
        userService.registerUser("testUser", "testPassword");
        boolean result = userService.registerUser("testUser", "anotherPassword");

        assertEquals(false, result);
    }

    public void testDeleteUser() {
        userService.registerUser("testUser", "testPassword");
        userService.login("testUser", "testPassword"); // Log in as the user to be deleted
        boolean result = userService.deleteUser("testUser");

        assertEquals(true, result);
    }

    public void testDeleteUserReturnsFalseWhenUserDoesNotExist() {
        // Again, no need to manually delete first!
        boolean result = userService.deleteUser("missingUser");

        assertEquals(false, result);
    }
    
 // Covers deleteUser when user exists but is not authorized (not logged in)
    public void testDeleteUserReturnsFalseWhenNotAuthorized() {
        userService.registerUser("testUser", "testPassword");
        // deliberately not logging in, so authenticatedUser is null
        boolean result = userService.deleteUser("testUser");
        assertEquals(false, result);
    }
}
