package event;

import java.util.List;

import invite.Invite;

/**
 * Event created inside the application rather than imported from ICS data.
 *
 * @author EO SS
 * @version 1
 */
public class CreatedEvent extends Event {

    /**
     * Creates a non-imported event.
     *
     * @param eventName display name of the event
     * @param eventDuration duration in minutes
     * @param eventDescription textual event description
     * @param organizerUsername username of the event organizer
     * @param invites invites associated with the event
     */
    public CreatedEvent(String eventName, int eventDuration, String eventDescription, String organizerUsername,
            List<Invite> invites) {
        super(eventName, eventDuration, eventDescription, organizerUsername, invites);
        this.isImportedField = false; // Just for IO
    }

    /**
     * Indicates that this event was created in the application.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isImported() {
        return false;
    }
}

