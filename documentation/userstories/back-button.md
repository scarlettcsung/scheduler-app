**Title** : Back button  
**ID** : 0  
**As a...** user  
**I want to ...** leave event manage screen without saving  
**So that...** it's easy to leave if I no longer need to create or update event  
 
**Acceptance criteria:**  
[x] User should be able to click a button to return to the user panel at any point during event creation/update 
 
**Acceptance Test:**  
Test 1: User is creating an event and backs out halfway
- Given: User is in the create event dialog
- And: User has not completed filling out the dialog form
- When: User clicks "Back"
- Then: User returns to the User Panel

