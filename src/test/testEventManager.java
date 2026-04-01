package test;

import junit.framework.TestCase;
import java.time.LocalDateTime;

import Event.Event;
import EventManager.EventManager;
import User.User;

import java.time.LocalDateTime;

import Event.Event;
import EventManager.EventManager;
import User.User;

public class testEventManager extends TestCase {

	
	public void testUpdateEvent() {
		User example_organizer = null;
		LocalDateTime example_date = null;
		
		EventManager eveupdateEvent = new EventManager();
		Event event = new Event("osman", example_date,60,"testEvent",
				example_organizer,false,null);
		eveupdateEvent.updateEvent(event, "eventName", "abc");
		assertEquals("abc", event.getEventName());
}
	/*
	public void testDeleteEvent() {
		
		
		
}
*/
	
}

