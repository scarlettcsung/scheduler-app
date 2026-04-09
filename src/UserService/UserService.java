package UserService;

import User.User;
import Authentication.Authentication;
import Repository.UserRepository;

public class UserService {
    private UserRepository userRepository;
    private Authentication authentication;
// changed 
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authentication = new Authentication(userRepository);
    }
    
// changed 
    public boolean registerUser(String username, String password) {
		if (userRepository.isExistingUser(username)) {
			return false;
		}	
		else {
        User newUser = new User(username, password, null);
        userRepository.saveUser(newUser);
       
        return true;
		}
    }
 // login implemented EO GI: 2/4/2026
    public boolean login(String username, String password) {
		return authentication.login(username, password);
	}

    public boolean deleteUser(String username) {
		if (!userRepository.isExistingUser(username)) {
			return false;
		}
		// Finally, it differentiates the current user
		User currentUser = authentication.getauthenticatedUser();
		int deleteStatus = userRepository.deleteUserData(username, currentUser);
		if (deleteStatus == 2 || deleteStatus == 3) { // 2->admin deleted, 3->self deleted
			return true;
		} else {
			return false;
		}
    }
}

