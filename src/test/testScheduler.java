package test;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import Scheduler.Scheduler;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class testScheduler extends TestCase {

    private Scheduler scheduler;
    private User organizer;
    private User invitee;
    private LocalDateTime baseNow;
    private Clock fixedClock;

    protected void setUp() {
        baseNow = LocalDateTime.of(2026, 4, 1, 8, 0);
        fixedClock = Clock.fixed(
                baseNow.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        scheduler = new Scheduler(0, 23, 7, fixedClock);
        UserCalendar organizerCalendar = new UserCalendar();
        organizer = new User("organizer", "pw", organizerCalendar, false);
        organizerCalendar.setOwner(organizer);

        UserCalendar inviteeCalendar = new UserCalendar();
        invitee = new User("invitee", "pw", inviteeCalendar, false);
        inviteeCalendar.setOwner(invitee);
    }

    public void testFindAvailableSlotReturnsNullWhenDurationExceedsDayWindow() {
        Scheduler oneHourWindow = new Scheduler(8, 9, 3, fixedClock);
        Event tooLong = new Event(
                "meeting",
                baseNow,
                61,
                "test meeting",
                organizer,
                false,
                new ArrayList<>()
        );

        LocalDateTime slot = oneHourWindow.findAvailableSlot(tooLong);

        assertNull(slot);
    }

    public void testFindAvailableSlotReturnsFirstFreeSlotAfterBusyEvent() {
        LocalDateTime busyStart = baseNow.plusMinutes(1);
        Event busy = new Event(
                "busy",
                busyStart,
                60,
                "busy slot",
                organizer,
                false,
                new ArrayList<>()
        );
        organizer.getCalendar().addEvent(busy);

        Event toSchedule = new Event(
                "meeting",
                baseNow,
                30,
                "test meeting",
                organizer,
                false,
                new ArrayList<>()
        );
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);

        assertNotNull(slot);
        assertFalse(slot.isBefore(busyStart.plusMinutes(60)));
    }
}
