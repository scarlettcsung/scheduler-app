**Title** : See list of invitees  
**ID** : 20  
**As a...** user  
**I want to ...** see the list of invitees   
**So that...** I can see who else is attending the event

**Acceptance criteria:**  
-[x] Invitees of event can be seen by all participants

**Acceptance Test:**  
Test 1: Organizer wishes to see participants
- Given: User created an event and invited 3 participants
- When: User clicks on the event name on the user panel
- Given: All participants did not reject the event
- Then: User can see all 3 participants

Test 2: User wishes to see participants
- Given: User was invited to an event
- When: User clicks on the event name on the user panel
- Then: User can see all participants

Test 3: Participant rejects invite
- Given: User created an event and invited 3 participants
- When: User clicks on the event name on the user panel
- Given: One participant rejected the invite
- Then: User can see 2 participants usernames, not including that of the participant that rejected invite