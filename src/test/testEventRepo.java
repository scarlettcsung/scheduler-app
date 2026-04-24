package test;

import event.CreatedEvent;
import event.Event;
import junit.framework.TestCase;
import repository.EventRepository;

public class TestEventRepo extends TestCase {
	EventRepository repo = new EventRepository();
	Event event = new CreatedEvent("Test", 60, "Desc", "admin", null);

    public void testEventRepositoryType() {
        
        assertEquals("Event Repository", repo.getRepositoryType());
    }

    public void testSaveEvent() {
        
        repo.save(event);
        assertEquals(1, repo.getAll().size());
    }
    
    public void testGetItemByID()
    {
    	repo.save(event);
    	assertEquals(repo.getItemById(event.getEventId()), event);
    }
    
    public void testDeleteItem()
    {
    	EventRepository repo2 = new EventRepository();
    	Event event = new CreatedEvent("Test", 60, "Desc", "admin", null);
    	repo2.deleteItem(event.getEventId());
    	assertEquals(repo2.getAll().size(),0);
    }
    
    public void testDeleteEventsByOrganizer()
    {
    	EventRepository repo2 = new EventRepository();
    	Event event = new CreatedEvent("Test", 60, "Desc", "admin", null);
    	repo2.deleteEventsByOrganizer(event.getOrganizer());
    	assertEquals(repo2.getAll().size(),0);
    	
    }

}
