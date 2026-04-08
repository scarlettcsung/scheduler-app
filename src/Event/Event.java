package Event;

import Invite.Invite;

// Additional Packages
import java.util.Objects;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventName;
    private LocalDateTime eventTime;
    private int eventDuration;
    private String eventDescription;
    private final String eventID = UUID.randomUUID().toString();
    private String organizerUsername;
    private Boolean isImported;
    private List<Invite> invites;

    public Event(String eventName, int eventDuration, String eventDescription,
                 String organizerUsername, Boolean isImported,  List<Invite> invites) {
        this.eventName = eventName;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.organizerUsername = organizerUsername;
        this.isImported = isImported;
        this.invites = Objects.requireNonNullElseGet(invites, () -> new ArrayList<>());
    }

    // Setter Methods
    public void setEventName(String eventName) {this.eventName = eventName;}
    public void setEventTime(LocalDateTime eventTime) {this.eventTime = eventTime;}
    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}
    public void setEventDuration(int eventDuration) {this.eventDuration = eventDuration;}

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
    public void addInvite(Invite invite) {
        invites.add(invite);
    }
    public void removeInvite(Invite invite) {
        invites.removeIf(n -> n.equals(invite));
    }


}
