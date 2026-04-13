package test;

import junit.framework.TestCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Event.Event;
import EventManager.EventManager;
import Invite.Invite;
import Invite.inviteStatus;
import User.User;
import UserCalendar.UserCalendar;
import Repository.UserRepository;

public class testEventManager extends TestCase {

    String exampleOrganizer = "testUser";
    LocalDateTime example_date = LocalDateTime.of(2026, 4, 1, 12, 0);
    EventManager eveUpdateEvent = new EventManager();
    Event event = new Event("osman", 60, "testEvent", exampleOrganizer, false, null);

    public void testUpdateEventShouldChangeEventName() {
        eveUpdateEvent.updateEvent(event, "eventName", "abc");
        assertEquals("abc", event.getEventName());
    }

    public void testDeleteEvent() {
        // Set up repository
        UserRepository repo = new UserRepository();

        // Create organizer with a calendar
        UserCalendar cal = new UserCalendar("testUser", null);
        User organizer = new User("testUser", "password", cal);
        cal.addEvent(event);

        // Save organizer to repository
        repo.saveUser(organizer);

        // Create EventManager with the repository
        EventManager managerWithRepo = new EventManager(repo);

        // Verify event is in calendar before deletion
        assertTrue(cal.getEvents().contains(event));

        // Delete the event
        managerWithRepo.deleteEvent(event);

        // Verify event is removed from calendar after deletion
        assertFalse(cal.getEvents().contains(event));
    }
    
    public void testInviteReject() {
    	
    	UserCalendar calendar = new UserCalendar(exampleOrganizer,null);
    	// Set up repository
    	UserRepository repository = new UserRepository();
    	repository.saveUser(new User("Charles","12345",calendar));
    	repository.saveUser(new User("Joe","67890",calendar));
    	String exampleInvitee = "Joe";
    	Invite invite = new Invite(exampleInvitee,event.getEventID());
    	List<Invite> expected = new ArrayList<>();
        event.addInvite(invite, repository);
        eveUpdateEvent.rejectInvite(invite,event,repository);
        assertEquals(expected,event.getInvites());
        User exampleUser = repository.findUsername(exampleInvitee);
        assertFalse(exampleUser.getCalendar().getEvents().contains(event));
        assertEquals(inviteStatus.REJECTED,invite.getStatus());
    }
}