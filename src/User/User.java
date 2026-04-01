package User;


import UserCalendar.UserCalendar;

public class User {
	
	public User(String username, String password, UserCalendar myCalendar, boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.myCalendar = myCalendar;
		this.isAdmin = isAdmin;
	}

	private String username;
	private String password;
	private boolean isAdmin;
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
	public boolean isAdmin() {
		return isAdmin;
	}
}
