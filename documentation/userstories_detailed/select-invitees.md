**Title** : Select Invitees
**ID** : 18
**As a...** user
**I want to ...** select users from the system to invite to my event
**So that...** I can select users from the system to invite to my event

**Acceptance criteria:**
- The organizer can enter a username while creating or updating an event.
- The organizer can add an existing user as an invitee.
- The organizer cannot invite a username that does not exist.
- The organizer cannot invite themselves.
- The organizer cannot add the same invitee twice.

**Acceptance Test:**
Test 1: Organizer invites an existing user
- Given: User "Bob" exists in the system
- And: User "Alice" is creating an event
- When: Alice enters "Bob" in the invitee field and clicks "Invite"
- Then: Bob is shown in the participants list
- And: Bob receives a pending invite after the event is saved

Test 2: Organizer cannot invite an unknown user
- Given: User "Charlie" does not exist in the system
- And: User "Alice" is creating an event
- When: Alice enters "Charlie" in the invitee field and clicks "Invite"
- Then: the system shows a validation error
- And: Charlie is not shown in the participants list

Test 3: Organizer cannot invite themselves
- Given: User "Alice" is creating an event
- When: Alice enters "Alice" in the invitee field and clicks "Invite"
- Then: the system shows a validation error
- And: Alice is not added as a guest invitee
