package EventManager;

import java.time.LocalDateTime;

import Event.Event;
import Invite.Invite;
import Invite.inviteStatus;

public class EventManager {

    public void updateEvent(Event event, String updateAspect, String newValue) {
    	
    	// You should type enter eventName for example as a parameter to updateAspect part.
    	
        switch (updateAspect) {
            case "eventName":
                event.setEventName(newValue);
                break;
            case "eventDescription":
                event.setEventDescription(newValue);
                break;
            case "eventTime":
            	LocalDateTime date = LocalDateTime.parse(newValue);
            	event.setEventTime(date);
                break;
            case "eventDuration":
                event.setEventDuration(Integer.parseInt(newValue));
                break;
        }
    }

    public void deleteEvent(Event event) {
    	/*
    	if (event.getOrganizer() != null && event.getOrganizer().getCalendar() != null) {
    		    event.getOrganizer().getCalendar().removeEvent(event);
    		}
    }
    */
		event.setEventName(null);
		event.setEventDescription(null);
		event.setEventTime(null);
		event.setEventDuration(0);
	}

    public void rejectInvite(Invite invite, Event event) {
        invite.setInviteStatus(inviteStatus.REJECTED);
        event.removeInvite(invite);
    }
}