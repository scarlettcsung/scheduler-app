package invite;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an invitation for a user to join an event.
 *
 * @author SS NS
 * @version IT1
 */
public class Invite {

    private String recipientUsername;
    @SerializedName(value = "eventId", alternate = {"eventID"})
    private String eventId;
    private InviteStatus status;
	private Role role;

    /**
     * Creates a new pending invite for an event recipient.
     *
     * @param recipientUsername username of the invite recipient
     * @param eventId identifier of the related event
     */
    public Invite(String recipientUsername, String eventId) {
        this.recipientUsername = recipientUsername;
        this.eventId = eventId;
        this.status = InviteStatus.PENDING;
    }

    // Status update
    /**
     * Marks the invite as accepted.
     */
    public void accept() {
        status = InviteStatus.ACCEPTED;
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
    public String getEventId() {
        return eventId;
    }
    /**
     * Returns the current invite status.
     *
     * @return invite status
     */
    public InviteStatus getStatus() {
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
     * @param eventId new event identifier
     */
    public void setEvent(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Updates the invite status directly.
     *
     * @param status new invite status
     */
    public void setInviteStatus(InviteStatus status) {
        this.status = status;
    }

    public void setOrganizer() {
        this.role = Role.Organiser;
    }

    public void setGuest() {
        this.role = Role.Guest;
    }

    public Role getRole() {
        return role;
    }
}
