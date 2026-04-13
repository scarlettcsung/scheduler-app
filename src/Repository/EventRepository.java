package Repository;

import Event.Event;

public class EventRepository extends Repository<Event> {
	
	public EventRepository() {
		super();
		
	}
	
	public Event findByEventID(String eventID) {
        for (Event e : data) {
            if (e.getEventID().equals(eventID)) {
                return e;
            }
        }
        return null;
    }
	
	public boolean deleteEvent(String eventID) {
        return data.removeIf(e -> e.getEventID().equals(eventID));
    }

	@Override
    public String getRepositoryType() {
        return "Event Repository";
	}
}
