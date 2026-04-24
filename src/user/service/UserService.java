package user.service;

import java.util.ArrayList;
import java.util.List;

import authentication.Authentication;
import repository.UserRepository;
import user.User;
import user.calendar.UserCalendar;

/**
 * Coordinates user-facing operations such as registration, login, and account
 * deletion.
 *
 * @author SN GI
 * @version 1
 */
public class UserService {
    private UserRepository userRepository;
    private Authentication authentication;

    /**
     * Creates a service backed by the given repository.
     *
     * @param userRepository repository used for user operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authentication = new Authentication(userRepository);
    }

    /**
     * Registers a new user when the username is still available.
     *
     * @param username requested username
     * @param password password for the new account
     * @return {@code true} when the user was created
     */
    public boolean registerUser(String username, String password) {
        if (userRepository.isExistingUser(username)) {
            return false;
        }

        User newUser = new User(username, password, null);
        newUser.setCalendar(new UserCalendar(null));
        userRepository.saveUser(newUser);

        return true;
    }

    /**
     * Attempts to authenticate a user and returns the matching user when the
     * credentials are valid.
     *
     * @param username username to authenticate
     * @param password password to validate
     * @return authenticated user, or {@code null} when authentication failed
     */
    public User authenticateUser(String username, String password) {
        if (!authentication.login(username, password)) {
            return null;
        }

        return authentication.getAuthenticatedUser();
    }

    /**
     * Attempts to log in a user.
     *
     * @param username username to authenticate
     * @param password password to validate
     * @return {@code true} when authentication succeeded
     */
    public boolean login(String username, String password) {
        return authenticateUser(username, password) != null;
    }

    /**
     * Returns usernames for GUI list rendering without exposing the repository
     * directly.
     *
     * @return usernames currently stored in the repository
     */
    public List<String> listUsernames() {
        List<String> usernames = new ArrayList<>();

        for (User user : userRepository.getAll()) {
            usernames.add(user.getUsername());
        }

        return usernames;
    }

    /**
     * Deletes a target user on behalf of the current actor.
     *
     * @param username username to delete
     * @param currentUser user requesting the deletion
     * @return named deletion result for the request
     */
    public UserDeletionResult deleteUser(String username, User currentUser) {
        return userRepository.deleteUserData(username, currentUser);
    }

    /**
     * Deletes the currently logged-in user's own account.
     *
     * @param currentUser user requesting self-deletion
     * @return named deletion result for the request
     */
    public UserDeletionResult deleteOwnAccount(User currentUser) {
        if (currentUser == null) {
            return UserDeletionResult.NOT_AUTHENTICATED;
        }

        return deleteUser(currentUser.getUsername(), currentUser);
    }
}
