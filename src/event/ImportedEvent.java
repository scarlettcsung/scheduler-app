package event;

import java.util.List;

import invite.Invite;

public class ImportedEvent extends Event{

	public ImportedEvent(String eventName, int eventDuration, String eventDescription, String organizerUsername,
			List<Invite> invites) {
		super(eventName, eventDuration, eventDescription, organizerUsername, invites);
		this.isImportedField = true; // Just for IO
	}
	
	@Override
	public boolean isImported() { return true; }
	}

