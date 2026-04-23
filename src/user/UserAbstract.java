package user;

public abstract class UserAbstract {

    protected String username;
    protected String password;

    public UserAbstract(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract boolean canAccessAdminPanel();

    public abstract boolean canDeleteUser(User targetUser);
}