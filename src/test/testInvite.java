package test;

import event.CreatedEvent;
import invite.Role;
import event.Event;
import invite.Invite;
import invite.inviteStatus;
import junit.framework.TestCase;
import user.User;
import user.calendar.UserCalendar;

/**
 * Unit tests for {@link invite.Invite}.
 *
 * @author NJ
 * @version TODO
 */
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
        calendar = new UserCalendar(null);
        event = new CreatedEvent("testEvent", 60,"testEvent", exampleOrganizer, null);
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
    public void testSetInviteStatus() {
        invite.setInviteStatus(inviteStatus.REJECTED);
        assertEquals(inviteStatus.REJECTED, invite.getStatus());
    }
    //test setOrganiser 
    public void testSetOrganizerandgetRole() {
    	invite.setOrganiser();
    	assertEquals(Role.Organiser,invite.getRole());
    }
    public void testSetGuestandgetRole() {
    	invite.setGuest();
    	assertEquals(Role.Guest,invite.getRole());
    }
}
