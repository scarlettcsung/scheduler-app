package UserCalendar;

import Event.Event;
import java.util.ArrayList;
import java.util.List;

public class UserCalendar {
	
	public UserCalendar(List<Event> events) {
		
		if (events == null) {
            this.events = new ArrayList<>();
        } else {
            this.events = events;
        }
	}
	private List<Event> events;
	
	public  List<Event> getEvents() {
        return events;
    }
	
 

    public void addEvent(Event event) {
        events.add(event);
    }
    
    public void removeEvent(Event event) {
        events.removeIf(n -> n == event);
    }
}