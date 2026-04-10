package test;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

public class testInvite extends TestCase{

    private String exampleOrganizer;
    private UserCalendar calendar;
    private Event event;
    private Invite invite;
    private String exampleInvitee;
    private String newInvitee;
    private String eventID;

    protected void setUp() {
        exampleOrganizer = "Charles";
        calendar = new UserCalendar(exampleOrganizer,null);
        event = new Event("testEvent", 60,"testEvent",
        		exampleOrganizer,false,null);
        eventID = event.getEventID();
        exampleInvitee = "Joe";
        invite = new Invite(exampleInvitee,eventID);
        newInvitee = "James";
    }

    // Test Getters
    public void testGetRecipient() {assertEquals(exampleInvitee,invite.getRecipient());}
    public void testGetEvent() {
        assertEquals(eventID,invite.getEventID());
    }

    // Test Update Status
    public void testStatusPending() {assertEquals(inviteStatus.PENDING,invite.getStatus());}
    public void testStatusAccept() {
        invite.accept();
        assertEquals(inviteStatus.ACCEPTED,invite.getStatus());
    }

    // Test Setters
    public void testSetRecipient() {
        invite.setRecipient(newInvitee);
        assertEquals(newInvitee,invite.getRecipient());
    }
    
    //test setEvent
    public void testSetEvent() {
        String newEventID = "newEventID";
        invite.setEvent(newEventID);
        assertEquals(newEventID, invite.getEventID());
    }
    
    //test setEventStatus
    
}
