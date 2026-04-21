package Repository;

import java.util.LinkedHashMap;
import java.util.Map;

import event.Event;
import invite.Invite;
import user.AdminUser;
import user.User;
import user.calendar.UserCalendar;

/**
 * In-memory repository for {@link User} instances.
 *
 * @author CR EO NJ
 * @version 1 and 3
 */
public class UserRepository extends Repository<User> {
    private EventRepository eventRepository;
	
	//hard coded admin
	/*
	public UserRepository() {
        super();
        AdminUser admin = new AdminUser("admin", "admin", null);
        admin.setCalendar(new UserCalendar(null));
        data.add(admin);
    }
    */
    /**
     * Returns the repository type label.
     *
     * @return repository type name
     */
    @Override
    public String getRepositoryType() {
    	return "user Repository";
    }

    /**
     * Saves a user in the repository.
     *
     * @param user user to store
     */
    public void saveUser(User user) {
        save(user);
    }

    /**
     * Sets the shared event repository so user deletion can keep central event
     * storage aligned with calendar cleanup.
     *
     * @param eventRepository shared event repository
     */
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Deletes a user when the current user is authorized to do so.
     *
     * @param username username of the user to delete
     * @param currentUser user requesting the deletion
     * @return {@code 1} when nobody is authenticated, {@code 2} when an admin
     *         deleted the user, {@code 3} when a user deleted themself, or
     *         {@code 4} when deletion was not permitted or the user was missing
     */
    public int deleteUserData(String username, User currentUser) {
    	
    	int notAuthenticated = 1;
    	int adminDeletion = 2;
    	int deleteItself = 3;
    	int notPermitted = 4;

        if (currentUser == null) {
            return notAuthenticated;
        }

        User targetUser = findUsername(username);
        if (targetUser == null) {
            return notPermitted;
        }

        if (!currentUser.canDeleteUser(targetUser)) {
            return notPermitted;
        }
        
        if ("admin".equals(targetUser.getUsername())) {
            return notPermitted;
        }

        cleanupUserEventReferences(targetUser.getUsername());

        data.removeIf(u -> u.getUsername().equals(username));

       
        if (currentUser.canAccessAdminPanel()) {
            return adminDeletion; 
        }

        return deleteItself;
    }

    /**
     * Finds a user by username.
     *
     * @param username username to search for
     * @return matching user, or {@code null} when not found
     */
    public User findUsername(String username) {
        for (User u : data) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Checks whether a username already exists in the repository.
     *
     * @param username username to check
     * @return {@code true} when a user with that username exists
     */
    public boolean isExistingUser(String username) {
        return findUsername(username) != null;
    }

    public void cleanupUserEventReferences(String username) {
        Map<String, Event> eventsById = new LinkedHashMap<>();

        for (User user : data) {
            if (user.getCalendar() == null || user.getCalendar().getEvents() == null) {
                continue;
            }

            for (Event event : user.getCalendar().getEvents()) {
                if (event != null && event.getEventID() != null) {
                    eventsById.putIfAbsent(event.getEventID(), event);
                }
            }
        }

        if (eventRepository != null) {
            for (User ignored : data) {
                for (Event event : eventRepository.getAll()) {
                    if (event != null && event.getEventID() != null) {
                        eventsById.putIfAbsent(event.getEventID(), event);
                    }
                }
                break;
            }
        }

        for (Event event : eventsById.values()) {
            if (event == null) {
                continue;
            }

            if (username.equals(event.getOrganizer())) {
                removeEventFromAllCalendars(event);
                if (eventRepository != null) {
                    eventRepository.deleteEvent(event.getEventID());
                }
                continue;
            }

            if (event.getInvites() != null) {
                event.getInvites().removeIf(invite -> invite != null && username.equals(invite.getRecipient()));
            }
        }
    }

    public void removeEventFromAllCalendars(Event event) {
        for (User user : data) {
            if (user.getCalendar() != null) {
                user.getCalendar().removeEvent(event);
            }
        }
    }
}
