package test;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;
import event.CreatedEvent;
import event.Event;
import invite.Invite;
import invite.Role;
import event.manager.InviteManager;
import repository.UserRepository;
import user.User;
import user.calendar.UserCalendar;

/**
 * Tests for the InviteManager class.
 */
public class TestInviteManager extends TestCase {

    private UserRepository repository;
    private InviteManager inviteManager;
    private Event event;
    private User exampleInvitee;
    private User exampleNewOrganizer;
    private String exampleOrganizer = "testUser";
    private UserCalendar calendar;

    public void setUp() {
        repository = new UserRepository();
        inviteManager = new InviteManager(repository);
        
        exampleInvitee = new User("Joe", "67890", new UserCalendar(null));
        exampleNewOrganizer = new User("Jennifer", "1234", new UserCalendar(null));
        
        event = new CreatedEvent("osman", 60, "testEvent", null);
        
        repository.saveUser(exampleInvitee);
        repository.saveUser(exampleNewOrganizer);
    }

    public void testAddTemporaryInviteSuccess() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleInvitee.getUsername()
        );

        assertNull(result);
        assertEquals(1, tempInvites.size());
        assertTrue(tempInvites.contains(exampleInvitee.getUsername()));
    }

    public void testAddTemporaryInviteRejectsEmptyUsername() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                ""
        );

        assertEquals("Please enter a username.", result);
        assertTrue(tempInvites.isEmpty());
    }

    public void testAddTemporaryInviteRejectsOrganizer() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleNewOrganizer.getUsername()
        );

        assertEquals("Event organizer already invited to this event.", result);
        assertTrue(tempInvites.isEmpty());
    }

    public void testAddTemporaryInviteRejectsUnknownUser() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                "ghostUser"
        );

        assertEquals("User not found.", result);
        assertTrue(tempInvites.isEmpty());
    }

    public void testAddTemporaryInviteRejectsDuplicateTemporaryInvite() {
        List<String> tempInvites = new ArrayList<>();
        tempInvites.add(exampleInvitee.getUsername());

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleInvitee.getUsername()
        );

        assertEquals("User is already invited to this event!", result);
        assertEquals(1, tempInvites.size());
    }

    public void testAddTemporaryInviteRejectsExistingEventInvite() {
        List<String> tempInvites = new ArrayList<>();
        event.getInvites().add(new Invite(exampleInvitee.getUsername(), event.getEventId(), null));

        String result = inviteManager.addTemporaryInvite(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleInvitee.getUsername()
        );

        assertEquals("User is already invited to this event!", result);
        assertTrue(tempInvites.isEmpty());
    }

    public void testRemoveInviteFromFormSuccess() {
        List<String> tempInvites = new ArrayList<>();
        tempInvites.add(exampleInvitee.getUsername());

        String result = inviteManager.removeInviteFromForm(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleInvitee.getUsername()
        );

        assertNull(result);
        assertTrue(tempInvites.isEmpty());
    }

    public void testRemoveInviteFromFormRejectsEmptyUsername() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.removeInviteFromForm(
                exampleNewOrganizer,
                event,
                tempInvites,
                ""
        );

        assertEquals("Please enter a username.", result);
    }

    public void testRemoveInviteFromFormRejectsOrganizer() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.removeInviteFromForm(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleNewOrganizer.getUsername()
        );

        assertEquals("You cannot uninvite the organizer.", result);
    }

    public void testRemoveInviteFromFormRejectsUnknownUser() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.removeInviteFromForm(
                exampleNewOrganizer,
                event,
                tempInvites,
                "ghostUser"
        );

        assertEquals("User not found.", result);
    }

    public void testRemoveInviteFromFormRejectsMissingInvite() {
        List<String> tempInvites = new ArrayList<>();

        String result = inviteManager.removeInviteFromForm(
                exampleNewOrganizer,
                event,
                tempInvites,
                exampleInvitee.getUsername()
        );

        assertEquals("User is not in this event!", result);
    }
    public void testAddInvite() {
        inviteManager.addInvite(event, exampleInvitee, Role.GUEST);
        assertEquals(1, event.getInvites().size());
        assertEquals(exampleInvitee.getUsername(), event.getInvites().get(0).getRecipient());
        assertTrue(exampleInvitee.getCalendar().getEvents().contains(event));
    }

    public void testAddInviteDuplicate() {
        inviteManager.addInvite(event, exampleInvitee, Role.GUEST);
        inviteManager.addInvite(event, exampleInvitee, Role.GUEST);
        assertEquals(1, event.getInvites().size());
    }

    public void testHasExistingInvite() throws Exception {
        java.lang.reflect.Method method = InviteManager.class.getDeclaredMethod("hasExistingInvite", Event.class, String.class);
        method.setAccessible(true);
        
        boolean hasInvite = (boolean) method.invoke(inviteManager, event, exampleInvitee.getUsername());
        assertFalse(hasInvite);
        
        inviteManager.addInvite(event, exampleInvitee, Role.GUEST);
        hasInvite = (boolean) method.invoke(inviteManager, event, exampleInvitee.getUsername());
        assertTrue(hasInvite);
    }

    public void testRemoveInvite() {
        inviteManager.addInvite(event, exampleInvitee, Role.GUEST);
        inviteManager.removeInvite(event, exampleInvitee);
        assertEquals(0, event.getInvites().size());
        assertFalse(exampleInvitee.getCalendar().getEvents().contains(event));
    }

    public void testRemoveInviteUserNotInRepo() {
        User ghostUser = new User("ghostUser", "1234", new UserCalendar(null));
        Invite ghostInvite = new Invite(ghostUser.getUsername(), event.getEventId(), null);
        event.getInvites().add(ghostInvite);
        inviteManager.removeInvite(event, ghostUser);
        assertEquals(0, event.getInvites().size());
    }
}
