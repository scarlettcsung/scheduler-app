package test;

import Event.Event;
import Invite.Invite;
import User.User;
import UserCalendar.UserCalendar;
import Repository.UserRepository;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class testEvent extends TestCase {

    private String exampleOrganizer;
    private Event event;
    private LocalDateTime example_time;
    private Invite invite;
    private String exampleInvitee;
    private UserRepository repository;

    protected void setUp() {
    	
    	UserCalendar calendar = new UserCalendar(exampleOrganizer,null);
    	// Set up repository
    	repository = new UserRepository();
    	repository.saveUser(new User("Charles","12345",calendar));
    	repository.saveUser(new User("Joe","67890",calendar));
    			
    	// Set up other inputs
        exampleOrganizer = "Charles";
        example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
        event = new Event("testEvent", 60,"testEvent",
                exampleOrganizer,false,null);
        exampleInvitee = "Joe";
        invite = new Invite(exampleInvitee,event.getEventID());
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

    // Test Invite Methods
    public void testInvites() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected,event.getInvites());
    }
    public void testAddInvite() {
        event.addInvite(invite,repository);
        List<Invite> expected = List.of(invite);
        assertEquals(expected,event.getInvites());
        User exampleUser = repository.findUsername(exampleInvitee);
        assertTrue(exampleUser.getCalendar().getEvents().contains(event));
        
    }
    public void testRemoveInvite() {
        List<Invite> expected = new ArrayList<>();
        event.addInvite(invite, repository);
        event.removeInvite(invite,repository);
        assertEquals(expected,event.getInvites());
        User exampleUser = repository.findUsername(exampleInvitee);
        assertFalse(exampleUser.getCalendar().getEvents().contains(event));
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
        Invite ghostInvite = new Invite("ghostUser", event.getEventID());
        event.getInvites().add(ghostInvite);
        event.removeInvite(ghostInvite, repository); 
        assertEquals(0, event.getInvites().size()); 
    }
    
    // Test addInvite when user is not found in repository
    public void testAddInviteUserNotInRepo() {
        Invite ghostInvite = new Invite("ghostUser", event.getEventID());
        event.addInvite(ghostInvite, repository);
        assertEquals(1, event.getInvites().size()); 
    }


}