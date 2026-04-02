package UserService;

import User.User;
import UserRepository.UserRepository;

public class UserService {
    private UserRepository userRepository;
// changed 
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
// changed 
    public boolean registerUser(String username, String password) {
		if (userRepository.isExistingUser(username)) {
			return false;
		}	
		else {
        User newUser = new User(username, password, null, false);
        userRepository.saveUser(newUser);
       
        return true;
		}
    }

    public boolean deleteUser(String username) {
		if (userRepository.isExistingUser(username)) {
			userRepository.deleteUserData(username);
			return true;
		}
		else {
			return false;
		}
    }
}

