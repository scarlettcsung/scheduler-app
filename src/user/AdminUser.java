package user;

import user.calendar.UserCalendar;

/**
 * Specialised user with administrative privileges.
 *
 * @author SN GI
 * @version 2
 */
public class AdminUser extends User {

    /**
     * Creates an admin user account.
     *
     * @param username admin username
     * @param password admin password
     * @param myCalendar calendar owned by the admin
     */
    public AdminUser(String username, String password, UserCalendar myCalendar) {
        super(username, password, myCalendar);
    }

    /**
     * Indicates that admin users may access the admin panel.
     *
     * @return always {@code true}
     */
    @Override
    public boolean canAccessAdminPanel() {
        return true;
    }

    /**
     * Indicates that admins may delete any user account.
     *
     * @param targetUser user targeted for deletion
     * @return {@code true} when a target user was supplied
     */
    @Override
    public boolean canDeleteUser(User targetUser) {
        return targetUser != null;
    }
}
