**Title** : Reject Invite  
**ID** : 0  
**As a...** User  
**I want to ...** Select the time range my event can be
**So that...** I can make sure my event is set at an appropriate time of the day, e.g in the working day

**Acceptance criteria:**  
-[x] User may select a time range when creating an event
- [x] Scheduled event does not go beyond the time range

**Acceptance Test:**  
Test 1: User creates an event
- Given: User is logged in and is creating an event and inputs all required information
- When: Inputs a range of hours of day when the event may happen
- And: User clicks "create event"
- Given: There is a free slot between those hours
- Then: Event is organized for a time between the selected hours