package user;

/**
 * Shared account identity state used by application users.
 *
 * @author NS SN
 * @version 1
 */
public abstract class Account {

    private String username;
    private String password;

    /**
     * Creates account identity details.
     *
     * @param username username for the account
     * @param password password for the account
     */
    protected Account(String username, String password) {
        this.username = username;
        this.password = password;
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
}
