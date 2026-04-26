**Title** : import calendar
**ID** : 5
**As a...** user
**I want to ...** be able to import my calendar from an external source
**So that...** I can conveniently communicate my availabilities

**Acceptance criteria:**
- The logged-in user can start a calendar import from the user dashboard.
- The import dialog lets the user select an ICS calendar file.
- Events from a valid ICS file are added to the user's calendar as imported events.
- Imported events show the correct title, description, start time, duration, and organizer.
- Existing manually created events stay in the user's calendar after importing.
- Previously imported events are replaced by the events from the latest import.

**Acceptance Test:**
Test 1: User imports a valid external calendar
- Given: user "Charles" is logged in and is on the user dashboard
- And: Charles already has a manually created event called "Manual" in his calendar
- And: Charles has a valid ICS file containing an event called "Simple Import" with description "Fixture event", start time 2026-04-10 09:00, and duration 60 minutes
- When: Charles clicks "Import Calendar" and selects the valid ICS file
- Then: the system shows "Calendar imported successfully!"
- And: "Simple Import" is visible in Charles' event list
- And: the imported event has organizer "Charles"
- And: the manually created event "Manual" is still visible

Test 2: Latest import replaces older imported events
- Given: user "Charles" is logged in and has a previously imported event called "Old import"
- And: Charles has a valid ICS file containing an event called "Simple Import"
- When: Charles imports the valid ICS file
- Then: "Old import" is no longer visible in Charles' event list
- And: "Simple Import" is visible in Charles' event list
