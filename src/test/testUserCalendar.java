package test;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Event.Event;
import User.User;
import UserCalendar.UserCalendar;

public class testUserCalendar extends TestCase {
    
    private LocalDateTime example_time;
    private Event event;
    private User example_owner;
    private UserCalendar userCalendar; // Declareer de variabele hier
    
    protected void setUp() {
        // Maak eerst de calendar aan of gebruik null als de User constructor dat toestaat
    	example_owner = new User("Charles", "123456", userCalendar, false);
    	example_time = LocalDateTime.of(2026, 1, 1, 11, 0);
    	event = new Event("testEvent", example_time,60,"testEvent",
                example_owner,false,null);
        userCalendar = new UserCalendar(example_owner, null); 
        
    }
    
    public void testOwner() {
        // Gebruik de variabele userCalendar (kleine letter)
        assertEquals(example_owner, userCalendar.getOwner());
    }
    
    public void testGetEvents() {
        List<Event> expected = new ArrayList<>();
        assertEquals(expected,userCalendar.getEvents());
    }
    
    public void testAddEvent() {
		userCalendar.addEvent(event);
        List<Event> example_event = List.of(event);
        assertEquals(example_event, userCalendar.getEvents());
    }
    
    public void testRemoveEvent() {
    	userCalendar.addEvent(event);
		userCalendar.removeEvent(event);
        List<Event> example_event = List.of(event);
        assertNotSame(example_event, userCalendar.getEvents());
    }

}