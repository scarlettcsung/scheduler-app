**Title** : register  
**ID** : 0  
**As a...** user  
**I want to ...** register to the system
**So that...** I can be saved to the system and login later on
  
**Acceptance criteria:**  
- A new user may create a new account
- A new user may log event information to this account
- The new account will exist and contain all information after logging out and closing the application
  
**Acceptance Test:**  
Test 1: User registers
- Given: "testregister" does not exist in the UserRepository
- When: User inputs "testregister" as username, then registers with password "testregister"
- Then: User can log in as "testregister" and correct password
  
Test 2: Registered user logs out
- Given: User "testregister" creates an event
- And: User logs out from user panel
- When: User "testregister" logs in again
- Then: User can enter user panel again
- And: User sees their previously made events