package Repository;

import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repository<User> {
	

<<<<<<< HEAD
    public UserRepository() {
        this.users = new ArrayList<>();
        AdminUser admin = new AdminUser("admin", "admin", null);
        admin.setCalendar(new UserCalendar(admin.getUsername(), null));
        users.add(admin);
    }
=======
	public UserRepository() {
		super();
		data.add(new AdminUser("admin", "admin", null));
	}
	
	public void saveUser(User user) {
	    save(user);
	}
>>>>>>> d2baabe (CR EO: Refactor UserRepository to use inheritance)

	public int deleteUserData(String username, User currentUser ) {
		
		// 1 -> Not logged in
		if (currentUser == null) {
			return 1;
		}
		
		User targetUser = findUsername(username);
		if (targetUser == null) {
			return 4;
		}
		
		if (!currentUser.canDeleteUser(targetUser)) {
			return 4;
		}
		
		boolean removed = data.removeIf(u -> u.getUsername().equals(username));
		if (!removed) {
			return 4;
		}
		
		// 2 -> Admin deleted, 3 -> Self deleted
		if (currentUser.canAccessAdminPanel()) {
			return 2;
		}
			
		return 3;
	}

    public User findUsername(String username) {
    	for(User u: data) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	return null; 
    }
    public boolean isExistingUser(String username){ 
    	return findUsername(username) !=null; 
    	
    
    }
}

