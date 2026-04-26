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
        organizer = new User("organizer", "pw");
        userRepository.saveUser(organizer);

        invitee = new User("invitee", "pw");
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
        tooLong.setOrganizer(organizer.getUsername());
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
        busy.setOrganizer(organizer.getUsername());
        eventRepository.save(busy);

        Event toSchedule = new CreatedEvent(
                "meeting",
                30,
                "test meeting",
                new ArrayList<>()
        );
        toSchedule.setOrganizer(organizer.getUsername());
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
    	event.setOrganizer(organizer.getUsername());
        inviteManager.addInvite(event, invitee, Role.GUEST);

        boolean scheduled = scheduler.scheduleEvent(event);

        assertTrue(scheduled);
        assertNotNull(event.getEventTime());
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
        block.setOrganizer(organizer.getUsername());
        block.setEventTime(dayStart);
        eventRepository.save(block);

        Event toSchedule = new CreatedEvent(
                "meeting",
                60,
                "test meeting",
                new ArrayList<>()
        );
        toSchedule.setOrganizer(organizer.getUsername());
        boolean scheduled = oneDayLookahead.scheduleEvent(toSchedule);

        assertFalse(scheduled);
    }
    
    public void testFindAvailableSlot_ignoresNullTimeEvent_andStillFindsSlot() {
    	//setup null event
        Event nullTimeEvent = new CreatedEvent("ghost", 30, "no time set", new ArrayList<>());
        nullTimeEvent.setOrganizer(organizer.getUsername());
        
        //to schedule event
        Event toSchedule = new CreatedEvent("meeting", 30, "test meeting", new ArrayList<>());
        toSchedule.setOrganizer(organizer.getUsername());
        
        //run the find available slot
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);
        assertNotNull(slot);
    }
    
    public void testFindAvailableSlot_FiltersInvalidOrganizerEvents() {
        Event toSchedule = new CreatedEvent("meeting", 30, "test meeting", new ArrayList<>());
        toSchedule.setOrganizer(organizer.getUsername());
        eventRepository.save(toSchedule); 

        Event unscheduled = new CreatedEvent("unscheduled", 30, "none", new ArrayList<>());
        unscheduled.setOrganizer(organizer.getUsername());
        eventRepository.save(unscheduled);

        eventRepository.getUserCalendar(organizer.getUsername()).add(null);

        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);
        assertNotNull(slot);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getHour(), slot.getHour());
    }
    
    public void testFindAvailableSlot_IgnoresInvalidInviteeEvents() {
        Event ghostEvent = new CreatedEvent("ghost", 60, "unscheduled", new ArrayList<>());
        ghostEvent.setOrganizer(invitee.getUsername());
        ghostEvent.setEventTime(null);
        eventRepository.save(ghostEvent);

        eventRepository.getUserCalendar(invitee.getUsername()).add(null);

        Event toSchedule = new CreatedEvent("meeting", 30, "desc", new ArrayList<>());
        toSchedule.setOrganizer(organizer.getUsername());
        inviteManager.addInvite(toSchedule, invitee, Role.GUEST);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);

        assertNotNull(slot);
        
        int expectedHour = Math.max(now.getHour(), 0);
        assertEquals(expectedHour, slot.getHour());
    }
    
    public void testFindAvailableSlotSameDay() {
        LocalDateTime todayBusy = baseNow.withHour(8).withMinute(0);
        Event busyOrg = new CreatedEvent("busy organizer", 60, "test", new ArrayList<>());
        busyOrg.setEventTime(todayBusy);
        busyOrg.setOrganizer(organizer.getUsername());
        eventRepository.save(busyOrg);

        LocalDateTime todayBusyInv = baseNow.withHour(10).withMinute(0);
        Event busyInv = new CreatedEvent("inviteebusy", 60, "test", new ArrayList<>());
        busyInv.setEventTime(todayBusyInv);
        busyInv.setOrganizer(invitee.getUsername());
        eventRepository.save(busyInv);

        Event toSchedule = new CreatedEvent("meeting", 30, "desc", new ArrayList<>());
        toSchedule.setOrganizer(organizer.getUsername());
        inviteManager.addInvite(toSchedule, invitee, Role.GUEST);

        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);

        assertNotNull(slot);
        assertTrue(slot.getHour() >= 11);
    }
    
    public void testFindAvailableSlot_ReturnsFromInsideLoop() {
    	
    	LocalDateTime now = LocalDateTime.now();
    	
    	// Block
        Event earlyBusy = new CreatedEvent("early", 30, "busy", new ArrayList<>());
        earlyBusy.setEventTime(now.plusMinutes(5));
        earlyBusy.setOrganizer(organizer.getUsername());
        eventRepository.save(earlyBusy);

        // Gap
        Event laterBusy = new CreatedEvent("later", 60, "busy", new ArrayList<>());
        laterBusy.setEventTime(now.plusMinutes(120));
        laterBusy.setOrganizer(organizer.getUsername());
        eventRepository.save(laterBusy);

        // Schedule
        Event toSchedule = new CreatedEvent("meeting", 60, "desc", new ArrayList<>());
        toSchedule.setOrganizer(organizer.getUsername());

        // 4. Execution
        // IMPORTANT: Ensure 'now' is early enough (e.g., midnight) or use a fixed clock 
        // so the scheduler actually looks at the 02:00 gap.
        LocalDateTime slot = scheduler.findAvailableSlot(toSchedule);

        // 5. Verification
        assertNotNull(slot);
        
        LocalDateTime expectedSlot = now.plusMinutes(35);
        assertEquals(expectedSlot.getHour(), slot.getHour());
        assertEquals(expectedSlot.getMinute(), slot.getMinute());
    }
}
