**Title** : Uninvite  
**ID** : 15  
**As a...** user  
**I want to ...** remove a participant from my event   
**So that...** I can manage the guest list if someone is no longer required

**Acceptance criteria:**  
-[x] Organizer of event can remove an user from the event by clicking "Uninvite" while updating event

**Acceptance Test:**  
Test 1: Event organizer uninvites user
- Given: User has created an event and invited an invitee
- When: User goes to "update event", types in invitee name, and clicks "Uninvite"
- Then: Invitee is no longer invited to the event
- And: Invitee cannot see the event on their user panel
