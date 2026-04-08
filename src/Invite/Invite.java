package Invite;

public class Invite {

    private String recipientUsername;
    private String eventID;
    private inviteStatus status;

    public Invite(String recipientUsername, String eventID) {
        this.recipientUsername = recipientUsername;
        this.eventID = eventID;
        this.status = inviteStatus.PENDING;
    }

    // Status update
    public void accept() {status = inviteStatus.ACCEPTED;}
    // reject moved to EventManager to avoid circularity


    // Getter methods
    public String getRecipient() {return recipientUsername;}
    public String getEventID() {
        return eventID;
    }
    public inviteStatus getStatus() {return status;}

    // Setter methods
    public void setRecipient(String recipient_username) {this.recipientUsername = recipientUsername;}
    public void setEvent(String eventID) {this.eventID = eventID;}
    public void setInviteStatus(inviteStatus status) {this.status = status;}
}
