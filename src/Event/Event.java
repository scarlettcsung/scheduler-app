package Event;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import Invite.Invite;
import User.User;

public class Event {

    private String eventName;
    public LocalDateTime eventTime;
    private int eventDuration;
    private String eventDescription;
    private User organizer;
    private Boolean isImported;
    private List<Invite> invites;

    public Event(String eventName, LocalDateTime eventTime, int eventDuration, String eventDescription,
                 User organizer, Boolean isImported,  List<Invite> invites) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventDuration = eventDuration;
        this.eventDescription = eventDescription;
        this.organizer = organizer;
        this.isImported = isImported;
        if (invites == null) {
            this.invites = new ArrayList<>();
        } else {
            this.invites = invites;
        }
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public void setEventDuration(int eventDuration) {
        this.eventDuration = eventDuration;
    }


    public String getEventName() {return eventName;}
    public LocalDateTime getEventTime() {return eventTime;}
    public User getOrganizer() {
        return organizer;
    }
    public  List<Invite> getInvites() {
        return invites;
    }
    public void addInvite(Invite invite) {
        invites.add(invite);
    }
    public void removeInvite(Invite invite) {
        invites.removeIf(n -> n == invite);
    }

}
