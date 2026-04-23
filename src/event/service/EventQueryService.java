package event.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import event.Event;
import invite.Invite;
import invite.inviteStatus;
import repository.EventRepository;

/**
 * Provides event and invite read models without exposing repository traversal
 * to GUI panels.
 *
 * @author EO SN
 * @version 1
 */
public class EventQueryService {

    private final EventRepository eventRepository;

    /**
     * Creates a query service backed by the given event repository.
     *
     * @param eventRepository repository used for event reads
     */
    public EventQueryService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Returns events visible to a user: events they organize and events where
     * they accepted an invite.
     *
     * @param username username to query for
     * @return visible events in repository order
     */
    public List<Event> getVisibleEventsForUser(String username) {
        Set<Event> visibleEvents = new LinkedHashSet<>();

        if (username == null) {
            return new ArrayList<>(visibleEvents);
        }

        for (Event event : eventRepository.getAll()) {
            if (username.equals(event.getOrganizer()) || hasAcceptedInvite(event, username)) {
                visibleEvents.add(event);
            }
        }

        return new ArrayList<>(visibleEvents);
    }

    /**
     * Returns all events for admin event-list rendering.
     *
     * @return all events in repository order
     */
    public List<Event> getEventsForAdmin() {
        return new ArrayList<>(eventRepository.getAll());
    }

    /**
     * Returns unique invite views for one recipient.
     *
     * @param username recipient username to query for
     * @return invite views in repository order
     */
    public List<EventInviteView> getInvitesForUser(String username) {
        return collectInviteViews(username);
    }

    /**
     * Returns unique invite views across all events for admin rendering.
     *
     * @return invite views in repository order
     */
    public List<EventInviteView> getInvitesForAdmin() {
        return collectInviteViews(null);
    }

    private boolean hasAcceptedInvite(Event event, String username) {
        if (event.getInvites() == null) {
            return false;
        }

        for (Invite invite : event.getInvites()) {
            if (username.equals(invite.getRecipient()) && invite.getStatus() == inviteStatus.ACCEPTED) {
                return true;
            }
        }

        return false;
    }

    private List<EventInviteView> collectInviteViews(String recipientFilter) {
        Set<String> seen = new LinkedHashSet<>();
        List<EventInviteView> inviteViews = new ArrayList<>();

        for (Event event : eventRepository.getAll()) {
            if (event.getInvites() == null) {
                continue;
            }

            for (Invite invite : event.getInvites()) {
                if (recipientFilter != null && !recipientFilter.equals(invite.getRecipient())) {
                    continue;
                }

                String key = invite.getRecipient() + "|" + invite.getEventID();
                if (seen.add(key)) {
                    inviteViews.add(new EventInviteView(event, invite));
                }
            }
        }

        return inviteViews;
    }
}
