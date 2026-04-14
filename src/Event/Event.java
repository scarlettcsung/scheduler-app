package Event;

import Invite.Invite;
import User.User;
import Repository.UserRepository;
import UserCalendar.UserCalendar;

// Additional Packages
import java.util.Objects;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    	for (Invite invite:invites) {
    		String participantUsername = invite.getRecipient();
    		participantUsernames.add(participantUsername);
    	}
    	return participantUsernames;
    }
    
    
    public void addInvite(Invite invite, UserRepository repository) {
    	for (Invite existingInvite:invites) {
    		if (existingInvite.getRecipient().equals(invite.getRecipient())) {
    			return;
    		}
    	}
    	
        invites.add(invite);
        if (eventTime != null) {
            String username = invite.getRecipient();
            User user = repository.findUsername(username);
            
            if (user != null && user.getCalendar() != null) {
            	user.getCalendar().addEvent(this);
            }
        }
    }
    
    public void removeInvite(Invite invite, UserRepository repository) {
        invites.removeIf(n -> n.getRecipient().equals(invite.getRecipient()));
        if (eventTime != null) {
            String username = invite.getRecipient();
            User user = repository.findUsername(username);
            if (user != null && user.getCalendar() != null) {
            	user.getCalendar().removeEvent(this);
            }
        }
    }
}
