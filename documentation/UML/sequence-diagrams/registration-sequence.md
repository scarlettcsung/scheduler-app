sequenceDiagram
    actor U as User
    participant AP as AuthenticationPanel
    participant US as UserService
    participant UR as UserRepository

    U->>AP: Click "Register"
    AP->>US: registerUser(username, password)
    US->>UR: isExistingUser(username)
    UR-->>US: true | false

    alt Username already exists
        US-->>AP: false
        AP-->>U: Show "User already exists!"
    else Username is available
        US->>US: Create User + UserCalendar
        US->>UR: saveUser(newUser)
        US-->>AP: true
        AP-->>U: Show "User registered!"
    end