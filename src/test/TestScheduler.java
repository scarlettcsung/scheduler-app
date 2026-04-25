package test;

import event.CreatedEvent;
import event.Event;
import event.manager.EventManager;
import event.manager.InviteManager;
import invite.InviteStatus;
import invite.Role;
import junit.framework.TestCase;
import repository.EventRepository;
import repository.UserRepository;
import scheduler.Scheduler;
import user.User;
import user.calendar.UserCalendar;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Unit tests for {@link scheduler.Scheduler}.
 *
 * @author SN SS NJ
 * @version 2
 */
public class TestScheduler extends TestCase {

    private Scheduler scheduler;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private User organizer;
    private User invitee;
    private LocalDateTime baseNow;
    private EventManager eventManager;
    private InviteManager inviteManager;
    

    protected void setUp() {
        baseNow = LocalDateTime.now().withSecond(0).withNano(0);
        userRepository = new UserRepository();
        eventRepository = new EventRepository();
        organizer = new User("organizer", "pw", null);
        UserCalendar organizerCalendar = new UserCalendar(null);
        organizer.setCalendar(organizerCalendar);
        userRepository.saveUser(organizer);

        invitee = new User("invitee", "pw", null);
        UserCalendar inviteeCalendar = new UserCalendar(null);
        invitee.setCalendar(inviteeCalendar);
        userRepository.saveUser(invitee);

        scheduler = new Scheduler(0, 23, 7, userRepository, eventRepository);
        
        eventManager = new EventManager(userRepository, eventRepository);
        inviteManager = new InviteManager(userRepository);
    }

    public void testFindAvailableSlotReturnsNullWhenDurationExceedsDayWindow() {
        // Duration longer than the daily window should never be schedulable.
        Scheduler oneHourWindow = new Scheduler(8, 9, 3, userRepository, eventRepository);
        Event tooLong = new CreatedEvent(
                "meeting",
                61,
                "test meeting",
                new ArrayList<>()
        );
        eventManager.setOrganizer(tooLong, organizer);
        LocalDateTime slot = oneHourWindow.findAvailableSlot(tooLong);

        assertNull(slot);
    }

    public void testFindAvailableSlotReturnsFirstFreeSlotAfterBusyEvent() {
        // A busy block at the start should push the slot to the first free minute after it.
        LocalDateTime busyStart = baseNow.plusMinutes(1);
        Event busy = new CreatedEvent(
                "busy",
                60,
                "busy slot",
                new ArrayList<>()
        );
        busy.setEventTime(busyStart);
        organizer.getCalendar().addEvent(busy);
        eventManager.setOrganizer(busy, organizer);

        Event toSchedule = new CreatedEvent(
                "meeting",
                30,
                "test meeting",
                new ArrayList<>()
        );
        eventManager.setOrganizer(toSchedule, organizer);
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);

        assertNotNull(slot);
        assertFalse(slot.isBefore(busyStart.plusMinutes(60)));
    }

    public void testScheduleEventSetsDateAndAddsToAllCalendars() {
        // Successful scheduling should set event time, add to calendars, and keep invites pending.
    	Event event = new CreatedEvent(
                "meeting",
                30,
                "test meeting",
                new ArrayList<>()
        );
    	eventManager.setOrganizer(event, organizer);
        inviteManager.addInvite(event, invitee, Role.GUEST);

        boolean scheduled = scheduler.scheduleEvent(event);

        assertTrue(scheduled);
        assertNotNull(event.getEventTime());
        assertTrue(organizer.getCalendar().getEvents().contains(event));
        assertTrue(invitee.getCalendar().getEvents().contains(event));
        assertEquals(2, event.getInvites().size());
        assertEquals(invitee.getUsername(), event.getInvites().get(1).getRecipient());
        assertEquals(InviteStatus.PENDING, event.getInvites().get(1).getStatus());
        assertNotNull(eventRepository.getItemById(event.getEventId()));
    }

    public void testScheduleEventReturnsFalseWhenNoSlotInLookahead() {
        // If the whole lookahead window is blocked, scheduling must fail.
        Scheduler oneDayLookahead = new Scheduler(8, 10, 1, userRepository,eventRepository);

        LocalDateTime dayStart = baseNow
                .withHour(8)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        Event block = new CreatedEvent(
                "busy",
                120,
                "busy slot",
                new ArrayList<>()
        );
        eventManager.setOrganizer(block, organizer);
        block.setEventTime(dayStart);
        organizer.getCalendar().addEvent(block);

        Event toSchedule = new CreatedEvent(
                "meeting",
                60,
                "test meeting",
                new ArrayList<>()
        );
        eventManager.setOrganizer(toSchedule, organizer);
        boolean scheduled = oneDayLookahead.scheduleEvent(toSchedule);

        assertFalse(scheduled);
        assertFalse(organizer.getCalendar().getEvents().contains(toSchedule));
    }
    
    public void testFindAvailableSlot_ignoresNullTimeEvent_andStillFindsSlot() {
    	//setup null event
        Event nullTimeEvent = new CreatedEvent("ghost", 30, "no time set", new ArrayList<>());
        eventManager.setOrganizer(nullTimeEvent, organizer);
        organizer.getCalendar().addEvent(nullTimeEvent);  // bad data in calendar
        
        //to schedule event
        Event toSchedule = new CreatedEvent("meeting", 30, "test meeting", new ArrayList<>());
        eventManager.setOrganizer(toSchedule, organizer);
        
        //run the find available slot
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);
        assertNotNull(slot);
    }
    
}
