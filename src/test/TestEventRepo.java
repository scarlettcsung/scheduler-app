package test;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import junit.framework.TestCase;
import repository.EventRepository;
import user.User;
import user.calendar.UserCalendar;

public class TestEventRepo extends TestCase {
	EventRepository repo;
	Event event;
	User admin;
	EventManager eventManager; 
	
	protected void setUp() {
		repo = new EventRepository();
		event = new CreatedEvent("Test", 60, "Desc", null);
		admin = new User("admin","admin",new UserCalendar(null));
		eventManager = new EventManager();
		eventManager.setOrganizer(event,admin);
	}

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
    	repo2.deleteItem(event.getEventId());
    	assertEquals(repo2.getAll().size(),0);
    }
    
    public void testDeleteEventsByOrganizer()
    {
    	EventRepository repo2 = new EventRepository();
    	repo2.deleteEventsByOrganizer(event.getOrganizer());
    	assertEquals(repo2.getAll().size(),0);
    	
    }

}
