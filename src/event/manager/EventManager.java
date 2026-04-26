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
import ics.importer.IcsImporter;
import ics.importer.ImportStatus;
/**
 * Applies event mutations such as updates, deletion, and invite rejection.
 *
 * @author EO GI
 * @version TODO
 */
public class EventManager {

    private UserRepository repository;
    private EventRepository eventRepository;
    private InviteManager inviteManager;

    /**
     * Creates an event manager without repository-backed delete behaviour.
     */
    public EventManager() {
        this.repository = null;
        this.eventRepository = null;
        this.inviteManager = new InviteManager(null);
    }

    /**
     * Creates an event manager backed by a user repository.
     *
     * @param repository repository used to update user calendars
     */
    public EventManager(UserRepository repository) {
        this.repository = repository;
        this.eventRepository = null;
        this.inviteManager = new InviteManager(repository);
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
        this.inviteManager = new InviteManager(repository);
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

        if (this.repository != null) {
            for (User user : this.repository.getAll()) {
                if (user.getCalendar() != null) {
                    user.getCalendar().removeEvent(event);
                }
            }

            if (this.eventRepository != null) {
                this.eventRepository.deleteItem(event.getEventId());
            }
            return;
        }
    }

    public void rejectInvite(Invite invite, Event event) {
        invite.setInviteStatus(InviteStatus.REJECTED);
        if (this.repository != null) {
            User invitee = this.repository.getItemById(invite.getRecipient());

            if (invitee != null) {
                this.inviteManager.removeInvite(event, invitee);
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

        if (organizer.getCalendar() == null) {
            organizer.setCalendar(new UserCalendar(new ArrayList<>()));
        }
        
        if (!organizer.getCalendar().getEvents().contains(event)) {
            organizer.getCalendar().addEvent(event);
        }
        
        if (!organizer.getCalendar().getEvents().contains(event)) {
            organizer.getCalendar().addEvent(event);
        }
    }

    /**
     * Gets User object of event organizer.
     *
     * @param event event whose organizer should be loaded
     * @return organizer user
     */
    public User getOrganizer(Event event) {
        String organizerUsername = event.getOrganizer();
        return this.repository.getItemById(organizerUsername);
    }

    /**
     * Returns all events in the repository where the given user participates as a guest.
     *
     * @param username username to search for
     * @param repo repository containing all events
     * @return list of events in which the user participates as a guest
     */
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
    
    /* Returns all events in the repository that are organized by the given user.
    *
    * @param username username to search for
    * @param repo repository containing all events
    * @return list of events organized by the user
    */
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

    /**
     * Imports an ICS calendar for a user and synchronizes it with the central
     * event repository.
     *
     * @param user user whose calendar should be updated
     * @param filePath path to the ICS file
     * @return status of the import operation
     */
    public ImportStatus importIcs(User user, String filePath) {
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath(filePath);
        importer.runImport();

        ImportStatus status = importer.getLastImportStatus();

        if (status == ImportStatus.Succes && this.eventRepository != null) {
            // 1. Remove old imported events for this user from the central repository
            List<Event> existingEvents = new ArrayList<>(this.eventRepository.getAll());
            for (Event event : existingEvents) {
                if (event.isImported() && user.getUsername().equals(event.getOrganizer())) {
                    this.eventRepository.deleteItem(event.getEventId());
                }
            }

            // 2. Add newly imported events from the user's calendar to the central repository
            if (user.getCalendar() != null) {
                for (Event event : user.getCalendar().getEvents()) {
                    if (event.isImported()) {
                        // Avoid duplicates if they already exist for some reason
                        if (this.eventRepository.getItemById(event.getEventId()) == null) {
                            this.eventRepository.save(event);
                        }
                    }
                }
            }
        }

        return status;
    }
}
