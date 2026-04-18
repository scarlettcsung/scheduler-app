package event;

import java.util.List;

import Invite.Invite;

public class CreatedEvent extends Event{

	public CreatedEvent(String eventName, int eventDuration, String eventDescription, String organizerUsername,
			List<Invite> invites) {
		super(eventName, eventDuration, eventDescription, organizerUsername, invites);
		this.isImportedField = false; // Just for IO
	}
	
	@Override
	public boolean isImported() { return false; }
	
	}

