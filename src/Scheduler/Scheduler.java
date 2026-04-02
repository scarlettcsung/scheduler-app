package Scheduler;

import Event.Event;
import Invite.Invite;
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

    public Scheduler(int dayStart, int dayEnd, int maxLookaheadDays) {
        this(dayStart, dayEnd, maxLookaheadDays, Clock.systemDefaultZone());
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

        LocalDateTime now = LocalDateTime.now(clock).withSecond(0);
        LocalDateTime dayCursor = now;

        for (int i = 0; i < maxLookaheadDays; i++) {
            LocalDateTime dayStartTime = dayCursor.with(LocalTime.of(dayStart, 0));
            LocalDateTime dayEndTime = dayCursor.with(LocalTime.of(dayEnd, 0));

            LocalDateTime candidate = dayStartTime;
            if (dayCursor.toLocalDate().equals(now.toLocalDate()) && now.isAfter(candidate)) {
                candidate = now;
            }

            List<Event> busyEvents = new ArrayList<>();

            User organizer = event.getOrganizer();
            if (organizer != null && organizer.getCalendar() != null) {
                for (Event e : organizer.getCalendar().getEvents()) {
                    if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) busyEvents.add(e);
                }
            }

            for (Invite invite : event.getInvites()) {
                User invitee = invite.getRecipient();
                if (invitee != null && invitee.getCalendar() != null) {
                    for (Event e : invitee.getCalendar().getEvents()) {
                        if (e.getEventTime().toLocalDate().equals(dayCursor.toLocalDate())) busyEvents.add(e);
                    }
                }
            }

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
}
