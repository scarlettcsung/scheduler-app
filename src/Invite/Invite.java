package Invite;

import Event.Event;
import User.User;

public class Invite {

    private User recipient;
    private Event event;
    private inviteStatus status;

    public Invite(User recipient, Event event) {
        this.recipient = recipient;
        this.event = event;
        this.status = inviteStatus.PENDING;
        }

    public void accept() {
        status = inviteStatus.ACCEPTED;
    }
    public void reject() {
        status = inviteStatus.REJECTED;
    }
    public User getRecipient() {
        return recipient;
    }
    public Event getEvent() {
        return event;
    }
    public inviteStatus getStatus() {return status;}
}
