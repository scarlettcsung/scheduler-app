package event;

import Invite.Invite;

// Additional Packages
import java.util.Objects;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> participantUsernames;
    
    protected boolean isImportedField; // Just for IO

    public Event(String eventName, int eventDuration, String eventDescription,
                 String organizerUsername,  List<Invite> invites) {
        this.eventName = eventName;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.organizerUsername = organizerUsername;
        this.invites = Objects.requireNonNullElseGet(invites, () -> new ArrayList<>());
        this.participantUsernames = new ArrayList<>();
    }
    
    public abstract boolean isImported(); 

    // Setter Methods
    public void setEventName(String eventName) {this.eventName = eventName;}
    public void setEventTime(LocalDateTime eventTime) {this.eventTime = eventTime;}
    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}
    public void setEventDuration(int eventDuration) {this.eventDuration = eventDuration;}
    public void setOrganizer(String organizerUsername) {this.organizerUsername = organizerUsername;}

    // Getter Methods
    public String getEventName() {return eventName;}
    public LocalDateTime getEventTime() {return eventTime;}
    public String getOrganizer() {return organizerUsername;}
    public String getEventDescription() {return eventDescription; }
    public int getEventDuration() {return eventDuration; }
    public String getEventID() {return eventID; }

    // Invites Methods
    
    public  List<Invite> getInvites() {
        return invites;
    }
    
    /**
     * Gets list of participant usernames
     */
    public List<String> getParticipants() {
    	participantUsernames.clear();
    	
    	for (Invite invite:invites) {
    		String participantUsername = invite.getRecipient();
    		participantUsernames.add(participantUsername);
    	}
    	return participantUsernames;
    }
    
    /**
     * Checks if event already has invite
     *
     * @param username string of the username of invited user
     */
    public boolean hasExistingInvite(String username) {
    	for (Invite existingInvite: invites) {
            if (existingInvite.getRecipient().equals(username)) {
                return true;
            }
    	}
    	return false;
    }

    
}
