package UserRepository;

import User.AdminUser;
import User.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
        users.add(new AdminUser("admin", "admin", null));
    }

    public void saveUser(User user) {
        users.add(user);
    }

    // Get user list EO GI: 5/4/2026. copies the list
    public List<User> getListUsers() {
        List<User> safeList = new ArrayList<User>();
        for (int i = 0; i < users.size(); i++) {
            safeList.add(users.get(i));
        }
        return safeList;
    }

    public int deleteUserData(String username, User currentUser) {

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

        boolean removed = users.removeIf(u -> u.getUsername().equals(username));
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
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean isExistingUser(String username) {
        if (findUsername(username) != null) {
            return true;
        } else {
            return false;
        }
    }
}

/*

*/
