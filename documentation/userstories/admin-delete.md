**Title** : admin delete 
**ID** : 6  
**As a...** admin  
**I want to ...** block removal of the admin form the delete user options
**So that...** I cant acedentaly remove it self

**Acceptance criteria:**

- Admin user cannot be deleted

**Acceptance Test:**
Given: An admin user is at the delete user screen  

When: user clicks on admin and clicks delete

Then: user receives an error message instead of removing admin user
