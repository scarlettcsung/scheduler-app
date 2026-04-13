package test;

import Event.Event;
import Repository.EventRepository;
import junit.framework.TestCase;

public class testEventRepo extends TestCase {

    public void testEventRepositoryType() {
        EventRepository repo = new EventRepository();
        assertEquals("Event Repository", repo.getRepositoryType());
    }

    public void testSaveEvent() {
        EventRepository repo = new EventRepository();
        Event event = new Event("Test", 60, "Desc", "admin", false, null);

        repo.save(event);

        assertEquals(1, repo.getAll().size());
    }
}
