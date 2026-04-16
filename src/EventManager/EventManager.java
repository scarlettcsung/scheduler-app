package EventManager;

import java.time.LocalDateTime;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;
import Repository.UserRepository;
import User.User;
import UserCalendar.UserCalendar;


/**
 * Applies event mutations such as updates, deletion, and invite rejection.
 *
 * @author EO GI
 * @version TODO
 */
public class EventManager {

    private UserRepository repository;

    /**
     * Creates an event manager without repository-backed delete behaviour.
     */
    public EventManager() {
        this.repository = null;
    }

    /**
     * Creates an event manager backed by a user repository.
     *
     * @param repository repository used to update user calendars
     */
    public EventManager(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Updates a single property on an event.
     *
     * @param event event to mutate
     * @param updateAspect property name to update; supported values are
     *        {@code eventName}, {@code eventDescription}, {@code eventTime},
     *        and {@code eventDuration}
     * @param newValue new value represented as text
     */

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

    /**
     * Removes an event from the organizer and invitee calendars when the
     * manager was constructed with a repository.
     *
     * @param event event to delete
     */
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

    public void addInvite(Event event, User recipient) {
        Invite invite = new Invite(recipient.getUsername(),event.getEventID());
        for (Invite existingInvite:event.getInvites()) {
            if (existingInvite.getRecipient().equals(invite.getRecipient())) {
                return;
            }
        }
        event.addInvite(invite);
        if (recipient.getCalendar() != null) {
            recipient.getCalendar().addEvent(event);
        }
    }

    public void removeInvite(Event event, User recipient) {
        event.removeInvite(recipient.getUsername());
        if (recipient.getCalendar() != null) {
            recipient.getCalendar().removeEvent(event);
        }
    }


    /**
     * Marks an invite as rejected and removes the event from the invitee's
     * calendar.
     *
     * @param invite invite to reject
     * @param event event associated with the invite
     */
    public void rejectInvite(Invite invite, Event event) {
        invite.setInviteStatus(inviteStatus.REJECTED);
        event.removeInvite(invite.getRecipient());
    }

    public void setOrganizer(Event event, User organizer) {
        event.setOrganizer(organizer.getUsername());

        if (organizer.getCalendar() == null) {
            organizer.setCalendar(new UserCalendar(organizer.getUsername(), null));
        }
        organizer.getCalendar().addEvent(event);
    }

    public User getOrganizer(Event event) {
        String organizerUsername = event.getOrganizer();
        return repository.findUsername(organizerUsername);
    }
}
