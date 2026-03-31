package Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Invite.Invite;
import User.User;

public class Event {

    private String eventName;
    public Date eventDate;
    private int eventDuration;
    private String eventDescription;
    private User organizer;
    private Boolean isImported;
    private List<Invite> invites;

    public Event(String eventName, Date eventDate, int eventDuration, String eventDescription,
                 User organizer, Boolean isImported,  List<Invite> invites) {
        this.eventName = eventName;
        this.eventDate = eventDate;
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
