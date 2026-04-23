package event.service;

import event.Event;
import invite.Invite;

/**
 * Read model that keeps an invite together with the event it belongs to.
 *
 * @author EO SN
 * @version 1
 */
public class EventInviteView {

    private final Event event;
    private final Invite invite;

    /**
     * Creates an invite view for GUI rendering.
     *
     * @param event event that owns the invite
     * @param invite invite to display
     */
    public EventInviteView(Event event, Invite invite) {
        this.event = event;
        this.invite = invite;
    }

    /**
     * Returns the event that owns the invite.
     *
     * @return event to display
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Returns the invite to display.
     *
     * @return invite to display
     */
    public Invite getInvite() {
        return invite;
    }
}
