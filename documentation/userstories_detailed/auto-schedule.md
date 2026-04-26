**Title** : Auto-schedule  
**ID** : 4  
**As a...** user  
**I want to ...** automatically find a time slot based on all participants' availability   
**So that...** I don't have to manually coordinate meeting times

**Acceptance criteria:**  
- [x] Creating an event in the application schedules a time for event if possible
- [x] Created event does not overlap with other events

**Acceptance Test:**  
(Most of the functionalities are already tested by unittesting)
Test 1: User creates event
- Given: User has gaps in their schedule, as do all invitees, and there are overlaps in availabilities
- When: User creates an event with invitees
- Then: Event is scheduled, with a time and date
