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
        event = new Event("testEvent", example_date,60,"testEvent",
                example_organizer,false,null);
        example_invitee = new User("Joe","789012", calendar,false);
        invite = new Invite(example_invitee,event);
    }

    public void testEventName() {
        assertEquals("testEvent", event.getEventName());
        assertNotSame("wrongEvent",event.getEventName()); //Test QC
    }

    public void testEventTime() {
        assertEquals(example_time,event.getEventTime());
    }

    public void testOrganizer() {
        assertEquals(example_organizer,event.getOrganizer());
    }

    public void testInvites() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected,event.getInvites());
    }

    public void testAddInvite() {
        event.addInvite(invite);
        List<Invite> example_invites = List.of(invite);
        assertEquals(example_invites,event.getInvites());

    }
}