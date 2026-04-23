package authentication;

import repository.UserRepository;
import user.User;

/**
 * Authenticates users against a UserRepository and keeps track of the
 * currently logged-in user for the active session.
 *
 * @author AA EO
 * @version 1
 */
public class Authentication {

    // Authenticated user, the user that will have their details presented.
    private User authenticatedUser;
    private UserRepository repository;

    /**
     * Creates a new authentication object backed by the supplied repository.
     *
     * @param repository repository used to look up users by username
     */
    public Authentication(UserRepository repository) {
        this.repository = repository;
        this.authenticatedUser = null;
    }

    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param UserName username to authenticate
     * @param UserPassword password to validate for the user
     * @return {@code true} when the credentials match an existing user
     */
    public boolean login(String UserName, String UserPassword) {
        User currentUser = repository.findUsername(UserName);

        if (currentUser == null) {
            return false;
        }

        if (!currentUser.getPassword().equals(UserPassword)) {
            return false;
        }

        authenticatedUser = currentUser;
        return true;
    }

    /**
     * Clears the currently authenticated user.
     */
    public void logout() {
        authenticatedUser = null;
    }

    /**
     * Returns the user that is currently authenticated.
     *
     * @return the authenticated user, or {@code null} when nobody is logged in
     */
    public User getauthenticatedUser() {
        return authenticatedUser;
    }
}
