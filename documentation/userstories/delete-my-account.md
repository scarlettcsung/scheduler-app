**Title** : Delete My Account
**ID** : 0
**As a...** user
**I want to ...** delete myself from the system
**So that...** my previous data from the system will no longer be stored

**Acceptance criteria:**
- A logged-in user can delete their own account.
- The deleted account is removed from the active user list.
- The user is returned to the login screen after deleting their account.
- Events organized by the deleted user are removed from the event repository.
- Invites for the deleted user are removed from other events.

**Acceptance Test:**
Test 1: User deletes their own account
- Given: User "Alice" is logged in and is on the user panel
- When: Alice clicks "Delete Account"
- Then: Alice is removed from the system
- And: Alice is returned to the login screen
- And: Alice can no longer log in with the deleted account

Test 2: Deleted user's event references are cleaned up
- Given: User "Alice" organized an event called "Planning"
- And: Alice was invited to another event called "Demo"
- When: Alice deletes her account
- Then: "Planning" is removed from the event repository
- And: Alice no longer appears as an invitee for "Demo"
