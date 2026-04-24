package test;

import java.util.List;

import junit.framework.TestCase;
import repository.UserRepository;
import user.AdminUser;
import user.User;
import user.service.UserService;
import user.service.UserDeletionResult;

/**
 * Unit tests for {@link user.service.UserService}.
 *
 * @author SN GI NJ
 * @version 1
 */
public class TestUserService extends TestCase {

    private UserService userService;
    private UserRepository testRepo;

    // setUp() runs automatically BEFORE every single test method
    protected void setUp() {
        // 1. Create a fresh, totally empty repository for THIS specific test
        testRepo = new UserRepository();
        
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

    public void testAuthenticateUserReturnsMatchingUser() {
        userService.registerUser("testUser", "testPassword");

        User authenticatedUser = userService.authenticateUser("testUser", "testPassword");

        assertNotNull(authenticatedUser);
        assertEquals("testUser", authenticatedUser.getUsername());
    }

    public void testAuthenticateUserReturnsNullForWrongPassword() {
        userService.registerUser("testUser", "testPassword");

        User authenticatedUser = userService.authenticateUser("testUser", "wrongPassword");

        assertNull(authenticatedUser);
    }

    public void testListUsernamesReturnsSavedUsers() {
        userService.registerUser("testUser", "testPassword");
        userService.registerUser("anotherUser", "pw");

        List<String> usernames = userService.listUsernames();

        assertTrue(usernames.contains("testUser"));
        assertTrue(usernames.contains("anotherUser"));
    }

    public void testDeleteUserMapsAdminDeletionResult() {
        User targetUser = new User("testUser", "pw123", null);
        User adminUser = new AdminUser("admin", "admin", null);
        testRepo.saveUser(targetUser);

        UserDeletionResult result = userService.deleteUser("testUser", adminUser);

        assertEquals(UserDeletionResult.DELETED_BY_ADMIN, result);
    }

    public void testDeleteOwnAccountMapsSelfDeletionResult() {
        User targetUser = new User("testUser", "pw123", null);
        testRepo.saveUser(targetUser);

        UserDeletionResult result = userService.deleteOwnAccount(targetUser);

        assertEquals(UserDeletionResult.DELETED_SELF, result);
    }
    
    public void testIsSuccessAllCases() {
        assertTrue(UserDeletionResult.DELETED_BY_ADMIN.isSuccess());
        assertTrue(UserDeletionResult.DELETED_SELF.isSuccess());
        assertFalse(UserDeletionResult.NOT_AUTHENTICATED.isSuccess());
        assertFalse(UserDeletionResult.NOT_PERMITTED.isSuccess());
    }
    
    public void testLoginReturnsFalseForUnknownUser() {
        boolean result = userService.login("unknownUser", "password");

        assertFalse(result);
    }

}
