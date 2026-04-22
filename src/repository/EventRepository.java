package repository;

import event.Event;

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
	 * @param eventID identifier to search for
	 * @return matching event, or {@code null} when not found
	 */
	@Override
	public Event getItemByID(String eventID) {
        for (Event e : data) {
            if (e.getEventID().equals(eventID)) {
                return e;
            }
        }
        return null;
    }
	
		
	
	
	/**
	 * Deletes an event by its identifier.
	 *
	 * @param eventID identifier of the event to remove
	 * @return {@code true} when an event was removed
	 */
	@Override
	public int deleteItem(String eventID) {
		if(data.removeIf(e -> e.getEventID().equals(eventID)))
		{return 1;}	
		else
		{return 0;}
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

	public void deleteEventsByOrganizer(String username) {
		for(Event e : data) {
			if(e.getOrganizer().equals(username)) {
				deleteItem(e.getEventID());
			}
		}
	}
}
