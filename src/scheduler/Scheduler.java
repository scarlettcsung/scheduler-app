package scheduler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import event.Event;
import event.manager.EventManager;
import invite.Invite;
import repository.EventRepository;
import repository.UserRepository;
import user.User;

/**
 * Finds available time slots for events and places scheduled events into the
 * relevant user calendars.
 *
 * @author SN SS
 * @version 2
 */
public class Scheduler {
    private int dayStart;
    private int dayEnd;
    private int maxLookaheadDays;
    private Clock clock;
    private UserRepository userRepository;
    private EventManager eventManager;
    private EventRepository eventRepository;

    /**
     * Creates a scheduler that uses the system clock and a user repository to
     * resolve organizer and invitee calendars.
     *
     * @param dayStart earliest scheduling hour, inclusive
     * @param dayEnd latest scheduling hour, exclusive
     * @param maxLookaheadDays number of days to search for a free slot
     * @param userRepository repository used to inspect and update calendars
     * @param eventRepository repository used to persist scheduled events
     */
    public Scheduler(int dayStart, int dayEnd, int maxLookaheadDays, UserRepository userRepository,
            EventRepository eventRepository) {
        this(dayStart, dayEnd, maxLookaheadDays, Clock.systemDefaultZone());
        this.userRepository = userRepository;
        this.eventManager = new EventManager(userRepository, eventRepository);
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a scheduler with an explicit clock, typically for tests.
     *
     * @param dayStart earliest scheduling hour, inclusive
     * @param dayEnd latest scheduling hour, exclusive
     * @param maxLookaheadDays number of days to search for a free slot
     * @param clock clock used to determine the current time
     */
    public Scheduler(int dayStart, int dayEnd, int maxLookaheadDays, Clock clock) {
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.maxLookaheadDays = maxLookaheadDays;
        this.clock = clock;
    }

    /**
     * Finds the earliest available slot for an event that fits the organizer
     * and invitee calendars within the configured scheduling window.
     *
     * @param event event to schedule
     * @return first available start time, or {@code null} when none is found
     */
    public LocalDateTime findAvailableSlot(Event event) {
        int duration = event.getEventDuration();
        int minutesInDay = (this.dayEnd - this.dayStart) * 60;
        if (duration > minutesInDay) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime dayCursor = now;

        // Check each day in the search window and return the first fitting gap.
        for (int i = 0; i < this.maxLookaheadDays; i++) {
            LocalDateTime dayStartTime = dayCursor.with(LocalTime.of(this.dayStart, 0));
            LocalDateTime dayEndTime = dayCursor.with(LocalTime.of(this.dayEnd, 0));

            LocalDateTime candidate = dayStartTime;
            if (dayCursor.toLocalDate().equals(now.toLocalDate()) && now.isAfter(candidate)) {
                candidate = now;
            }

            // Merge busy entries from organizer and invitees for this specific day.
            List<Event> busyEvents = new ArrayList<>();

            String organizerUsername = event.getOrganizer();
            User organizer = this.userRepository.getItemById(organizerUsername);
            if (organizer != null && organizer.getCalendar() != null) {
                for (Event e : organizer.getCalendar().getEvents()) {
                    if (e == null || e == event || e.getEventTime() == null) {
                        continue;
                    }
                    if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) {
                        busyEvents.add(e);
                    }
                }
            }

            for (Invite invite : event.getInvites()) {
                String inviteeUsername = invite.getRecipient();
                User invitee = this.userRepository.getItemById(inviteeUsername);
                if (invitee != null && invitee.getCalendar() != null) {
                    for (Event e : invitee.getCalendar().getEvents()) {
                        if (e == null || e == event || e.getEventTime() == null) {
                            continue;
                        }
                        if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) {
                            busyEvents.add(e);
                        }
                    }
                }
            }

            // Scan conflicts in chronological order to find the earliest free slot.
            busyEvents.sort(Comparator.comparing(Event::getEventTime));

            for (Event busy : busyEvents) {
                LocalDateTime busyStart = busy.getEventTime();
                LocalDateTime busyEnd = busyStart.plusMinutes(busy.getEventDuration());
                LocalDateTime candidateEnd = candidate.plusMinutes(duration);

                if (!candidateEnd.isAfter(busyStart) && !candidateEnd.isAfter(dayEndTime)) {
                    return candidate;
                }

                if (busyEnd.isAfter(candidate)) {
                    candidate = busyEnd;
                }
            }

            if (!candidate.plusMinutes(duration).isAfter(dayEndTime)) {
                return candidate;
            }

            dayCursor = dayCursor.plusDays(1);
        }

        return null;
    }

    /**
     * Schedules an event at the first available slot and updates the organizer
     * and invitee calendars accordingly.
     *
     * @param event event to schedule
     * @return {@code true} when a slot was found and applied
     */
    public boolean scheduleEvent(Event event) {
        LocalDateTime slot = findAvailableSlot(event);
        if (slot == null) {
            return false;
        }

        event.setEventTime(slot);
        String organizerUsername = event.getOrganizer();
        User organizer = this.userRepository.getItemById(organizerUsername);
        if (organizer != null && organizer.getCalendar() != null) {
            organizer.getCalendar().addEvent(event);
            if (this.eventRepository.getItemById(event.getEventId()) == null) {
                this.eventRepository.save(event);
            }
        }

        List<Invite> currentInvites = new ArrayList<>(event.getInvites());
        for (Invite invite : currentInvites) {
            String inviteeUsername = invite.getRecipient();
            User invitee = this.userRepository.getItemById(inviteeUsername);
            if (invitee != null && invitee.getCalendar() != null) {
                invitee.getCalendar().addEvent(event);
                if (this.eventRepository.getItemById(event.getEventId()) == null) {
                    this.eventRepository.save(event);
                }
            }

            // Recreate invites so they reset to default PENDING status.
            if (invitee != null) {
                this.eventManager.removeInvite(event, invitee);
                this.eventManager.addInvite(event, invitee);
            }
        }
        return true;
    }
}
