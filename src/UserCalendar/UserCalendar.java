package UserCalendar;

import Event.Event;
import User.User;

import java.util.ArrayList;
import java.util.List;

public class UserCalendar {

    private User owner;
    private List<Event> events;

    public UserCalendar() {
        this.events = new ArrayList<>();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public List<Event> getEvents() {
        return events;
    }

}
