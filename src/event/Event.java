package event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

import invite.Invite;
import invite.Role;

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
    @SerializedName(value = "eventId", alternate = {"eventID"})
    private final String eventId = UUID.randomUUID().toString();
    private List<Invite> invites;

    @SerializedName(value = "isImportedField", alternate = {"isImported"})
    protected boolean isImportedField; // Just for persistence

    /**
     * Creates an event with organizer, duration, description, and invite list.
     *
     * @param eventName display name of the event
     * @param eventDuration duration in minutes
     * @param eventDescription textual event description
     * @param organizerUsername username of the event organizer
     * @param invites invites associated with the event, or {@code null} for an empty list
     */
    public Event(String eventName, int eventDuration, String eventDescription, List<Invite> invites) {
        this.eventName = eventName;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.invites = Objects.requireNonNullElseGet(invites, () -> new ArrayList<>());
    }

    /**
     * Indicates whether this event was imported from an external calendar.
     *
     * @return {@code true} for imported events
     */
    public abstract boolean isImported();

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
    	boolean found = false;
    	for (Invite invite:invites) {
        	if (invite.getRecipient().equals(organizerUsername)) {
        		invite.setOrganizer();
        		invite.accept();
        		found = true;
        	} else if (invite.getRole().equals(Role.ORGANIZER)) {
        		invite.setGuest();
        		}
        }
    	
    	if (!found) {
    		Invite newInvite = new Invite(organizerUsername, this.eventId, Role.ORGANIZER);
    		newInvite.accept();
            this.invites.add(newInvite);
        }
    }

    /**
     * Returns the event name.
     *
     * @return event name
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * Returns the scheduled event start time.
     *
     * @return event start time
     */
    public LocalDateTime getEventTime() {
        return this.eventTime;
    }

    /**
     * Returns the organizer username.
     *
     * @return organizer username
     */
    public String getOrganizer() {
        for (Invite invite:invites) {
        	if (invite.getRole().equals(Role.ORGANIZER)) {
        		return invite.getRecipient();
        	}
        }
        return null;
    }

    /**
     * Returns the event description.
     *
     * @return event description
     */
    public String getEventDescription() {
        return this.eventDescription;
    }

    /**
     * Returns the event duration.
     *
     * @return duration in minutes
     */
    public int getEventDuration() {
        return this.eventDuration;
    }

    /**
     * Returns the generated event identifier.
     *
     * @return event identifier
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Returns the mutable invite list for this event.
     *
     * @return invite list
     */
    public List<Invite> getInvites() {
        return this.invites;
    }

    /**
     * Gets list of participant usernames.
     *
     * @return participant usernames derived from the invite list
     */
    public List<String> getParticipants() {
        List<String> participantUsernames = new ArrayList<>();
        for (Invite invite : this.invites) {
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
        if (this.eventTime == null) {
        	return "Time not set";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String stringDateTime = this.eventTime.format(formatter);
        LocalDateTime endTime = this.eventTime.plusMinutes(this.eventDuration);
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return String.format("%s - %s", stringDateTime, endTimeStr);
    }
        
}
