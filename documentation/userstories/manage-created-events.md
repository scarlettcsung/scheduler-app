**Title** : manage created events  
**ID** : 11  
**As a...** user  
**I want to ...** change the details of the event, like edit dates or participants
**So that...** I can update the event as things change and progress

**Acceptance criteria:**

Only the event creator can edit the event

The user can update date, time, participants, and description

Changes are saved and visible after confirmation

Invalid inputs (e.g. past date) are rejected with an error message

**Acceptance Test:**  
Given: am logged in as the event creator

When: click “Edit event”

Then: see a form pre-filled with current event details

When: update the date and click save

Then: the event is updated and the new date is displayed

