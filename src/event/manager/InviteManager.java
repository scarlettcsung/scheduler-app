package event.manager;

import java.util.List;
import event.Event;
import invite.Invite;
import invite.Role;
import user.User;
import repository.UserRepository;

/**
 * Handles the logic for managing event invites and temporary invite lists.
 */
public class InviteManager {

    private UserRepository repository;

    /**
     * Creates an invite manager with the supplied user repository.
     *
     * @param repository user repository for user validation
     */
    public InviteManager(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Validates and adds a username to a temporary invite list.
     *
     * @param currentUser the user performing the action
     * @param event the event to add the invite to
     * @param tempInvites the current list of temporary invites
     * @param inviteeUsername the username to add
     * @return null on success, or an error message string
     */
    public String addTemporaryInvite(User currentUser, Event event, List<String> tempInvites, String inviteeUsername) {
        if (inviteeUsername == null || inviteeUsername.trim().isEmpty()
                || inviteeUsername.equalsIgnoreCase("Invitee username")) {
            return "Please enter a username.";
        }

        if (inviteeUsername.equals(currentUser.getUsername())) {
            return "Event organizer already invited to this event.";
        }

        User invitee = repository.getItemById(inviteeUsername);
        if (invitee == null) {
            return "User not found.";
        }

        boolean alreadyTemporary = tempInvites.contains(inviteeUsername);
        boolean alreadyInvited = event != null && event.getParticipants().contains(inviteeUsername);

        if (alreadyTemporary || alreadyInvited) {
            return "User is already invited to this event!";
        }

        tempInvites.add(inviteeUsername);
        return null;
    }

    /**
     * Removes an invite or temporary invite from the event.
     *
     * @param currentUser the user performing the action
     * @param event the event to remove the invite from
     * @param tempInvites the current list of temporary invites
     * @param inviteeUsername the username to remove
     * @return null on success, or an error message string
     */
    public String removeInviteFromForm(User currentUser, Event event, List<String> tempInvites, String inviteeUsername) {
        if (inviteeUsername == null
                || inviteeUsername.trim().isEmpty()
                || inviteeUsername.equalsIgnoreCase("Invitee username")) {
            return "Please enter a username.";
        }

        if (inviteeUsername.equals(currentUser.getUsername())) {
            return "You cannot uninvite the organizer.";
        }

        User invitee = repository.getItemById(inviteeUsername);
        if (invitee == null) {
            return "User not found.";
        }

        if (tempInvites.contains(inviteeUsername)) {
            tempInvites.remove(inviteeUsername);
            return null;
        }

        if (event != null && hasExistingInvite(event, inviteeUsername)) {
            removeInvite(event, invitee);
            return null;
        }

        return "User is not in this event!";
    }

    /**
     * Removes an invite from an event and the recipient's calendar.
     *
     * @param event event to remove invite from
     * @param recipient user associated with invite
     */
    public void removeInvite(Event event, User recipient) {
        String username = recipient.getUsername();

        if (!hasExistingInvite(event, username)) {
            return;
        }

        event.getInvites().removeIf(i -> i.getRecipient().equals(username));
    }

    /**
     * Adds an invite for a recipient to an event.
     *
     * @param event event to add invite to
     * @param recipient user to invite
     */
    public void addInvite(Event event, User recipient, Role role) {
        if (event == null || recipient == null) {
            return;
        }

        if (hasExistingInvite(event, recipient.getUsername())) {
            return;
        }
        event.getInvites().add(new Invite(recipient.getUsername(), event.getEventId(), role));
    }

    /**
     * Checks if a user already has an invite for an event.
     *
     * @param event event to check
     * @param username username to look for
     * @return true if an invite exists
     */
    private boolean hasExistingInvite(Event event, String username) {
        if (event == null || event.getInvites() == null) {
            return false;
        }
        for (Invite i : event.getInvites()) {
            if (i.getRecipient().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
