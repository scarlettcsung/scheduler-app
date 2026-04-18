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

public class Event {

    private String eventName;
    private int eventDuration;
    private LocalDateTime eventTime;
    private String eventDescription;
    private final String eventID = UUID.randomUUID().toString();
    private String organizerUsername;
    private Boolean isImported;
    private List<Invite> invites;
    private List<String> participantUsernames;

    public Event(String eventName, int eventDuration, String eventDescription,
                 String organizerUsername, Boolean isImported,  List<Invite> invites) {
        this.eventName = eventName;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.organizerUsername = organizerUsername;
        this.isImported = isImported;
        this.invites = Objects.requireNonNullElseGet(invites, () -> new ArrayList<>());
        this.participantUsernames = new ArrayList<>();
    }

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
    public Boolean getIsImported() {return isImported;}

    // Invites Methods
    
    public  List<Invite> getInvites() {
        return invites;
    }
    
    public List<String> getParticipants() {
    	participantUsernames.clear();
    	
    	for (Invite invite:invites) {
    		String participantUsername = invite.getRecipient();
    		participantUsernames.add(participantUsername);
    	}
    	return participantUsernames;
    }
    
}
