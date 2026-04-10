package User;

import UserCalendar.UserCalendar;

public class AdminUser extends User {

	public AdminUser(String username, String password, UserCalendar myCalendar) {
		super(username, password, myCalendar);
	}
	
	@Override
	public boolean canAccessAdminPanel() {
		return true;
	}
	
	@Override
	public boolean canDeleteUser(User targetUser) {
		return targetUser != null;
	}
}