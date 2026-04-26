**Title** : edit invites  
**ID** : 10  
**As a...** user  
**I want to ...** easily add or remove invites while creating or updating event.  
**So that...** I can edit who attends if things change.  
 
**Acceptance criteria:**  
[x] Event organizer should be able to click a button to add or remove invitees when updating an event  
[x] Invitees should be able to click a button to reject an invite and remove themselves from the event  
 
**Acceptance Test:**  
Test 1: Event organizer "exampleorganizer" adds user "exampleuser" after creating the event
- Given: exampleorganizer is an user who has made an event and is at the update event screen  
- And: exampleuser is not in the event  
- When: exampleorganizer types exampleuser and clicks invite  
- Then: exampleuser is in the event
- And: exampleuser gets an invite that they can accept or reject

Test 2: Event organizer "exampleorganizer" removes user "exampleuser" that is no longer invited  
- Given: exampleorganizer is an user who has made an event and is at the update event screen  
- And: exampleuser is already in the event  
- When: exampleorganizer types exampleuser and clicks uninvite  
- Then: exampleuser is no longer in the event
- And: exampleuser can no longer see the event in their events

Test 3: User "exampleuser" rejects event
- Given: example user was invited to an event
- When: exampleuser clicks "reject" on the invite that they received
- Then: exampleuser is no longer in the event
- And: exampleuser can no longer see the event in their events

