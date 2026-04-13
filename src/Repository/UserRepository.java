package Repository;

import User.AdminUser;
import User.User;
import UserCalendar.UserCalendar;

public class UserRepository extends Repository<User> {

    public UserRepository() {
        super();
        AdminUser admin = new AdminUser("admin", "admin", null);
        admin.setCalendar(new UserCalendar(admin.getUsername(), null));
        data.add(admin);
    }
    
    @Override
    public String getRepositoryType() {
    	return "user Repository";
    }

    public void saveUser(User user) {
        save(user);
    }

    public int deleteUserData(String username, User currentUser) {

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

        if (currentUser.canAccessAdminPanel()) {
            return 2;
        }

        return 3;
    }

    public User findUsername(String username) {
        for (User u : data) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean isExistingUser(String username) {
        return findUsername(username) != null;
    }
}
