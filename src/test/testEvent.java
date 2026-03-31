package test;

import Event.Event;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.util.Date;

public class testEvent extends TestCase {

    private UserCalendar calendar;
    private User example_organizer;
    private Event event;
    private Date example_date;

    protected void setUp() {
        calendar = new UserCalendar();
        example_organizer = new User("Charles","123456",calendar,false);
        example_date = new Date(2026,01,01);
        event = new Event("testEvent",example_date,60,"testEvent",
                example_organizer,false,null);
    }

    public void testAvailableSlot() {
        assertEquals(event.getOrganizer(),example_organizer);
    }

}