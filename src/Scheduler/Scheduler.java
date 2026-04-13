package Scheduler;

import Event.Event;
import Invite.Invite;
import Repository.UserRepository;
import User.User;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scheduler {
    private int dayStart;
    private int dayEnd;
    private int maxLookaheadDays;
    private Clock clock;
    private UserRepository userRepository;

    public Scheduler(int dayStart, int dayEnd, int maxLookaheadDays, UserRepository userRepository) {
        this(dayStart, dayEnd, maxLookaheadDays, Clock.systemDefaultZone());
        this.userRepository = userRepository;
    }

    public Scheduler(int dayStart, int dayEnd, int maxLookaheadDays, Clock clock) {
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.maxLookaheadDays = maxLookaheadDays;
        this.clock = clock;
    }

    public LocalDateTime findAvailableSlot(Event event) {
        int duration = event.getEventDuration();
        int minutesInDay = (dayEnd - dayStart) * 60;
        if (duration > minutesInDay) return null;

        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime dayCursor = now;

        // Check each day in the search window and return the first fitting gap.
        for (int i = 0; i < maxLookaheadDays; i++) {
            LocalDateTime dayStartTime = dayCursor.with(LocalTime.of(dayStart, 0));
            LocalDateTime dayEndTime = dayCursor.with(LocalTime.of(dayEnd, 0));

            LocalDateTime candidate = dayStartTime;
            if (dayCursor.toLocalDate().equals(now.toLocalDate()) && now.isAfter(candidate)) {
                candidate = now;
            }

            // Merge busy entries from organizer and invitees for this specific day.
            List<Event> busyEvents = new ArrayList<>();

            String organizerUsername = event.getOrganizer();
            User organizer = userRepository.findUsername(organizerUsername);
            if (organizer != null && organizer.getCalendar() != null) {
                for (Event e : organizer.getCalendar().getEvents()) {
                    if (e == null || e == event || e.getEventTime() == null) {
                        continue;
                    }
                    if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) busyEvents.add(e);
                }
            }


            for (Invite invite : event.getInvites()) {
                String inviteeUsername = invite.getRecipient();
                User invitee = userRepository.findUsername(inviteeUsername);
            if (invitee != null && invitee.getCalendar() != null) {
                for (Event e : invitee.getCalendar().getEvents()) {
                    if (e == null || e == event || e.getEventTime() == null) {
                        continue;
                    }
                    if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) busyEvents.add(e);
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

    public boolean scheduleEvent(Event event) {
        LocalDateTime slot = findAvailableSlot(event);
        if (slot == null) return false;

        event.setEventTime(slot);
        String organizerUsername = event.getOrganizer();
        User organizer = userRepository.findUsername(organizerUsername);
        if (organizer != null && organizer.getCalendar() != null) {
            organizer.getCalendar().addEvent(event);
        }

        List<Invite> currentInvites = new ArrayList<>(event.getInvites());
        for (Invite invite : currentInvites) {
            String inviteeUsername = invite.getRecipient();
            User invitee = userRepository.findUsername(inviteeUsername);
            if (invitee != null && invitee.getCalendar() != null) {
                invitee.getCalendar().addEvent(event);
            }

            // Recreate invites so they reset to default PENDING status.
            event.removeInvite(invite, userRepository);
            if (invitee != null) {
                event.addInvite(new Invite(inviteeUsername, event.getEventID()), userRepository);
            }
        }
        return true;
    }
}
