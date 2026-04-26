**Title** : Date picker  
**ID** : 0  
**As a...** user  
**I want to ...** pick a date using a calendar  
**So that...** I don't need to type the date in manually  
 
**Acceptance criteria:**  
[x] User should be able to view a calendar when choosing a date
[x] User should be able to click on a date on the calendar to choose said date
[x] The date should be translated correctly to the input box
 
**Acceptance Test:**  
Test 1: User chooses a date
- Given: User is in the create event dialog to create an event
- And: User has filled in all other required input boxes
- When: User clicks a button next to any date inputs
- Then: A calendar pops up
- When: User clicks on a date on the calendar
- Then: The date is translated properly into the input box
- And: The date input does not return errors

