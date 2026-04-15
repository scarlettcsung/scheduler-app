package Repository;

import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;

/**
 * In-memory repository for {@link User} instances.
 *
 * @author CR EO NJ
 * @version 1 and 3
 */
public class UserRepository extends Repository<User> {

    /**
     * Creates a repository pre-populated with the default admin account.
     */
    public UserRepository() {
        super();
        AdminUser admin = new AdminUser("admin", "admin", null);
        admin.setCalendar(new UserCalendar(admin.getUsername(), null));
        data.add(admin);
    }
    
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
     * Deletes a user when the current user is authorized to do so.
     *
     * @param username username of the user to delete
     * @param currentUser user requesting the deletion
     * @return {@code 1} when nobody is authenticated, {@code 2} when an admin
     *         deleted the user, {@code 3} when a user deleted themself, or
     *         {@code 4} when deletion was not permitted or the user was missing
     */
    public int deleteUserData(String username, User currentUser) {

        if (currentUser == null) {
            return 1;
        }

        User targetUser = findUsername(username);
        if (targetUser == null) {
            return 4;
        }

        if (!currentUser.canDeleteUser(targetUser)) {
            return 4;
        }

        data.removeIf(u -> u.getUsername().equals(username));

       
        if (currentUser.canAccessAdminPanel()) {
            return 2; 
        }

        return 3; 
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
}
