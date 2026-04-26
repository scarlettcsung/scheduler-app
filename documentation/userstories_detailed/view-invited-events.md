**Title** : View user's events
**ID** : 23
**As a...** Invited user
**I want to ...** see all invites that I am invited to
**So that...** I can accept/neglect an invite and make an educated decision.
  
**Acceptance criteria:**  
- The system must display a list of invitations to events for the currently logged in user.
- Each invitation must show the name of the event, the person who sent it and the current status of the event.
- The logged in user must be able to accept or decline an invitation via a button.
- When the logged in user responds to an invite, the invite status should update to for example accepted.
  
**Acceptance Test:**  
Given: I am logged in as "Christian" and see an invite to a pending "Work on SmartCalendar" with the option to accept or decline the invite
when: I click the accept or decline the event
then: the invitation status should be updated to "accepted" and added to my own events, or the invite should disappear from my dashboard respectively.