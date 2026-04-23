package test;

import java.util.List;

import event.CreatedEvent;
import event.Event;
import event.service.EventInviteView;
import event.service.EventQueryService;
import invite.Invite;
import repository.EventRepository;
import junit.framework.TestCase;

/**
 * Unit tests for {@link event.service.EventQueryService}.
 *
 * @author EO SN
 * @version 1
 */
public class testEventQueryService extends TestCase {

	private EventRepository eventRepository;
	private EventQueryService eventQueryService;

	protected void setUp() {
		eventRepository = new EventRepository();
		eventQueryService = new EventQueryService(eventRepository);
	}

	public void testVisibleEventsForOrganizer() {
		Event ownEvent = new CreatedEvent("Planning", 60, "Sprint planning", "alice", null);
		Event otherEvent = new CreatedEvent("Review", 30, "Project review", "bob", null);

		eventRepository.save(ownEvent);
		eventRepository.save(otherEvent);

		List<Event> visibleEvents = eventQueryService.getVisibleEventsForUser("alice");

		assertTrue(visibleEvents.contains(ownEvent));
		assertFalse(visibleEvents.contains(otherEvent));
	}

	public void testVisibleEventsForAcceptedInvitee() {
		Event acceptedEvent = new CreatedEvent("Demo", 45, "Accepted invite", "bob", null);
		Invite acceptedInvite = new Invite("alice", acceptedEvent.getEventID());
		acceptedInvite.accept();
		acceptedEvent.getInvites().add(acceptedInvite);

		Event pendingEvent = new CreatedEvent("Workshop", 90, "Pending invite", "charlie", null);
		pendingEvent.getInvites().add(new Invite("alice", pendingEvent.getEventID()));

		eventRepository.save(acceptedEvent);
		eventRepository.save(pendingEvent);

		List<Event> visibleEvents = eventQueryService.getVisibleEventsForUser("alice");

		assertTrue(visibleEvents.contains(acceptedEvent));
		assertFalse(visibleEvents.contains(pendingEvent));
	}

	public void testInvitesForUserDeduplicatesByRecipientAndEvent() {
		Event event = new CreatedEvent("Design", 30, "Discuss design", "bob", null);
		event.getInvites().add(new Invite("alice", event.getEventID()));
		event.getInvites().add(new Invite("alice", event.getEventID()));
		event.getInvites().add(new Invite("charlie", event.getEventID()));
		eventRepository.save(event);

		List<EventInviteView> invites = eventQueryService.getInvitesForUser("alice");

		assertEquals(1, invites.size());
		assertEquals(event, invites.get(0).getEvent());
		assertEquals("alice", invites.get(0).getInvite().getRecipient());
	}

	public void testEventsForAdminIncludesAllEvents() {
		Event firstEvent = new CreatedEvent("Planning", 60, "Sprint planning", "alice", null);
		Event secondEvent = new CreatedEvent("Review", 30, "Project review", "bob", null);

		eventRepository.save(firstEvent);
		eventRepository.save(secondEvent);

		List<Event> adminEvents = eventQueryService.getEventsForAdmin();

		assertEquals(2, adminEvents.size());
		assertTrue(adminEvents.contains(firstEvent));
		assertTrue(adminEvents.contains(secondEvent));
	}

	public void testInvitesForAdminIncludesAllRecipientsAndDeduplicates() {
		Event event = new CreatedEvent("Design", 30, "Discuss design", "bob", null);
		event.getInvites().add(new Invite("alice", event.getEventID()));
		event.getInvites().add(new Invite("alice", event.getEventID()));
		event.getInvites().add(new Invite("charlie", event.getEventID()));
		eventRepository.save(event);

		List<EventInviteView> invites = eventQueryService.getInvitesForAdmin();

		assertEquals(2, invites.size());
		assertEquals(event, invites.get(0).getEvent());
		assertEquals(event, invites.get(1).getEvent());
	}
}
