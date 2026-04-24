package test;

import junit.framework.TestCase;
import user.User;
import user.calendar.UserCalendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import event.CreatedEvent;
import event.Event;

/**
 * Unit tests for {@link user.UserCalendar}.
 *
 * @author NJ
 * @version 1
 */
public class TestUserCalendar extends TestCase {
    
    private LocalDateTime exampleTime;
    private Event event;
    private String exampleOwner;
    private UserCalendar userCalendar; // Declareer de variabele hier
    
    protected void setUp() {
        // Maak eerst de calendar aan of gebruik null als de User constructor dat toestaat
    	exampleOwner = "Charles";
    	exampleTime = LocalDateTime.of(2026, 1, 1, 11, 0);
    	event = new CreatedEvent("testEvent",60,"testEvent",exampleOwner,null);
        userCalendar = new UserCalendar(null); 
        
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
    
    public void testRemoveEventInCalendar() {
    	userCalendar.addEvent(event);
        userCalendar.removeEvent(event);
        assertTrue(userCalendar.getEvents().isEmpty());
    }
    
    public void testRemoveEventNotInCalendar() {
        Event otherEvent = new CreatedEvent("otherEvent", 30, "otherDesc", exampleOwner, null);
        userCalendar.addEvent(event);
        userCalendar.removeEvent(otherEvent); // otherEvent is not in the calendar
        assertFalse(userCalendar.getEvents().isEmpty()); // original event should still be there
    }
    

    
    //test for loading UserCalendar with Event list already in place
    public void testConstructorWithExistingEvents() {
        List<Event> existingEvents = new ArrayList<>();
        existingEvents.add(event);
        UserCalendar calendarWithEvents = new UserCalendar(existingEvents);
        assertEquals(existingEvents, calendarWithEvents.getEvents());
    }

}
