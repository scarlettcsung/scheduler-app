package test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import event.service.EventInviteView;
import event.service.EventQueryService;
import invite.Invite;
import repository.EventRepository;
import user.User;
import junit.framework.TestCase;

/**
 * Unit tests for {@link event.service.EventQueryService}.
 *
 * @author EO SN
 * @version 1
 */
public class TestEventQueryService extends TestCase {

	private EventRepository eventRepository;
	private EventQueryService eventQueryService;
	private EventManager eventManager;
	private User alice;
	private User bob;
	private User charlie;

	protected void setUp() {
		eventRepository = new EventRepository();
		eventQueryService = new EventQueryService(eventRepository);
		eventManager = new EventManager();
		alice = new User("alice","12345");
		bob = new User("bob","12345");
		charlie = new User("charlie","12345");
	}

	public void testVisibleEventsForOrganizer() {
		Event ownEvent = new CreatedEvent("Planning", 60, "Sprint planning", null);
		ownEvent.setOrganizer(alice.getUsername());
		Event otherEvent = new CreatedEvent("Review", 30, "Project review", null);
		otherEvent.setOrganizer(bob.getUsername());

		eventRepository.save(ownEvent);
		eventRepository.save(otherEvent);

		List<Event> visibleEvents = eventQueryService.getVisibleEventsForUser("alice");

		assertTrue(visibleEvents.contains(ownEvent));
		assertFalse(visibleEvents.contains(otherEvent));
	}

	public void testVisibleEventsForAcceptedInvitee() {
		Event acceptedEvent = new CreatedEvent("Demo", 45, "Accepted invite", null);
		acceptedEvent.setOrganizer(bob.getUsername());
		Invite acceptedInvite = new Invite("alice", acceptedEvent.getEventId(), null);
		acceptedInvite.accept();
		acceptedEvent.getInvites().add(acceptedInvite);

		Event pendingEvent = new CreatedEvent("Workshop", 90, "Pending invite", null);
		pendingEvent.setOrganizer(charlie.getUsername());
		pendingEvent.getInvites().add(new Invite("alice", pendingEvent.getEventId(), null));

		eventRepository.save(acceptedEvent);
		eventRepository.save(pendingEvent);

		List<Event> visibleEvents = eventQueryService.getVisibleEventsForUser("alice");

		assertTrue(visibleEvents.contains(acceptedEvent));
		assertFalse(visibleEvents.contains(pendingEvent));
	}

	public void testInvitesForUserDeduplicatesByRecipientAndEvent() {
		Event event = new CreatedEvent("Design", 30, "Discuss design", null);
		event.setOrganizer(bob.getUsername());
		event.getInvites().add(new Invite("alice", event.getEventId(), null));
		event.getInvites().add(new Invite("alice", event.getEventId(), null));
		event.getInvites().add(new Invite("charlie", event.getEventId(), null));
		eventRepository.save(event);

		List<EventInviteView> invites = eventQueryService.getInvitesForUser("alice");

		assertEquals(1, invites.size());
		assertEquals(event, invites.get(0).getEvent());
		assertEquals("alice", invites.get(0).getInvite().getRecipient());
	}

	public void testEventsForAdminIncludesAllEvents() {
		Event firstEvent = new CreatedEvent("Planning", 60, "Sprint planning", null);
		firstEvent.setOrganizer(alice.getUsername());
		Event secondEvent = new CreatedEvent("Review", 30, "Project review", null);
		secondEvent.setOrganizer(bob.getUsername());

		eventRepository.save(firstEvent);
		eventRepository.save(secondEvent);

		List<Event> adminEvents = eventQueryService.getEventsForAdmin();

		assertEquals(2, adminEvents.size());
		assertTrue(adminEvents.contains(firstEvent));
		assertTrue(adminEvents.contains(secondEvent));
	}

	public void testInvitesForAdminIncludesAllRecipientsAndDeduplicates() {
		Event event = new CreatedEvent("Design", 30, "Discuss design", null);
		event.setOrganizer(bob.getUsername());
		event.getInvites().add(new Invite("alice", event.getEventId(), null));
		event.getInvites().add(new Invite("alice", event.getEventId(), null));
		event.getInvites().add(new Invite("charlie", event.getEventId(), null));
		eventRepository.save(event);

		List<EventInviteView> invites = eventQueryService.getInvitesForAdmin();

		assertEquals(3, invites.size());
		assertEquals(event, invites.get(0).getEvent());
		assertEquals(event, invites.get(1).getEvent());
	}
	
	public void testVisibleEventsforUser() {
		Set<Event> expectedSet = new LinkedHashSet<>();
		List<Event> expectedList = new ArrayList<>(expectedSet);
		assertEquals(expectedList, eventQueryService.getVisibleEventsForUser(null));
		
	}
	
}
