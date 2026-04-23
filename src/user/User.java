package user;

import user.calendar.UserCalendar;

public class User extends UserAbstract{

    private UserCalendar myCalendar;

    public User(String username, String password, UserCalendar myCalendar) {
        super(username, password);
        this.myCalendar = myCalendar;
    }

    public UserCalendar getCalendar() {
        return myCalendar;
    }

    public void setCalendar(UserCalendar calendar) {
        this.myCalendar = calendar;
    }

    @Override
    public boolean canAccessAdminPanel() {
        return false;
    }

    @Override
    public boolean canDeleteUser(User targetUser) {
        if (targetUser == null) return false;
        return username.equals(targetUser.getUsername());
    }
}