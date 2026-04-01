package test;

import UserService.UserService;
import UserRepository.UserRepository; // Added this import
import junit.framework.TestCase;

public class testUserService extends TestCase {

    private UserService userService;

    // setUp() runs automatically BEFORE every single test method
    protected void setUp() {
        // 1. Create a fresh, totally empty repository for THIS specific test
        UserRepository testRepo = new UserRepository();
        
        // 2. Inject it into the service
        userService = new UserService(testRepo);
    }

    public void testRegisterUser() {
        // Look how clean this is! 
        // We don't need to call deleteUser("testUser") first anymore 
        // because setUp() guarantees the list is completely empty!
        
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
        boolean result = userService.deleteUser("testUser");

        assertEquals(true, result);
    }

    public void testDeleteUserReturnsFalseWhenUserDoesNotExist() {
        // Again, no need to manually delete first!
        boolean result = userService.deleteUser("missingUser");

        assertEquals(false, result);
    }
}