package test;

import junit.framework.TestCase;
import repository.EventRepository;
import user.User;
import user.calendar.UserCalendar;
import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;

import java.util.List;

/**
 * Unit tests for {@link repository.EventRepository}.
 *
 * @author CR EO
 * @version 3
 */
public class TestEventRepository extends TestCase {

    private EventRepository eventRepo;
    private EventManager eventManager;
    private Event testEvent;
    private User admin;

    public TestEventRepository(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        eventRepo = new EventRepository();
        testEvent = new CreatedEvent("Project", 60, "test description", null);
        admin = new User("admin","admin", new UserCalendar(null));
        eventManager = new EventManager();
        eventManager.setOrganizer(testEvent, admin);
    }

    public void testSaveAndFindEvent() {
        eventRepo.save(testEvent);
        String id = testEvent.getEventId();
        
        Event found = eventRepo.getItemById(id);
        
        assertNotNull("Event should be found by ID", found);
        assertEquals("Project", found.getEventName());
    }

    public void testGetAll() {
        eventRepo.save(testEvent);
        eventRepo.save(new CreatedEvent("Lunch", 30, "Kaas", null));
        
        List<Event> allEvents = eventRepo.getAll();
        
        assertEquals("Repository should contain 2 events", 2, allEvents.size());
    }

    public void testDeleteEvent() {
        eventRepo.save(testEvent);
        String id = testEvent.getEventId();
        
        int deleted = eventRepo.deleteItem(id);
        
        assertEquals(1, deleted);
        assertNull("Event should no longer exist in repo", eventRepo.getItemById(id));
    }
    
    public void testDeleteEventsByOrganizer() {
    	Event project = new CreatedEvent("Project", 60, "test description", null);
    	User john = new User("John","12345",null);
    	eventManager.setOrganizer(project, john);
    	Event meeting = new CreatedEvent("Meeting", 45, "Discuss project",  null);
    	User user1 = new User("user1","12345",null);
    	eventManager.setOrganizer(meeting, user1);
		eventRepo.save(project);
		eventRepo.save(meeting);
		
		assertEquals("Repository should contain 2 events", 2, eventRepo.getAll().size());
		
		eventRepo.deleteEventsByOrganizer("John");
		
		assertEquals("Repository should contain 1 event after deletion", 1, eventRepo.getAll().size());
	}

    public void testGetRepositoryType() {
        assertEquals("Event Repository", eventRepo.getRepositoryType());
    }
}
