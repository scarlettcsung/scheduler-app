**Title** : delete event
**ID** : 0
**As a...** user
**I want to ...** delete my event
**So that...** if the event is no longer happening I can remove it from everyone's schedule

**Acceptance criteria:**

- Only the event creator can delete the event
- The user must be able to delete the event from the manage event screen
- After confirmation, the event should be removed from the creator's event list
- Invited users should no longer see the deleted event in their events

**Acceptance Test:**
Given: I am logged in as the creator of an event

When: I click "Delete event" on the manage event screen and confirm the deletion

Then: the event should be removed from my events list

And: invited users should no longer see the event in their events
