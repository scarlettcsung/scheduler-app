package UserCalendar;

import Event.Event;
import java.util.ArrayList;
import java.util.List;

public class UserCalendar {
	
	public UserCalendar(String ownerUsername, List<Event> events) {
		this.ownerUsername = ownerUsername;
		if (events == null) {
            this.events = new ArrayList<>();
        } else {
            this.events = events;
        }
	}
	private String ownerUsername;
	private List<Event> events;
	
	public  List<Event> getEvents() {
        return events;
    }
	
    public String getOwner() {
    	return ownerUsername;
    }

    public void SetOwner(String newOwnerUsername) {
        this.ownerUsername = newOwnerUsername;
    }

    public void addEvent(Event event) {
        events.add(event);
    }
    
    public void removeEvent(Event event) {
        events.removeIf(n -> n == event);
    }
}