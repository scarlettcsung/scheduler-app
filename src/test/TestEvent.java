package test;

import event.*;
import event.manager.EventManager;
import invite.Invite;
import junit.framework.TestCase;
import repository.EventRepository;
import repository.UserRepository;
import user.User;
import user.calendar.UserCalendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link event.Event}, {@link event.CreatedEvent}, and {@link event.ImportedEvent}.
 *
 * @author NJ
 * @version TODO
 */
public class TestEvent extends TestCase {

    private String exampleOrganizer;
    private Event event;
    private LocalDateTime exampleTime;
    private Invite invite;
    private User exampleInvitee;
    private UserRepository repository;
    private EventRepository eventRepository;
    private EventManager eventManager;
    
    protected void setUp() {
    	Locale.setDefault(Locale.ENGLISH);       
    	exampleOrganizer = "Charles";
        exampleInvitee = new User("Joe", "67890", new UserCalendar(null));

        UserCalendar calendar = new UserCalendar(null);
        repository = new UserRepository();
        eventRepository = new EventRepository();
    	eventManager = new EventManager(repository, eventRepository);
        repository.saveUser(new User("Charles", "12345", calendar));
        repository.saveUser(exampleInvitee); // CHANGE

        exampleTime = LocalDateTime.of(2026, 1, 1, 11, 0);
        event = new CreatedEvent("testEvent", 60, "testEvent", exampleOrganizer, null);
        invite = new Invite(exampleInvitee.getUsername(), event.getEventId(), null);
    }

    // Test Getters
    public void testEventName() {assertEquals("testEvent", event.getEventName());}
    public void testEventTime() {assertNull(event.getEventTime());}
    public void testEventId() {assertNotNull(event.getEventId());}
    public void testOrganizer() {
        assertEquals(exampleOrganizer,event.getOrganizer());
    }
    public void testDescription() {
        assertEquals("testEvent",event.getEventDescription());
    }
    public void testDuration() {assertEquals(60,event.getEventDuration()); }
    
    public void testGetParticipants() {
    	eventManager.addInvite(event,exampleInvitee);
    	List<String> expected = List.of(invite.getRecipient());
    	assertEquals(expected, event.getParticipants());
    }
    

    public void testTimeString() {
    	event.setEventTime(exampleTime);
    	String expected = "Jan 01, 2026 11:00 - 12:00";
    	assertEquals(expected,event.getTimeString());
    }
    
}
