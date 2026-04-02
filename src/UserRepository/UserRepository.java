package UserRepository;

import User.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users;

    public UserRepository() {
    	
        this.users = new ArrayList<>();
        users.add(new User("admin", "admin", null, true));
    }

    public void saveUser(User user) {
        users.add(user);
    }

    public int deleteUserData(String username, User currentUser) {

        // 1 → Not logged in
        if (currentUser == null) {
            return 1;
        }

        // 2 → Admin → can delete anyone
        if (currentUser.isAdmin()) {

            boolean removed = users.removeIf(u -> u.getUsername().equals(username));

            if (removed == true) {
                return 2;
            } else {
                return 4;
            }
        }

        // 3 → Normal user → only themselves
        if (currentUser.getUsername().equals(username)) {

            boolean removed = users.removeIf(u -> u.getUsername().equals(username));

            if (removed == true) {
                return 3;
            } else {
                return 4;
            }
        }

        // 4 → Not allowed
        return 4;
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
}

/*

*/