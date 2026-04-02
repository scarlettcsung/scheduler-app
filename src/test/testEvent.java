package test;

import Event.Event;
import Invite.Invite;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class testEvent extends TestCase {

    private User example_organizer;
    private Event event;
    private LocalDateTime example_time;
    private Invite invite;
    private User example_invitee;

    protected void setUp() {
        UserCalendar calendar = new UserCalendar();
        example_organizer = new User("Charles","123456", calendar,false);
        example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
        event = new Event("testEvent", example_time,60,"testEvent",
                example_organizer,false,null);
        example_invitee = new User("Joe","789012", calendar,false);
        invite = new Invite(example_invitee,event);
    }

    // Test Getters
    public void testEventName() {assertEquals("testEvent", event.getEventName());}
    public void testEventTime() {
        assertEquals(example_time,event.getEventTime());
    }
    public void testOrganizer() {
        assertEquals(example_organizer,event.getOrganizer());
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
        event.addInvite(invite);
        List<Invite> expected = List.of(invite);
        assertEquals(expected,event.getInvites());
    }
    public void testRemoveInvite() {
        List<User> expected = new ArrayList<>();
        event.removeInvite(invite);
        assertEquals(expected,event.getInvites());
    }

    // Test Setters
    public void testSetEventName() {
        event.setEventName("testEvent2");
        assertNotSame("testEvent",event.getEventName());
        assertEquals("testEvent2",event.getEventName());
    }
    public void testSetEventTime() {
        LocalDateTime new_time = LocalDateTime.of(2026, 1, 1, 11, 0);
        event.setEventTime(new_time);
        assertNotSame(example_time,event.getEventName());
        assertEquals(new_time,event.getEventName());
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


}