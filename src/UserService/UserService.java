package UserService;

import Authentication.Authentication;
import Repository.UserRepository;
import User.User;
import UserCalendar.UserCalendar;

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
// changed 
    /**
     * Creates a service backed by the given repository.
     *
     * @param userRepository repository used for user operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authentication = new Authentication(userRepository);
    }
    
// changed 
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
	else {
        User newUser = new User(username, password, null);
        newUser.setCalendar(new UserCalendar(null));
        userRepository.saveUser(newUser);
       
        return true;
		}
    }
 // login implemented EO GI: 2/4/2026
    /**
     * Attempts to log in a user.
     *
     * @param username username to authenticate
     * @param password password to validate
     * @return {@code true} when authentication succeeded
     */
    public boolean login(String username, String password) {
		return authentication.login(username, password);
	}

    /**
     * Deletes the named user when the authenticated user is permitted to do so.
     *
     * @param username username of the account to delete
     * @return {@code true} when the account was deleted
     */
    public boolean deleteUser(String username) {
    	
    	int adminDelete = 2;
    	int selfDelete = 3;
    	
		if (!userRepository.isExistingUser(username)) {
			return false;
		}
		// Finally, it differentiates the current user
		User currentUser = authentication.getauthenticatedUser();
		int deleteStatus = userRepository.deleteUserData(username, currentUser);
		if (deleteStatus == adminDelete || deleteStatus == selfDelete) { // 2->admin deleted, 3->self deleted
			return true;
		} else {
			return false;
		}
    }
}
