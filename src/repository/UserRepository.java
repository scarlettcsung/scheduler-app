package repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import event.Event;
import user.User;
import user.service.UserDeletionResult;

/**
 * In-memory repository for {@link User} instances.
 *
 * @author CR EO NJ
 * @version 1 and 3
 */
public class UserRepository extends Repository<User> {
    private EventRepository eventRepository;

    // hard-coded admin
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
     * @return named deletion result for the request
     */
    public UserDeletionResult deleteUserData(String username, User currentUser) {
        if (currentUser == null) {
            return UserDeletionResult.NOT_AUTHENTICATED;
        }

        User targetUser = getItemById(username);
        if (targetUser == null) {
            return UserDeletionResult.NOT_PERMITTED;
        }

        if (!currentUser.canDeleteUser(targetUser)) {
            return UserDeletionResult.NOT_PERMITTED;
        }

        if ("admin".equals(targetUser.getUsername())) {
            return UserDeletionResult.NOT_PERMITTED;
        }

        cleanupUserEventReferences(targetUser.getUsername());

        this.data.removeIf(u -> u.getUsername().equals(username));

        if (currentUser.canAccessAdminPanel()) {
            return UserDeletionResult.DELETED_BY_ADMIN;
        }

        return UserDeletionResult.DELETED_SELF;
    }

    /**
     * Finds a user by username.
     *
     * @param username username to search for
     * @return matching user, or {@code null} when not found
     */
    @Override
    public User getItemById(String username) {
        for (User u : this.data) {
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
        return getItemById(username) != null;
    }

    /**
     * Removes references to a user from events and calendars before deletion.
     *
     * @param username username whose event references should be cleaned up
     */
    public void cleanupUserEventReferences(String username) {
        if (this.eventRepository == null) {
            return;
        }

        List<Event> allEvents = new ArrayList<>(this.eventRepository.getAll());

        for (Event event : allEvents) {
            if (event == null) continue;

            // Deletes event if user is organizer
            if (username.equals(event.getOrganizer())) {
                this.eventRepository.deleteItem(event.getEventId());
                continue; 
            }

            if (event.getInvites() != null) {
                event.getInvites().removeIf(invite -> 
                    invite != null && username.equals(invite.getRecipient())
                );
            }
        }
    }

}
