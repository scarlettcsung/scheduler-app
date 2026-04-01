package test;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class testInvite extends TestCase{

    private User example_organizer;
    private UserCalendar calendar;
    private LocalDateTime example_time;
    private Event event;
    private Invite invite;
    private User example_invitee;
    private User new_invitee;

    protected void setUp() {
        UserCalendar calendar = new UserCalendar();
        example_organizer = new User("Charles","123456", calendar,false);
        example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
        event = new Event("testEvent", example_time,60,"testEvent",
                example_organizer,false,null);
        example_invitee = new User("Joe","789012", calendar,false);
        invite = new Invite(example_invitee,event);
        new_invitee = new User("James","password",calendar,false);
    }

    // Test Getters
    public void testGetRecipient() {assertEquals(example_invitee,invite.getRecipient());}
    public void testGetEvent() {
        assertEquals(event,invite.getEvent());
    }

    // Test Update Status
    public void testStatusPending() {assertEquals(inviteStatus.PENDING,invite.getStatus());}
    public void testStatusAccept() {
        invite.accept();
        assertEquals(inviteStatus.ACCEPTED,invite.getStatus());
    }
    public void testStatusReject() {
        invite = new Invite(example_invitee,event);
        assertEquals(inviteStatus.PENDING,invite.getStatus());
        invite.reject();
        assertEquals(inviteStatus.REJECTED,invite.getStatus());
    }

    // Test Setters
    public void testSetRecipient() {
        invite.setRecipient(new_invitee);
        assertEquals(new_invitee,invite.getRecipient());
    }

}
