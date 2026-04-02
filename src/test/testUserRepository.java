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
    	// Delete Checks :EO GI

    public void testDeleteNullUsername() {
 
        int deleted = repository.deleteUserData("MissingUser", null);
        assertEquals(1, deleted);
    }
    
    public void testDeleteExistingUserAsAdmin() {
    	// We will change once we have a file to save the user list and a permanent admin
    	// Now we will just go on with a dummy admin user.
		User adminUser = new User("admin", "admin", null, true);
		repository.saveUser(testUser);
		int deleted = repository.deleteUserData("testUser", adminUser);
		assertEquals(2, deleted);
			}
	
	public void testDeleteExistingUserAsSelf() {
		repository.saveUser(testUser);
		int deleted = repository.deleteUserData("testUser", testUser);
		assertEquals(3, deleted);
			}
	
	public void testDeleteExistingUserAsOther() {
		User otherUser = new User("otherUser", "pw123", null, false);
		repository.saveUser(testUser);
		int deleted = repository.deleteUserData("testUser", otherUser);
		assertEquals(4, deleted);
		
	}

    protected void tearDown() throws Exception {
        repository = null;
        testUser = null;
        super.tearDown();
    }
}