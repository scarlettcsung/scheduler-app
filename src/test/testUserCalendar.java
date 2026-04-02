package test;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import Event.Event;
import User.User;
import UserCalendar.UserCalendar;

public class testUserCalendar extends TestCase {
    
    private User example_owner;
    private UserCalendar userCalendar; // Declareer de variabele hier
    
    protected void setUp() {
        // Maak eerst de calendar aan of gebruik null als de User constructor dat toestaat
        userCalendar = new UserCalendar(example_owner, null); 
        example_owner = new User("Charles", "123456", userCalendar, false);
    }
    
    public void testOwner() {
        // Gebruik de variabele userCalendar (kleine letter)
        assertEquals(example_owner, userCalendar.getOwner());
    }
    
    public void testGetEvents() {
        List<Event> expected = new ArrayList<>();
        assertEquals(expected,userCalendar.getEvents());
    }
    
    public void testAddEvent(Event Event) {
		userCalendar.addEvent(Event);
        List<Event> example_event = List.of(Event);
        assertEquals(example_event, userCalendar.getEvents());
    }
    
    public void testRemoveEvent(Event Event) {
    	userCalendar.addEvent(Event);
		userCalendar.removeEvent(Event);
        List<Event> example_event = List.of(Event);
        assertNotSame(example_event, userCalendar.getEvents());
    }

}