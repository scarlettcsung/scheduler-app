package test;

import EventManager.EventManager;
import Invite.Invite;
import Repository.EventRepository;
import User.User;
import UserCalendar.UserCalendar;
import event.*;
import Repository.UserRepository;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Unit tests for {@link event.Event}, {@link event.CreatedEvent}, and {@link event.ImportedEvent}.
 *
 * @author NJ
 * @version TODO
 */
public class testEvent extends TestCase {

    private String exampleOrganizer;
    private Event event;
    private LocalDateTime example_time;
    private Invite invite;
    private User exampleInvitee;
    private UserRepository repository;
    private EventRepository eventRepository;
    private EventManager eventManager;
    
    protected void setUp() {
        exampleOrganizer = "Charles";
        exampleInvitee = new User("Joe", "67890", new UserCalendar(null));

        UserCalendar calendar = new UserCalendar(null);
        repository = new UserRepository();
        eventRepository = new EventRepository();
    	eventManager = new EventManager(repository, eventRepository);
        repository.saveUser(new User("Charles", "12345", calendar));
        repository.saveUser(exampleInvitee); // CHANGE

        example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
        event = new CreatedEvent("testEvent", 60, "testEvent", exampleOrganizer, null);
        invite = new Invite(exampleInvitee.getUsername(), event.getEventID());
    }

    // Test Getters
    public void testEventName() {assertEquals("testEvent", event.getEventName());}
    public void testEventTime() {assertNull(event.getEventTime());}
    public void testEventID() {assertNotNull(event.getEventID());}
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
    
    public void testHasExistingInvite() {
    	eventManager.addInvite(event, exampleInvitee);
    	assertTrue(event.hasExistingInvite(exampleInvitee.getUsername()));
    	eventManager.removeInvite(event, exampleInvitee);
    	assertFalse(event.hasExistingInvite(exampleInvitee.getUsername()));
    }
    
}
