package test;

import Event.Event;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class testEvent extends TestCase {

    private User example_organizer;
    private Event event;
    private Date example_date;

    protected void setUp() {
        UserCalendar calendar = new UserCalendar();
        example_organizer = new User("Charles","123456", calendar,false);
        example_date = new Date(126, 0, 01);
        event = new Event("testEvent", example_date,60,"testEvent",
                example_organizer,false,null);
    }

    public void testEventName() {
        assertEquals("testEvent", event.getEventName());
        assertNotSame("wrongEvent",event.getEventName()); //Test QC
    }

    public void testEventDate() {
        assertEquals(example_date,event.getEventDate());
    }

    public void testOrganizer() {
        assertEquals(example_organizer,event.getOrganizer());
    }

    public void testInvites() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected,event.getInvites());
    }

    //Invite to be completed
}