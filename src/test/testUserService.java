package test;

import UserService.UserService;
import junit.framework.TestCase;
import Authentication.Authentication;
import Repository.UserRepository;

/**
 * Unit tests for {@link UserService.UserService}.
 *
 * @author SN GI NJ
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


 
}
