**Title** : Login  
**ID** : 1  
**As a...** user  
**I want to ...** Log into my account
**So that...** I can view and schedule only my events 
  
**Acceptance criteria:**  
- User must use a correct username and password to Login  
- User gets an error message if username or password is wrong (does not exist, mismatch)  
- User can see the user panel after logging in  
  
**Acceptance Test:**  
Test 1: User logs in with correct information
- Given: User "John" is registered with password "Pork"
- When: User enters the correct information and clicks login
- Then: User can see their User Panel with their events
  
Test 2: Unregistered user tries to login
- Given: User "Helen" does not exist
- When: User enters "Helen" into the username box and clicks login
- Then: User gets an error message saying username or password is incorrect
  
Test 3: User tries logs in with incorrect information
- Given: User "John" is registered with password "Pork"
- When: User enters "John" as username with password "Steak" 
- Then: User gets an error message saying username or password is incorrect