**Title** : logout  
**ID** : 0  
**As a...** user  
**I want to ...** Click the logout button while logged in  
**So that...** Logs the current user out of their account and returns to the login screen  
  
**Acceptance criteria:**  
- User is logged out and returns to login screen after clicking "logout"
- After logging out, user can no longer interact with the user Panel
- After logging out, someone else may log in
  
**Acceptance Test:**  
Test 1: User logs out
- Given: User is logged in and at the User Panel
- When: User clicks the logout button
- Then: User is logged out and only sees login screen, can no longer interact with program
- And: Another user may log in
  