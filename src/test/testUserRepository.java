package test;

import UserCalendar.UserCalendar;

import User.User;
import UserRepository.UserRepository;
import junit.framework.TestCase;

public class testUserRepository extends TestCase {
    
    private UserRepository repository;
    private User testUser;
    private UserCalendar userCalendar;

    protected void setUp() throws Exception {
        super.setUp();
        testUser = new User("testUser", "pw123", userCalendar, false);
        repository = new UserRepository();
        UserCalendar testCalendar = new UserCalendar(testUser, null);
    }

    public void testSaveAndFindUser() {
        repository.saveUser(testUser);
        
        User found = repository.findUsername("testUser");
        
        assertNotNull(found);
        assertEquals("testUser", found.getUsername());
    }

    public void testFindNonExistentUser() {
        User found = repository.findUsername("nonExistent");
        assertNull(found);
    }

    public void testDeleteUserSuccess() {
        repository.saveUser(testUser);
        
        boolean deleted = repository.deleteUserData("testUser");
        
        assertTrue(deleted);
        assertNull(repository.findUsername("testUser"));
    }

    public void testDeleteUserNotFound() {
        boolean deleted = repository.deleteUserData("MissingUser");
        assertFalse(deleted);
    }

    public void testDeleteNullUsername() {
        boolean deleted = repository.deleteUserData(null);
        assertFalse(deleted);
    }

    protected void tearDown() throws Exception {
        repository = null;
        testUser = null;
        super.tearDown();
    }
}