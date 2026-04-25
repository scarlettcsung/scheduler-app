package test;

import junit.framework.TestCase;
import repository.EventRepository;
import repository.UserRepository;
import user.User;

import java.time.LocalDateTime;
import java.util.List;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import event.manager.InviteManager;
import invite.Invite;
import invite.InviteStatus;
import invite.Role;
import ics.importer.ImportStatus;


/**
 * Unit tests for {@link event.EventManager}.
 *
 * @author GI NJ
 * @version 2
 */
public class TestEventManager extends TestCase {
	
	private UserRepository repository;
	private EventRepository eventRepository;
	private EventManager eveUpdateEvent;
	private InviteManager inviteManager;

    User exampleOrganizer = new User("Charles","12345");
    LocalDateTime example_date = LocalDateTime.of(2026, 4, 1, 12, 0);
    Event event = new CreatedEvent("osman", 60, "testEvent", null);
    LocalDateTime example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
    
	// Set up repository
	User exampleInvitee = new User("Joe", "67890");
	User exampleNewOrganizer = new User("Jennifer","1234");
	Invite invite = new Invite(exampleInvitee.getUsername(),event.getEventId(), null);
	
	public void setUp() {
		repository = new UserRepository();
		eventRepository = new EventRepository();
		eveUpdateEvent = new EventManager(repository, eventRepository);
		inviteManager = new InviteManager(repository);
		repository.saveUser(new User("Charles","12345"));
		repository.saveUser(exampleInvitee);
		repository.saveUser(exampleNewOrganizer);
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

        // Create organizer
        User organizer = new User("testUser", "password");

        // Save organizer to repository
        repo.saveUser(organizer);

        // Create EventManager with the repository
        Event event = new CreatedEvent("meeting", 60, "desc", null);
        event.setOrganizer(organizer.getUsername());
        eventRepo.save(event);
        EventManager managerWithRepo = new EventManager(repo, eventRepo);

        // Verify event is in calendar before deletion]
        List<Event> cal = eventRepo.getUserCalendar(organizer.getUsername());
        assertTrue(cal.contains(event));

        // Delete the event
        managerWithRepo.deleteEvent(event);

        // Verify event is removed from calendar after deletion
        cal = eventRepo.getUserCalendar(organizer.getUsername());
        assertFalse(cal.contains(event));
    }
    
    public void testInviteReject() {
    	inviteManager.addInvite(event,exampleInvitee,Role.GUEST);
    	EventRepository eventRepo = new EventRepository();
        eveUpdateEvent.rejectInvite(invite,event);
        assertEquals(0, event.getInvites().size());
        List<Event> inviteeEvents = eventRepo.getUserCalendar(exampleInvitee.getUsername());
        assertFalse(inviteeEvents.contains(event));
        assertEquals(InviteStatus.REJECTED,invite.getStatus());
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
        User organizer = new User("testUser", "password");
        repo.saveUser(organizer);
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        
        //add event to calendar
        e.getInvites().add(new Invite("ghostUser", e.getEventId(), null));
        
        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(eventRepo.getUserCalendar(organizer.getUsername()).contains(e));
    }
 
    
 // Covers invitee calendar removeEvent line
    public void testDeleteEventWithInvites() {
    	//setup
        UserRepository repo = new UserRepository();
        EventRepository eventRepo = new EventRepository();
        User organizer = new User("testUser", "password");
        repo.saveUser(organizer);
        User invitee = new User("inviteeUser", "password");
        repo.saveUser(invitee);
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        
        //add to calendar
        e.getInvites().add(new Invite("inviteeUser", e.getEventId(), null));
        
        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(eventRepo.getUserCalendar(organizer.getUsername()).contains(e));
        assertFalse(eventRepo.getUserCalendar(invitee.getUsername()).contains(e));
    }
    
    // Covers the path where repository is null, hitting the final closing bracket?
    public void testDeleteEventNoRepository() {
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        new EventManager().deleteEvent(e); 
    }
    
    // Test recipientUsername == null branch
    public void testDeleteEventNullRecipientUsername() {
    	//setup
        UserRepository repo = new UserRepository();
        EventRepository eventRepo = new EventRepository();
        User organizer = new User("testUser", "password");
        repo.saveUser(organizer);
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        
        //add to calendar
        e.getInvites().add(new Invite(null, e.getEventId(), null));

        //test
        new EventManager(repo, eventRepo).deleteEvent(e);
        assertFalse(eventRepo.getUserCalendar(null).contains(e));
    }
    
    // Test organizerUsername == null branch
    public void testDeleteEventNullOrganizerUsername() {
        UserRepository repo = new UserRepository();
        Event e = new CreatedEvent("meeting", 60, "desc", null); // null organizer
        new EventManager(repo, new EventRepository()).deleteEvent(e);
    }
    
    // Covers organizer not found in repo (organizer == null) branch
    public void testDeleteEventOrganizerNotInRepo() {
        UserRepository repo = new UserRepository();
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        new EventManager(repo, new EventRepository()).deleteEvent(e);
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
    
    public void testSetOrganizer() {
    	event.setOrganizer(exampleNewOrganizer.getUsername());
    	assertEquals(exampleNewOrganizer.getUsername(),event.getOrganizer());
    }
    
    
    public void testGetOrganizer() {
        Event e = new CreatedEvent("meeting", 60, "desc", null);
        e.setOrganizer(exampleNewOrganizer.getUsername());
        User result = eveUpdateEvent.getOrganizer(e);
        assertEquals(exampleNewOrganizer, result);
    }
    
    public void testReturnOrganizedEvents() {
        Event event1 = new CreatedEvent("Team Meeting", 60, "Weekly sync", null);
        Invite invite1 = new Invite(exampleNewOrganizer.getUsername(), event1.getEventId(), null);
        invite1.setOrganizer(); 
        event1.getInvites().add(invite1);
        
        Event event2 = new CreatedEvent("Project Deadline", 120, "Work session", null);
        Invite invite2 = new Invite(exampleNewOrganizer.getUsername(), event2.getEventId(), null);
        invite2.setOrganizer();
        event2.getInvites().add(invite2);
        
        Event event3 = new CreatedEvent("Lunch", 45, "Food", null);
        Invite invite3 = new Invite("Charles", event3.getEventId(), null);
        invite3.setOrganizer();
        event3.getInvites().add(invite3);
        
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        List<Event> jennifersEvents = eveUpdateEvent.returnOrganisedEvents(exampleNewOrganizer.getUsername(), eventRepository);

        assertEquals(2, jennifersEvents.size());
        assertFalse(jennifersEvents.contains(event3));
    }

    public void testReturnParticipatingEvents() {
        Event event1 = new CreatedEvent("Team Meeting", 60, "Weekly sync", null);
        
        Invite invite1 = new Invite(exampleNewOrganizer.getUsername(), event1.getEventId(), null);
        invite1.setOrganizer();
        event1.getInvites().add(invite1);

        Invite invite2 = new Invite(exampleInvitee.getUsername(), event1.getEventId(), null);
        invite2.setGuest(); 
        event1.getInvites().add(invite2); 
        
        eventRepository.save(event1);

        List<Event> joesEvents = eveUpdateEvent.returnParticipatingEvents(exampleInvitee.getUsername(), eventRepository);
        List<Event> jennifersParticipatingEvents = eveUpdateEvent.returnParticipatingEvents(exampleNewOrganizer.getUsername(), eventRepository);

        assertEquals(1, joesEvents.size());
        assertEquals("Team Meeting", joesEvents.get(0).getEventName());
        assertEquals(0, jennifersParticipatingEvents.size());
    }

    public void testImportIcs() {
        String testIcsPath = "src/test/resources/simpleImport.ics";
        User user = new User("ImportUser", "pass");
        repository.saveUser(user);

        ImportStatus status = eveUpdateEvent.importIcs(user, testIcsPath);

        assertEquals(ics.importer.ImportStatus.Succes, status);
        
        // Verify events are in the central repository
        int importedCount = 0;
        for (event.Event e : eventRepository.getAll()) {
            if (e.isImported() && "ImportUser".equals(e.getOrganizer())) {
                importedCount++;
            }
        }
        assertTrue(importedCount > 0);
    }
}
