package test;


import java.util.ArrayList;

import Repository.UserRepository;
import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;
import event.CreatedEvent;
import junit.framework.TestCase;
import event.Event;

/**
 * Unit tests for {@link Repository.UserRepository}.
 *
 * @author CR EO NJ
 * @version 3 and 4
 */
public class testUserRepository extends TestCase {
    
    private UserRepository repository;
    private User testUser;

    protected void setUp() throws Exception {
        super.setUp();
        repository = new UserRepository();
        testUser = new User("testUser", "pw123", null);
        UserCalendar testCalendar = new UserCalendar(null);
        testUser.setCalendar(testCalendar);
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
		User adminUser = new AdminUser("admin", "admin", null);
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
		User otherUser = new User("otherUser", "pw123", null);
		repository.saveUser(testUser);
		int deleted = repository.deleteUserData("testUser", otherUser);
		assertEquals(4, deleted);
		
	}

    protected void tearDown() throws Exception {
        repository = null;
        testUser = null;
        super.tearDown();
    }
    
    //test getrepository type
    public void testGetRepositoryType() {
        String type = repository.getRepositoryType();
        assertEquals("user Repository", type);
    }
    
    //test delete user that is not in repository
    public void testDeleteNonExistingUser() {
        User someUser = new User("someone", "pw", null);

        int result = repository.deleteUserData("ghostUser", someUser);

        assertEquals(4, result);
    }
    
    public void testRemoveFromAllCalendars() {
    	Event testEvent1 = new CreatedEvent("Test Event 1", 60, "Description 1", "testUser1", null);
    	Event testEvent2 = new CreatedEvent("Test Event 2", 60, "Description 2", "testUser2", null);
    	ArrayList<Event> eventList = new ArrayList<>();
    	eventList.add(testEvent1);
    	eventList.add(testEvent2);
    	UserCalendar calendar1 = new UserCalendar(eventList);
    	UserCalendar calendar2 = new UserCalendar(eventList);
    	User testUser1 = new User("testUser2","123",calendar1);
    	User testUser2 = new User("testUser2","123",calendar2);
    	repository.saveUser(testUser1);
    	repository.saveUser(testUser2);
    	
    	repository.removeEventFromAllCalendars(testEvent1);
    	
    	assertFalse(testUser1.getCalendar().getEvents().contains(testEvent1));
    	assertFalse(testUser2.getCalendar().getEvents().contains(testEvent1));
    }
    
    public void testCleanupUserEventReferences() {
    	Event testEvent = new CreatedEvent("Test Event", 60, "Description", "testUser", null);
		ArrayList<Event> eventList = new ArrayList<>();
		eventList.add(testEvent);
		UserCalendar calendar = new UserCalendar(eventList);
		User testUser = new User("testUser","123",calendar);
		repository.saveUser(testUser);
		
		
		repository.cleanupUserEventReferences("testUser");
		
		assertFalse(testUser.getCalendar().getEvents().contains(testEvent));
    }
    
    //admin cant delete itself test
    public void testDeleteAdminUser() {
        User adminUser = new AdminUser("admin", "admin", null);
        repository.saveUser(adminUser);
        
        int result = repository.deleteUserData("admin", adminUser);
        
        assertEquals(4, result);
    }
    
    
}
