**Title** : Save Events  
**ID** : 13  
**As a...** user  
**I want to ...** See my events after closing the application
**So that...** I can refer to my events even after the application closes
 
**Acceptance criteria:**  
[x] Events remain after logging out
[x] Events remain after closing application
 
**Acceptance Test:**  
Test 1: User logs out
- Given: User has made an event
- When: User logs out
- And: User logs back in
- Then: Created events still exist in user panel

Test 2: User closes application
- Given: User has made an event
- When: User closes application
- And: User opens application and logs back in 
- Then: Created events still exist in user panel

