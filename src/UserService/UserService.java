package UserService;

import User.User;
import UserRepository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    // Dependency Injection: The repository is passed in from the outside
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String username, String password) {
		if (userRepository.isExistingUser(username)) {
			return false;
		}
		else {
        User newUser = new User(username, password, null, true);
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