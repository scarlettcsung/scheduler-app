package UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
	// declare type
	private List<User> users;
	
	// constructor
	public UserRepository() {
		this.users = new ArrayList<>();
	}
	
	// save users to make them known to the class
	public void saveUser(User newUser) {
		this.users.add(newUser);
	}
	// retrieve user object
	public User findUsername(String username) {
		User foundUser = null;
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
	}
	

}
