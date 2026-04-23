package user;

import user.calendar.UserCalendar;

/**
 * Represents an application user and their personal calendar.
 *
 * @author SN GI
 * @version 1
 */
public class User {

    private String username;
    private String password;
    private UserCalendar myCalendar;

    /**
     * Creates a user account.
     *
     * @param username username for the account
     * @param password password for the account
     * @param myCalendar calendar owned by the user
     */
    public User(String username, String password, UserCalendar myCalendar) {
        this.username = username;
        this.password = password;
        this.myCalendar = myCalendar;
    }

    /**
     * Returns the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's calendar.
     *
     * @return user calendar
     */
    public UserCalendar getCalendar() {
        return myCalendar;
    }

    /**
     * Replaces the user's calendar.
     *
     * @param calendar new calendar instance
     */
    public void setCalendar(UserCalendar calendar) {
        this.myCalendar = calendar;
    }

    /**
     * Indicates whether the user can open the admin panel.
     *
     * @return {@code false} for regular users
     */
    public boolean canAccessAdminPanel() {
        return false;
    }

    /**
     * Indicates whether this user may delete the target user.
     *
     * @param targetUser user targeted for deletion
     * @return {@code true} when the user is deleting their own account
     */
    public boolean canDeleteUser(User targetUser) {
        if (targetUser == null) {
            return false;
        }
        return username.equals(targetUser.getUsername());
    }
}
