package user.service;

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


}
