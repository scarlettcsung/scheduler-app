package test;

import junit.framework.TestCase;
import repository.EventRepository;
import user.User;
import event.CreatedEvent;
import event.Event;

import java.util.List;

/**
 * Unit tests for {@link repository.EventRepository}.
 *
 * @author CR EO
 * @version 3
 */
public class testEventRepository extends TestCase {

    private EventRepository eventRepo;
    private Event testEvent;

    public testEventRepository(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        eventRepo = new EventRepository();
        testEvent = new CreatedEvent("Project", 60, "test description", "admin", null);
    }

    public void testSaveAndFindEvent() {
        eventRepo.save(testEvent);
        String id = testEvent.getEventID();
        
        Event found = eventRepo.findByEventID(id);
        
        assertNotNull("Event should be found by ID", found);
        assertEquals("Project", found.getEventName());
    }

    public void testGetAll() {
        eventRepo.save(testEvent);
        eventRepo.save(new CreatedEvent("Lunch", 30, "Kaas", "user1", null));
        
        List allEvents = eventRepo.getAll();
        
        assertEquals("Repository should contain 2 events", 2, allEvents.size());
    }

    public void testDeleteEvent() {
        eventRepo.save(testEvent);
        String id = testEvent.getEventID();
        
        boolean deleted = eventRepo.deleteEvent(id);
        
        assertTrue("Delete should return true when event exists", deleted);
        assertNull("Event should no longer exist in repo", eventRepo.findByEventID(id));
    }
    
    public void testDeleteEventsByOrganizer() {
		eventRepo.save(new CreatedEvent("Project", 60, "test description", "John", null));
		eventRepo.save(new CreatedEvent("Meeting", 45, "Discuss project", "user1", null));
		
		assertEquals("Repository should contain 2 events", 2, eventRepo.getAll().size());
		
		eventRepo.deleteEventsByOrganizer("John");
		
		assertEquals("Repository should contain 1 event after deletion", 1, eventRepo.getAll().size());
	}

    public void testGetRepositoryType() {
        assertEquals("Event Repository", eventRepo.getRepositoryType());
    }
}
