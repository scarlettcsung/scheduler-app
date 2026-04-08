package test;

import junit.framework.TestCase;

import java.time.LocalDateTime;

import Event.Event;
import EventManager.EventManager;
import User.User;
import UserCalendar.UserCalendar;

import java.time.LocalDateTime;

import Event.Event;
import EventManager.EventManager;
import User.User;

public class testEventManager extends TestCase {

	UserCalendar orgCalendar = new UserCalendar(null, null);
	String exampleOrganizer = "testUser" ; // creating a dummy user for testing
	LocalDateTime example_date = LocalDateTime.of(2026, 4, 1, 12, 0);; // creating a dummy date for testing
	EventManager eveupdateEvent = new EventManager();
	Event event = new Event("osman",60,"testEvent",exampleOrganizer,false,null);
	
	public void testUpdateEventShouldChangeEventName() {
		
		eveupdateEvent.updateEvent(event, "eventName", "abc");
		assertEquals("abc", event.getEventName()); 
}
	
	public void testDeleteEvent() {
		
		assertNotNull(event.getEventName());
	    assertNotNull(event.getEventDescription());
	    
		eveupdateEvent.deleteEvent(event);
		
		assertEquals(null,event.getEventName());
		assertEquals(null,event.getEventDescription());
		assertEquals(0,event.getEventDuration());
}

	
}


