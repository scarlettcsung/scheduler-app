package test;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.util.Date;

public class testInvite extends TestCase{

    private User example_organizer;
    private UserCalendar calendar;
    private Date example_date;
    private Event event;
    private Invite invite;
    private User example_invitee;

    protected void setUp() {
        UserCalendar calendar = new UserCalendar();
        example_organizer = new User("Charles","123456", calendar,false);
        example_date = new Date(126, 0, 01);
        event = new Event("testEvent", example_date,60,"testEvent",
                example_organizer,false,null);
        example_invitee = new User("Joe","789012", calendar,false);
        invite = new Invite(example_invitee,event);
    }

    public void testGetRecipient() {
        assertEquals(example_invitee,invite.getRecipient());
        assertNotSame(example_organizer,invite.getRecipient()); //Test QC
    }

    public void testGetEvent() {
        assertEquals(event,invite.getEvent());
    }

    public void testStatusPending() {
        assertEquals(inviteStatus.PENDING,invite.getStatus());
    }

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
}
