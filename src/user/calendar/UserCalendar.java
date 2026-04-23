package user.calendar;

import java.util.ArrayList;
import java.util.List;

import event.Event;

/**
 * Stores the events associated with one user.
 *
 * @author SN GI CR NS AA NS AA GI
 * @version 2
 */
public class UserCalendar {
	
	/**
	 * Creates a user calendar backed by the supplied event list.
	 *
	 * @param events initial events, or {@code null} for an empty calendar
	 */
	public UserCalendar(List<Event> events) {
		
		if (events == null) {
            this.events = new ArrayList<>();
        } else {
            this.events = events;
        }
	}
	private List<Event> events;
	
	/**
	 * Returns the events in this calendar.
	 *
	 * @return event list
	 */
	public  List<Event> getEvents() {
        return events;
    }
	
 

    /**
     * Adds an event to this calendar.
     *
     * @param event event to add
     */
    public void addEvent(Event event) {
        events.add(event);
    }
    
    /**
     * Removes an event from this calendar by object identity or matching event ID.
     *
     * @param event event to remove
     */
    public void removeEvent(Event event) {
        if (event == null) {
            return;
        }

        String targetEventId = event.getEventID();
        events.removeIf(n -> n == event
                || (n != null && targetEventId != null && targetEventId.equals(n.getEventID())));
    }
}
