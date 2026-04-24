package test;

import event.CreatedEvent;
import invite.Role;
import event.Event;
import invite.Invite;
import invite.InviteStatus;
import junit.framework.TestCase;
import user.User;
import user.calendar.UserCalendar;

/**
 * Unit tests for {@link invite.Invite}.
 *
 * @author NJ
 * @version TODO
 */
public class TestInvite extends TestCase{

    private String exampleOrganizer;
    private UserCalendar calendar;
    private Event event;
    private Invite invite;
    private String exampleInvitee;
    private String newInvitee;
    private String eventId;

    protected void setUp() {
        exampleOrganizer = "Charles";
        calendar = new UserCalendar(null);
        event = new CreatedEvent("testEvent", 60,"testEvent", exampleOrganizer, null);
        eventId = event.getEventId();
        exampleInvitee = "Joe";
        invite = new Invite(exampleInvitee,eventId);
        newInvitee = "James";
    }

    // Test Getters
    public void testGetRecipient() {assertEquals(exampleInvitee,invite.getRecipient());}
    public void testGetEvent() {
        assertEquals(eventId,invite.getEventId());
    }

    // Test Update Status
    public void testStatusPending() {assertEquals(InviteStatus.PENDING,invite.getStatus());}
    public void testStatusAccept() {
        invite.accept();
        assertEquals(InviteStatus.ACCEPTED,invite.getStatus());
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
        assertEquals(newEventID, invite.getEventId());
    }
    
    //test setEventStatus
    public void testSetInviteStatus() {
        invite.setInviteStatus(InviteStatus.REJECTED);
        assertEquals(InviteStatus.REJECTED, invite.getStatus());
    }
    // test setOrganizer
    public void testSetOrganizerAndGetRole() {
    	invite.setOrganizer();
    	assertEquals(Role.ORGANIZER,invite.getRole());
    }
    public void testSetGuestAndGetRole() {
    	invite.setGuest();
    	assertEquals(Role.GUEST,invite.getRole());
    }
}
