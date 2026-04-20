package test;

import junit.framework.TestCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import EventManager.EventManager;
import Invite.Invite;
import Invite.inviteStatus;
import User.User;
import UserCalendar.UserCalendar;
import event.CreatedEvent;
import event.Event;
import Repository.EventRepository;
import Repository.UserRepository;

/**
 * Unit tests for {@link event.EventManager}.
 *
 * @author GI NJ
 * @version TODO
 */
public class testEventManager extends TestCase {
	
	private UserRepository repository;
	private EventRepository eventRepository;
	private EventManager eveUpdateEvent;
	private UserCalendar calendar;

    String exampleOrganizer = "testUser";
    LocalDateTime example_date = LocalDateTime.of(2026, 4, 1, 12, 0);
    Event event = new CreatedEvent("osman", 60, "testEvent", exampleOrganizer, null);
    LocalDateTime example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
    
	// Set up repository
	User exampleInvitee = new User("Joe", "67890", new UserCalendar(null));
	User exampleNewOrganizer = new User("Jennifer","1234", new UserCalendar(null));
	Invite invite = new Invite(exampleInvitee.getUsername(),event.getEventID());
	
	public void setUp() {
		repository = new UserRepository();
		eventRepository = new EventRepository();
		eveUpdateEvent = new EventManager(repository, eventRepository);
		repository.saveUser(new User("Charles","12345",calendar));
		repository.saveUser(exampleInvitee);
		repository.saveUser(exampleNewOrganizer);
		calendar = new UserCalendar(null);
	}
	
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
        EventRepository eventRepo = new EventRepository();

        // Create organizer with a calendar
        UserCalendar cal = new UserCalendar(null);
        User organizer = new User("testUser", "password", cal);
        cal.addEvent(event);

        // Save organizer to repository
        repo.saveUser(organizer);

        // Create EventManager with the repository
        EventManager managerWithRepo = new EventManager(repo, eventRepo);

        // Verify event is in calendar before deletion
        assertTrue(cal.getEvents().contains(event));

        // Delete the event
        managerWithRepo.deleteEvent(event);

        // Verify event is removed from calendar after deletion
        assertFalse(cal.getEvents().contains(event));
    }
    
    public void testInviteReject() {
    	eveUpdateEvent.addInvite(event,exampleInvitee);
        eveUpdateEvent.rejectInvite(invite,event);
        assertEquals(0, event.getInvites().size());
        assertFalse(exampleInvitee.getCalendar().getEvents().contains(event));
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
        EventRepository eventRepo = new EventRepository();
        UserCalendar orgCal = new UserCalendar(null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        
        //add event to calendar
        orgCal.addEvent(e);
        e.getInvites().add(new Invite("ghostUser", e.getEventID()));
        
        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(orgCal.getEvents().contains(e));
    }
 
    //test invitee has no calendar
    public void testDeleteEventInviteeHasNoCalendar() {
    	//setup event for test
    	UserRepository repo = new UserRepository();
        EventRepository eventRepo = new EventRepository();
        UserCalendar orgCal = new UserCalendar(null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        repo.saveUser(new User("inviteeUser", "password", null)); // no calendar
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        
        //add event to calendar
        orgCal.addEvent(e);
        e.getInvites().add(new Invite("inviteeUser", e.getEventID()));
        
        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(orgCal.getEvents().contains(e));
    }
    
 // Covers invitee calendar removeEvent line
    public void testDeleteEventWithInvites() {
    	//setup
        UserRepository repo = new UserRepository();
        EventRepository eventRepo = new EventRepository();
        UserCalendar orgCal = new UserCalendar(null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        UserCalendar inviteeCal = new UserCalendar(null);
        User invitee = new User("inviteeUser", "password", inviteeCal);
        repo.saveUser(invitee);
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        
        //add to calendar
        orgCal.addEvent(e);
        inviteeCal.addEvent(e);
        e.getInvites().add(new Invite("inviteeUser", e.getEventID()));
        
        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(orgCal.getEvents().contains(e));
        assertFalse(inviteeCal.getEvents().contains(e));
    }
    
    // Covers the path where repository is null, hitting the final closing bracket?
    public void testDeleteEventNoRepository() {
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        new EventManager().deleteEvent(e); 
    }
    
    // Test recipientUsername == null branch
    public void testDeleteEventNullRecipientUsername() {
    	//setup
        UserRepository repo = new UserRepository();
        EventRepository eventRepo = new EventRepository();
        UserCalendar orgCal = new UserCalendar(null);
        User organizer = new User("testUser", "password", orgCal);
        repo.saveUser(organizer);
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        
        //add to calendar
        orgCal.addEvent(e);
        e.getInvites().add(new Invite(null, e.getEventID()));

        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(orgCal.getEvents().contains(e));
    }
    
    // Test organizerUsername == null branch
    public void testDeleteEventNullOrganizerUsername() {
        UserRepository repo = new UserRepository();
        Event e = new CreatedEvent("meeting", 60, "desc", null, null); // null organizer
        new EventManager(repo, new EventRepository()).deleteEvent(e);
    }
    
    // Covers organizer not found in repo (organizer == null) branch
    public void testDeleteEventOrganizerNotInRepo() {
        UserRepository repo = new UserRepository();
        Event e = new CreatedEvent("meeting", 60, "desc", "unknownUser", null);
        new EventManager(repo, new EventRepository()).deleteEvent(e);
    }
    
 // Test Invite Methods
    public void testInvites() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected,event.getInvites());
    }
    public void testAddInvite() {
    	
    	event.setEventTime(example_time);
    	eveUpdateEvent.addInvite(event,exampleInvitee);
    	assertEquals(1, event.getInvites().size());
        assertEquals(exampleInvitee.getUsername(), event.getInvites().get(0).getRecipient());
        assertTrue(exampleInvitee.getCalendar().getEvents().contains(event));
    } 
    
  //test duplicates in same calendar
    public void testAddInviteDuplicate() {
    	eveUpdateEvent.addInvite(event,exampleInvitee);
    	eveUpdateEvent.addInvite(event,exampleInvitee);
        assertEquals(1, event.getInvites().size());
    }
    
    public void testRemoveInvite() {
        List<Invite> expected = new ArrayList<>();
        eveUpdateEvent.addInvite(event,exampleInvitee);
        eveUpdateEvent.removeInvite(event,exampleInvitee);
        assertEquals(expected,event.getInvites());
        assertFalse(exampleInvitee.getCalendar().getEvents().contains(event));
    }

    // Test Setters
    public void testSetEventName() {
        event.setEventName("testEvent2");
        assertNotSame("testEvent",event.getEventName());
        assertEquals("testEvent2",event.getEventName());
    }
    public void testSetEventTime() {
        event.setEventTime(example_time);
        assertNotNull("Time was not set",event.getEventTime());
        assertEquals(example_time, event.getEventTime());
    }
    public void testSetEventDescription() {
        event.setEventDescription("testEvent2");
        assertNotSame("testEvent",event.getEventDescription());
        assertEquals("testEvent2",event.getEventDescription());
    }
    public void testSetEventDuration() {
        event.setEventDuration(120);
        assertNotSame(60,event.getEventDuration());
        assertEquals(120,event.getEventDuration());
    }
    // Test removeInvite when user is not found in repository
    public void testRemoveInviteUserNotInRepo() {
        User ghostUser = new User("ghostUser","1234",new UserCalendar(null));
        Invite ghostInvite = new Invite(ghostUser.getUsername(),event.getEventID());
        event.getInvites().add(ghostInvite);
        eveUpdateEvent.removeInvite(event, ghostUser); 
        assertEquals(0, event.getInvites().size()); 
    }
    
    public void testSetOrganizer() {
    	eveUpdateEvent.setOrganizer(event, exampleNewOrganizer);
    	assertEquals(exampleNewOrganizer.getUsername(),event.getOrganizer());
    }
    
    public void testSetOrganizerNullCalendar() {
        User noCalUser = new User("noCalUser", "pass", null); // null calendar
        eveUpdateEvent.setOrganizer(event, noCalUser);
        assertNotNull(noCalUser.getCalendar());
        assertEquals("noCalUser", event.getOrganizer());
        assertTrue(noCalUser.getCalendar().getEvents().contains(event));
    }
    
    public void testConstructorWithRepositoryOnly() {
        UserRepository repo = new UserRepository();
        EventManager manager = new EventManager(repo);
        
        // Verify it doesn't crash and behaves correctly with null eventRepository
        // deleteEvent should still work (repository is set, eventRepository is null)
        UserCalendar cal = new UserCalendar(null);
        User organizer = new User("testUser", "password", cal);
        repo.saveUser(organizer);
        
        Event e = new CreatedEvent("meeting", 60, "desc", "testUser", null);
        cal.addEvent(e);
        
        manager.deleteEvent(e);
        assertFalse(cal.getEvents().contains(e));
    }
    
    public void testGetOrganizer() {
        Event e = new CreatedEvent("meeting", 60, "desc", "Jennifer", null);
        User result = eveUpdateEvent.getOrganizer(e);
        assertEquals(exampleNewOrganizer, result);
    }
}
