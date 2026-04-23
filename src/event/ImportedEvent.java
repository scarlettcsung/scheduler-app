package event;

import java.util.List;

import invite.Invite;

/**
 * Event imported from external calendar data.
 *
 * @author EO SS
 * @version 1
 */
public class ImportedEvent extends Event{

	/**
	 * Creates an imported event.
	 *
	 * @param eventName display name of the event
	 * @param eventDuration duration in minutes
	 * @param eventDescription textual event description
	 * @param organizerUsername username of the event organizer
	 * @param invites invites associated with the event
	 */
	public ImportedEvent(String eventName, int eventDuration, String eventDescription, String organizerUsername,
			List<Invite> invites) {
		super(eventName, eventDuration, eventDescription, organizerUsername, invites);
		this.isImportedField = true; // Just for IO
	}
	
	/**
	 * Indicates that this event came from an imported calendar.
	 *
	 * @return always {@code true}
	 */
	@Override
	public boolean isImported() { return true; }
	}

