package event;

// Additional Packages
import java.util.Objects;
import java.util.UUID;

import invite.Invite;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Represents a schedulable event together with its organizer and invitees.
 *
 * @author NS SS
 * @version 1
 */

public abstract class Event {

    private String eventName;
    private int eventDuration;
    private LocalDateTime eventTime;
    private String eventDescription;
    private final String eventID = UUID.randomUUID().toString();
    private String organizerUsername;
    private List<Invite> invites;
 

    protected boolean isImportedField; // Just for IO

    /**
     * Creates an event with organizer, duration, description, and invite list.
     *
     * @param eventName display name of the event
     * @param eventDuration duration in minutes
     * @param eventDescription textual event description
     * @param organizerUsername username of the event organizer
     * @param invites invites associated with the event, or {@code null} for an empty list
     */
    public Event(String eventName, int eventDuration, String eventDescription,
            String organizerUsername, List<Invite> invites) {
        this.eventName = eventName;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.organizerUsername = organizerUsername;
        this.invites = Objects.requireNonNullElseGet(invites, () -> new ArrayList<>());
      
    }

    /**
     * Indicates whether this event was imported from an external calendar.
     *
     * @return {@code true} for imported events
     */
    public abstract boolean isImported();

    // Setter Methods
    /**
     * Updates the event name.
     *
     * @param eventName new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Updates the scheduled event start time.
     *
     * @param eventTime new event start time
     */
    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Updates the event description.
     *
     * @param eventDescription new event description
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Updates the event duration.
     *
     * @param eventDuration new duration in minutes
     */
    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }

    /**
     * Updates the event organizer.
     *
     * @param organizerUsername username of the new organizer
     */
    public void setOrganizer(String organizerUsername) {
        this.organizerUsername = organizerUsername;
    }

    // Getter Methods
    /**
     * Returns the event name.
     *
     * @return event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Returns the scheduled event start time.
     *
     * @return event start time
     */
    public LocalDateTime getEventTime() {
        return eventTime;
    }

    /**
     * Returns the organizer username.
     *
     * @return organizer username
     */
    public String getOrganizer() {
        return organizerUsername;
    }

    /**
     * Returns the event description.
     *
     * @return event description
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Returns the event duration.
     *
     * @return duration in minutes
     */
    public int getEventDuration() {
        return eventDuration;
    }

    /**
     * Returns the generated event identifier.
     *
     * @return event identifier
     */
    public String getEventID() {
        return eventID;
    }

    // Invites Methods

    /**
     * Returns the mutable invite list for this event.
     *
     * @return invite list
     */
    public List<Invite> getInvites() {
        return invites;
    }

    /**
     * Gets list of participant usernames.
     *
     * @return participant usernames derived from the invite list
     */
    public List<String> getParticipants() {
    	List<String> participantUsernames = new ArrayList<>();
    	participantUsernames.clear();
        for (Invite invite : invites) {
            String participantUsername = invite.getRecipient();
            participantUsernames.add(participantUsername);
        }
        return participantUsernames;
    }

    /**
     * Formats the event start and end time for display.
     *
     * @return formatted time range
     */
    public String getTimeString() {
    	
  		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
  		String stringDateTime = eventTime.format(formatter);
  		LocalDateTime endTime = eventTime.plusMinutes(eventDuration);
  		String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
  		String stringDisplay = String.format("%s - %s", stringDateTime, endTimeStr);

  		return stringDisplay;
    }


}
