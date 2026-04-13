package Repository;

import Event.Event;

public class EventRepository extends Repository<Event> {
	
	@Override
    public String getRepositoryType() {
        return "Event Repository";
	}
}
