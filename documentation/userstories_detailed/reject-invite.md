**Title** : Reject Invite
**ID** : 16
**As a...** user
**I want to ...** reject an event invite
**So that...** I do not have to go to events I do not want to go to

**Acceptance criteria:**
- A pending invite is visible to the invited user.
- The invited user can decline the invite.
- The invite status changes to rejected.
- The declined event is removed from the invited user's invite list.
- The declined event does not appear in the invited user's event list.

**Acceptance Test:**
Test 1: Invited user declines an invite
- Given: User "Alice" is logged in
- And: Alice has a pending invite to "Project Meeting" from "Bob"
- When: Alice clicks "Decline" on the invite
- Then: the invite status is updated to "REJECTED"
- And: Alice no longer sees the invite in the invite list
- And: "Project Meeting" does not appear in Alice's event list
