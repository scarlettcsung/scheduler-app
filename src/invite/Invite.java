package invite;

/**
 * Represents an invitation for a user to join an event.
 *
 * @author SS NS
 * @version IT1
 */
public class Invite {

    private String recipientUsername;
    private String eventID;
    private inviteStatus status;
	private Role role;

    /**
     * Creates a new pending invite for an event recipient.
     *
     * @param recipientUsername username of the invite recipient
     * @param eventID identifier of the related event
     */
    public Invite(String recipientUsername, String eventID) {
        this.recipientUsername = recipientUsername;
        this.eventID = eventID;
        this.status = inviteStatus.PENDING;
    }

    // Status update
    /**
     * Marks the invite as accepted.
     */
    public void accept() {
        status = inviteStatus.ACCEPTED;
    }

    // reject moved to EventManager to avoid circularity


    // Getter methods
    /**
     * Returns the username of the invite recipient.
     *
     * @return recipient username
     */
    public String getRecipient() {
        return recipientUsername;
    }

    /**
     * Returns the associated event identifier.
     *
     * @return event identifier
     */
    public String getEventID() {
        return eventID;
    }
    /**
     * Returns the current invite status.
     *
     * @return invite status
     */
    public inviteStatus getStatus() {
        return status;
    }

    // Setter methods
    /**
     * Updates the invite recipient.
     *
     * @param recipientUsername new recipient username
     */
    public void setRecipient(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    /**
     * Updates the associated event identifier.
     *
     * @param eventID new event identifier
     */
    public void setEvent(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Updates the invite status directly.
     *
     * @param status new invite status
     */
    public void setInviteStatus(inviteStatus status) {
        this.status = status;
    }

    public void setOrganiser() {
        this.role = Role.Organiser;
    }

    public void setGuest() {
        this.role = Role.Guest;
    }

    public Role getRole() {
        return role;
    }
}
