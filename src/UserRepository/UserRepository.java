package UserRepository;

import User.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();
    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public void saveUser(User user) {
        users.add(user);
    }

    public boolean deleteUserData(String username) {
        return users.removeIf(u -> u.getUsername().equals(username));
    }

    public User findUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

	public boolean isExistingUser(String username) {
		if (findUsername(username) != null){
			return true;
		}
		else {
			return false;
		}
	}
	public boolean deleteUser(String username) {
	    if (username == null) return false;        

	    User target = findUsername(username);  

	    if (target != null) {                     
	        users.remove(target);                  
	        return true;                           
	    }
	    return false;                              
	}
}