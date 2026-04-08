package User;


import UserCalendar.UserCalendar;

public class User {
	
	public User(String username, String password, UserCalendar myCalendar) {
		this.username = username;
		this.password = password;
		this.myCalendar = myCalendar;
	}

	private String username;
	private String password;
	private UserCalendar myCalendar;
	
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public UserCalendar getCalendar() {
		return myCalendar;
	}
	
	public void setCalendar(UserCalendar calendar) {
	    this.myCalendar = calendar;
	}
	
	public boolean canAccessAdminPanel() {
		return false;
	}
	
	public boolean canDeleteUser(User targetUser) {
		if (targetUser == null) {
			return false;
		}
		return username.equals(targetUser.getUsername());
	}
}
