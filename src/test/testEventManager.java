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

    public void testUpdateEventShouldChangeEvent() {
        eveUpdateEvent.updateEvent(event, "eventName", "abc");
        assertEquals("abc", event.getEventName());
        
        eveUpdateEvent.updateEvent(event, "eventDescription", "abc");
        assertEquals("abc", event.getEventDescription());
        
        eveUpdateEvent.updateEvent(event, "eventTime", "2026-04-01T12:00");
        assertEquals(LocalDateTime.of(2026, 4, 1, 12, 0), event.getEventTime());
        
        eveUpdateEvent.updateEvent(event, "eventDuration", "5");
        assertEquals(5, event.getEventDuration());
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
    
    // Test the null check at the top of deleteEvent
    public void testDeleteNullEvent() {
        EventManager manager = new EventManager();
        manager.deleteEvent(null); // if the system does not crash it is correct?
    }
    
    //test invitee not in repository
    public void testDeleteEventInviteeNotInRepo() {
    	//setup Event for this test
    	UserRepository repo = new UserRepository();
        UserCalendar orgCal = new UserCalendar("testUser", null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        Event e = new Event("meeting", 60, "desc", "testUser", false, null);
        
        //add event to calendar
        orgCal.addEvent(e);
        e.getInvites().add(new Invite("ghostUser", e.getEventID()));
        
        //test
        orgCal.addEvent(e);
        e.getInvites().add(new Invite("ghostUser", e.getEventID()));
    }
    
    //test invitee has no calendar
    public void testDeleteEventInviteeHasNoCalendar() {
    	//setup event for test
    	UserRepository repo = new UserRepository();
        UserCalendar orgCal = new UserCalendar("testUser", null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        repo.saveUser(new User("inviteeUser", "password", null)); // no calendar
        Event e = new Event("meeting", 60, "desc", "testUser", false, null);
        
        //add event to calendar
        orgCal.addEvent(e);
        e.getInvites().add(new Invite("inviteeUser", e.getEventID()));
        
        //test
        new EventManager(repo).deleteEvent(e);
        assertFalse(orgCal.getEvents().contains(e));
    }
}