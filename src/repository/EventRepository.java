package repository;

import java.util.ArrayList;
import java.util.List;

import event.Event;
import invite.Invite;

/**
 * In-memory repository for {@link event} instances.
 *
 * @author CR EO
 * @version 3
 */
public class EventRepository extends Repository<Event> {
    /**
     * Creates an empty event repository.
     */
    public EventRepository() {
        super();
    }

    /**
     * Finds an event by its identifier.
     *
     * @param eventId identifier to search for
     * @return matching event, or {@code null} when not found
     */
    @Override
    public Event getItemById(String eventId) {
        for (Event e : this.data) {
            String currentEventId = e.getEventId();
            if (currentEventId != null && currentEventId.equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Deletes an event by its identifier.
     *
     * @param eventId identifier of the event to remove
     * @return {@code 1} when an event was removed, otherwise {@code 0}
     */
    public int deleteItem(String eventId) {
        if (this.data.removeIf(e -> {
            String currentEventId = e.getEventId();
            return currentEventId != null && currentEventId.equals(eventId);
        })) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns the repository type label.
     *
     * @return repository type name
     */
    @Override
    public String getRepositoryType() {
        return "Event Repository";
    }

    /**
     * Deletes all events organized by the given user.
     *
     * @param username organizer username whose events should be removed
     */
    public void deleteEventsByOrganizer(String username) {
        for (Event e : this.data) {
            if (e.getOrganizer().equals(username)) {
                deleteItem(e.getEventId());
            }
        }
    }
    
    public List<Event> getUserCalendar(String username) {
    	List<Event> userEvents = new ArrayList<>();
    	for (Event e: this.data) {
    		for (Invite i : e.getInvites()) {
    			if (i.getRecipient().equals(username)) {
    				userEvents.add(e);
    				break;
    			}		
    		}
    	}
    	return userEvents;
    }
}
