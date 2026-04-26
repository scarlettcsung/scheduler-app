package test;


import java.util.ArrayList;

import event.CreatedEvent;
import junit.framework.TestCase;
import repository.EventRepository;
import repository.UserRepository;
import user.AdminUser;
import user.User;
import event.Event;
import event.manager.EventManager;
import invite.Invite;
import invite.Role;
import user.service.UserDeletionResult;

/**
 * Unit tests for {@link repository.UserRepository}.
 *
 * @author CR EO NJ
 * @version 3 and 4
 */
public class TestUserRepository extends TestCase {
    
    private UserRepository repository;
    private User testUser;
    private EventManager eventManager;

    protected void setUp() throws Exception {
        super.setUp();
        eventManager = new EventManager();
        repository = new UserRepository();
        testUser = new User("testUser", "pw123");
    }

    public void testSaveAndFindUser() {
        repository.saveUser(testUser);
        
        User found = repository.getItemById("testUser");
        
        assertNotNull(found);
        assertEquals("testUser", found.getUsername());
    }

    public void testFindNonExistentUser() {
        User found = repository.getItemById("nonExistent");
        assertNull(found);
    }
    	// Delete Checks :EO GI

    public void testDeleteNullUsername() {
 
        UserDeletionResult deleted = repository.deleteUserData("MissingUser", null);
        assertEquals(UserDeletionResult.NOT_AUTHENTICATED, deleted);
    }
    
    public void testDeleteExistingUserAsAdmin() {
    	// We will change once we have a file to save the user list and a permanent admin
    	// Now we will just go on with a dummy admin user.
		User adminUser = new AdminUser("admin", "admin");
		repository.saveUser(testUser);
		UserDeletionResult deleted = repository.deleteUserData("testUser", adminUser);
		assertEquals(UserDeletionResult.DELETED_BY_ADMIN, deleted);
			}
	
	public void testDeleteExistingUserAsSelf() {
		repository.saveUser(testUser);
		UserDeletionResult deleted = repository.deleteUserData("testUser", testUser);
		assertEquals(UserDeletionResult.DELETED_SELF, deleted);
			}
	
	public void testDeleteExistingUserAsOther() {
		User otherUser = new User("otherUser", "pw123");
		repository.saveUser(testUser);
		UserDeletionResult deleted = repository.deleteUserData("testUser", otherUser);
		assertEquals(UserDeletionResult.NOT_PERMITTED, deleted);
		
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
        User someUser = new User("someone", "pw");

        UserDeletionResult result = repository.deleteUserData("ghostUser", someUser);

        assertEquals(UserDeletionResult.NOT_PERMITTED, result);
    }
    
    
    //admin cant delete itself test
    public void testDeleteAdminUser() {
        User adminUser = new AdminUser("admin", "admin");
        repository.saveUser(adminUser);
        
        UserDeletionResult result = repository.deleteUserData("admin", adminUser);
        
        assertEquals(UserDeletionResult.NOT_PERMITTED, result);
    }
    
    public void testCleanupUserEventReferences() {
        EventRepository eventRepo = new EventRepository();
        repository.setEventRepository(eventRepo); 

        String organizer = "testUser";
        
        CreatedEvent organizedEvent = new CreatedEvent("Meeting", 60, "Work", new ArrayList<>());
        organizedEvent.setOrganizer(organizer);
        eventRepo.save(organizedEvent);

        CreatedEvent invitedEvent = new CreatedEvent("Party", 120, "Fun", new ArrayList<>());
        invitedEvent.setOrganizer("newOrganizer");
        

        Invite invite = new Invite(organizer,organizedEvent.getEventId(),Role.ORGANIZER); 
        invitedEvent.getInvites().add(invite);
        eventRepo.save(invitedEvent);

        repository.cleanupUserEventReferences(organizer);
        assertNull(eventRepo.getItemById(organizedEvent.getEventId()));
        Event remainingEvent = eventRepo.getItemById(invitedEvent.getEventId());
        assertNotNull(remainingEvent);
        for (Invite i : remainingEvent.getInvites()) {
            assertFalse(organizer.equals(i.getRecipient()));
        }
    }
}
