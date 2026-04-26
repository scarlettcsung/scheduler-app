**Title** : Accept Invite
**ID** : 0
**As a...** user
**I want to ...** accept an event invite
**So that...** I can accept the invitation and be added to the event

**Acceptance criteria:**
- A pending invite is visible to the invited user.
- The invited user can accept the invite.
- The invite status changes to accepted.
- The accepted event appears in the invited user's event list.

**Acceptance Test:**
Test 1: Invited user accepts an invite
- Given: User "Alice" is logged in
- And: Alice has a pending invite to "Project Meeting" from "Bob"
- When: Alice clicks "Accept" on the invite
- Then: the invite status is updated to "ACCEPTED"
- And: "Project Meeting" appears in Alice's event list
