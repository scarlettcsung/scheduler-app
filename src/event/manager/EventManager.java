package event.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import event.Event;
import invite.Invite;
import invite.Role;
import invite.InviteStatus;
import repository.EventRepository;
import repository.UserRepository;
import user.User;
import user.calendar.UserCalendar;
/**
 * Applies event mutations such as updates, deletion, and invite rejection.
 *
 * @author EO GI
 * @version TODO
 */
public class EventManager {

    private UserRepository repository;
    private EventRepository eventRepository;

    /**
     * Creates an event manager without repository-backed delete behaviour.
     */
    public EventManager() {
        this.repository = null;
        this.eventRepository = null;
    }

    /**
     * Creates an event manager backed by a user repository.
     *
     * @param repository repository used to update user calendars
     */
    public EventManager(UserRepository repository) {
        this.repository = repository;
        this.eventRepository = null;
    }

    /**
     * Creates an event manager backed by user and event repositories.
     *
     * @param repository repository used to update user calendars
     * @param eventRepository repository used to keep a global event index in sync
     */
    public EventManager(UserRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
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
            for (User user : repository.getAll()) {
                if (user.getCalendar() != null) {
                    user.getCalendar().removeEvent(event);
                }
            }

            if (eventRepository != null) {
                eventRepository.deleteItem(event.getEventId());
            }
            return;
        }
    }

    /**
     * Adds invite to event if not already invited.
     *
     * @param event event to add invite to
     * @param recipient user associated with invite
     */
    public void addInvite(Event event, User recipient) {
        if (hasExistingInvite(event, recipient.getUsername())) {
            return;
        }

        Invite invite = new Invite(recipient.getUsername(), event.getEventId());
        event.getInvites().add(invite);

        if (recipient.getCalendar() != null) {
            recipient.getCalendar().addEvent(event);
        }
    }

    /**
     * Removes invite from event.
     *
     * @param event event to remove invite from
     * @param recipient user associated with invite
     */
    public void removeInvite(Event event, User recipient) {
        String username = recipient.getUsername();

        if (!hasExistingInvite(event, username)) {
            return;
        }

        event.getInvites().removeIf(i -> i.getRecipient().equals(username));
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
        invite.setInviteStatus(InviteStatus.REJECTED);
        if (this.repository != null) {
            User invitee = repository.getItemById(invite.getRecipient());

            if (invitee != null) {
                this.removeInvite(event, invitee);
            }
        }
    }

    /**
     * Sets event organizer, if change is necessary.
     *
     * @param event event to update
     * @param organizer user to be set as organizer of event
     */
    public void setOrganizer(Event event, User organizer) {
        event.setOrganizer(organizer.getUsername());

        UserCalendar calendar = new UserCalendar(null);

        if (organizer.getCalendar() == null) {
            organizer.setCalendar(calendar);
        }
        organizer.getCalendar().addEvent(event);
    }

    /**
     * Gets User object of event organizer.
     *
     * @param event event whose organizer should be loaded
     * @return organizer user
     */
    public User getOrganizer(Event event) {
        String organizerUsername = event.getOrganizer();
        return repository.getItemById(organizerUsername);
    }

    /**
     * Checks if an event already has an invite for the given username.
     *
     * @param event event to check
     * @param username username to look for
     * @return {@code true} when an invite for that username exists
     */
    private boolean hasExistingInvite(Event event, String username) {
        for (Invite invite : event.getInvites()) {
            if (invite.getRecipient().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public List<Event> returnParticipatingEvents(String username,EventRepository repo) {
    	List<Event> pEvents= new ArrayList<>();
    	List<Event> allEvents= repo.getAll();
    	for (Event e: allEvents) { 
    		for (Invite i: e.getInvites()){
    			if (username.equals(i.getRecipient())){
    				if(i.getRole().equals(Role.GUEST)) 
    					{pEvents.add(e);}
    			}
    	}
    	}
    	return pEvents;
    	
    }
    
    public List<Event> returnOrganisedEvents(String username,EventRepository repo) {
    	List<Event> oEvents= new ArrayList<>();
    	List<Event> allEvents= repo.getAll();
    	for (Event e: allEvents) { 
    		for (Invite i: e.getInvites()){
    			if (username.equals(i.getRecipient())){
    				if(i.getRole().equals(Role.ORGANIZER)) 
    					{oEvents.add(e);}
    			}
    	}
    	} return oEvents;
    }
    }
