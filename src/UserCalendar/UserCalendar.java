package UserCalendar;

import Event.Event;
import User.User;
import java.util.ArrayList;
import java.util.List;

public class UserCalendar {
	
	public UserCalendar(User owner, List<Event> events) {
		this.owner = owner;
		if (events == null) {
            this.events = new ArrayList<>();
        } else {
            this.events = events;
        }
	}
	private User owner;
	private List<Event> events;
	
	public  List<Event> getEvents() {
        return events;
    }
	
    public User getOwner() {
    	return owner;
    }

    public void SetOwner(User newOwner) {
        this.owner = newOwner;
    }
    
    
    public void addEvent(Event event) {
        events.add(event);
    }
    
    public void removeEvent(Event event) {
        events.removeIf(n -> n == event);
    }
}