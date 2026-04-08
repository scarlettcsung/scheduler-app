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
        // Fixed clock keeps scheduling assertions deterministic.
        baseNow = LocalDateTime.of(2026, 4, 1, 8, 0);
        fixedClock = Clock.fixed(
                baseNow.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        scheduler = new Scheduler(0, 23, 7, fixedClock);
        organizer = new User("organizer", "pw", null, false);
        UserCalendar organizerCalendar = new UserCalendar(organizer, null);
        organizer.setCalendar(organizerCalendar);

        invitee = new User("invitee", "pw", null, false);
        UserCalendar inviteeCalendar = new UserCalendar(invitee, null);
        invitee.setCalendar(inviteeCalendar);
    }

    public void testFindAvailableSlotReturnsNullWhenDurationExceedsDayWindow() {
        // Duration longer than the daily window should never be schedulable.
        Scheduler oneHourWindow = new Scheduler(8, 9, 3, fixedClock);
        Event tooLong = new Event("meeting",baseNow,61,"test meeting",organizer,false,new ArrayList<>());

        LocalDateTime slot = oneHourWindow.findAvailableSlot(tooLong);

        assertNull(slot);
    }

    public void testFindAvailableSlotReturnsFirstFreeSlotAfterBusyEvent() {
        // A busy block at the start should push the slot to the first free minute after it.
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

    public void testScheduleEventSetsDateAndAddsToAllCalendars() {
        // Successful scheduling should set event time, add to calendars, and keep invites pending.
        Event event = new Event(
                "meeting",
                baseNow,
                30,
                "test meeting",
                organizer,
                false,
                new ArrayList<>()
        );
        event.addInvite(new Invite(invitee, event));

        boolean scheduled = scheduler.scheduleEvent(event);

        assertTrue(scheduled);
        assertNotNull(event.getEventTime());
        assertTrue(organizer.getCalendar().getEvents().contains(event));
        assertTrue(invitee.getCalendar().getEvents().contains(event));
        assertEquals(1, event.getInvites().size());
        assertEquals(invitee, event.getInvites().get(0).getRecipient());
        assertEquals(inviteStatus.PENDING, event.getInvites().get(0).getStatus());
    }

    public void testScheduleEventReturnsFalseWhenNoSlotInLookahead() {
        // If the whole lookahead window is blocked, scheduling must fail.
        Scheduler oneDayLookahead = new Scheduler(8, 10, 1, fixedClock);

        LocalDateTime dayStart = baseNow
                .withHour(8)
                .withMinute(0);
        Event block = new Event(
                "busy",
                dayStart,
                120,
                "busy slot",
                organizer,
                false,
                new ArrayList<>()
        );
        organizer.getCalendar().addEvent(block);

        Event toSchedule = new Event(
                "meeting",
                baseNow,
                60,
                "test meeting",
                organizer,
                false,
                new ArrayList<>()
        );
        boolean scheduled = oneDayLookahead.scheduleEvent(toSchedule);

        assertFalse(scheduled);
        assertFalse(organizer.getCalendar().getEvents().contains(toSchedule));
    }
}
