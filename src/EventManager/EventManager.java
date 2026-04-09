package EventManager;

import java.time.LocalDateTime;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import Repository.UserRepository;
import User.User;


public class EventManager {

    private UserRepository repository;

    public EventManager() {
        this.repository = null;
    }

    public EventManager(UserRepository repository) {
        this.repository = repository;
    }

    public void updateEvent(Event event, String updateAspect, String newValue) {
    	
    	// You should type enter eventName for example as a parameter to updateAspect part.
    	
        switch (updateAspect) {
            case "eventName":
                event.setEventName(newValue);
                break;
            case "eventDescription":
                event.setEventDescription(newValue);
                break;
            case "eventTime":
            	LocalDateTime date = LocalDateTime.parse(newValue);
            	event.setEventTime(date);
                break;
            case "eventDuration":
                event.setEventDuration(Integer.parseInt(newValue));
                break;
        }
    }

    public void deleteEvent(Event event) {
        if (event == null) {
            return;
        }

        if (repository != null) {
            String organizerUsername = event.getOrganizer();
            User organizer = organizerUsername != null ? repository.findUsername(organizerUsername) : null;

            if (organizer != null && organizer.getCalendar() != null) {
                organizer.getCalendar().removeEvent(event);
            }

            if (event.getInvites() != null) {
                for (Invite invite : event.getInvites()) {
                    String recipientUsername = invite.getRecipient();
                    User invitee = recipientUsername != null ? repository.findUsername(recipientUsername) : null;
                    if (invitee != null && invitee.getCalendar() != null) {
                        invitee.getCalendar().removeEvent(event);
                    }
                }
            }
            return;
        }
    }

    public void rejectInvite(Invite invite, Event event) {
        invite.setInviteStatus(inviteStatus.REJECTED);
        event.removeInvite(invite);
    }

}
