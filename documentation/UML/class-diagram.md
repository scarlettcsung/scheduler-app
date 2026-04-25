```mermaid
classDiagram
    class Authentication {
        -authenticatedUser: User
        -repository: UserRepository
        +Authentication(repository: UserRepository)
        +login(UserName: String, UserPassword: String) : boolean
        +logout() : void
        +getauthenticatedUser() : User
    }

    class Main {
        +main(args: String[]) : void$
    }

    class UserService {
        -userRepository: UserRepository
		-authentication: Authentication
        +UserService(userRepository: UserRepository)
		+registerUser(username: String, password: String) : Boolean
		+authenticateUser(username: String, password: String) : User
		+login(username: String, password: String) : boolean
		+listUsernames() : List<String>
		+deleteUser(username: String, currentUser: User) : UserDeletionResult
		+deleteOwnAccount(currentUser: User) : UserDeletionResult
    }


    class Repository~T~ {
        <<abstract>>
        #List~T~ data
        +Repository()
        +getItemByID(itemID: String) : T
        +save(item: T) : void
        +getAll() : List~T~
        +getRepositoryType() : String
    }

    class EventRepository {
        +EventRepository()
        +getItemByID(eventID: String) : Event
        +deleteItem(eventID: String) : int
        +getRepositoryType() : String
        +deleteEventsByOrganizer(username: String) : void
    }

    class UserRepository {
        -eventRepository: EventRepository
        +saveUser(user: User) : void
        +setEventRepository(eventRepository: EventRepository) : void
        +deleteUserData(username: String, currentUser: User) : UserDeletionResult
        +getItemByID(username: String) : User
        +isExistingUser(username: String) : boolean
        +cleanupUserEventReferences(username: String) : void
        +removeEventFromAllCalendars(event: Event) : void
        +getRepositoryType() : String
    }

    class IO {
        +readUsers(filePath: String) : List~User~
        +writeUsers(userList: List~User~, filePath: String) : void
    }

    class User {
        -username: String
        -password: String
        -myCalendar: Calendar
        +canAccessAdminPanel() : Boolean
        +canDeleteUser() : Boolean
        +setCalendar(calendar: UserCalendar) : void
        +canAccessAdminPanel() : boolean
        +canDeleteUser(targetUser: User) : boolean 
    }
        class AdminUser {
    +canAccessAdminPanel() : boolean
    +canDeleteUser(targetUser: User) : boolean
}
    

    class Scheduler {
        -eventManager: EventManager
        -inviteManager: InviteManager
        +findAvailableSlot(event: Event) : String
        +scheduleEvent(event: Event, calendar: UserCalendar) : Void
    }

    class EventManager {
        -repository: UserRepository
        -eventRepository: EventRepository
        -inviteManager: InviteManager
        +EventManager()
        +EventManager(repository: UserRepository)
        +EventManager(repository: UserRepository, eventRepository: EventRepository)
        +updateEvent(event: Event, updateAspect: String, newValue: String) : void
        +deleteEvent(event: Event) : void
        +rejectInvite(invite: Invite, event: Event) : void
        +setOrganizer(event: Event, organizer: User) : void
        +getOrganizer(event: Event) : User
        +returnParticipatingEvents(username: String, repo: EventRepository) : List~Event~
        +returnOrganisedEvents(username: String, repo: EventRepository) : List~Event~
        +importIcs(user: User, filePath: String) : ImportStatus
    }

    class InviteManager {
        -repository: UserRepository
        +InviteManager(repository: UserRepository)
        +addTemporaryInvite(currentUser: User, event: Event, tempInvites: List~String~, inviteeUsername: String) : String
        +removeInviteFromForm(currentUser: User, event: Event, tempInvites: List~String~, inviteeUsername: String) : String
        +removeInvite(event: Event, recipient: User) : void
        +addInvite(event: Event, recipient: User) : void
        -hasExistingInvite(event: Event, username: String) : boolean
    }

    class UserCalendar {
        -owner: User
        -events: List~Event~
        +addEvent(event: Event) : void
        +removeEvent(event: Event) : void
        +getEvents() : List~Event~
        +getOwner() : User
    }

    class IcsImporter {
        -ICS_DATE_TIME_FORMAT: DateTimeFormatter$
        +importCalendar(user: User, icsFile: String) : ImportStatus
        +parseICS(icsFile: String) : List~Event~
        +overwriteImportedEvents(calendar: UserCalendar, importedEvents: List~Event~) : void
    }

    class Event {
         <<abstract>>
        -eventName: String
        -eventTime: LocalDateTime
        -eventDuration: int
        -eventDescription: String
        -eventID: String 
        -organizerUser: String
        -invites: List~Invite~
        -participantUsernames: List~String~
        #isImportedField: boolean
        
        +isImported(): boolean
        +getParticipants() List~String~
        +hasExistingInvite(String username): boolean
        +getTimeString(): String
    }
    
    class CreatedEvent {
         +isImported() boolean
    }
    
    class ImportedEvent {
        +isImported() boolean
    }

    class Invite {
        -recipientUsername: String
        -eventID: String
        -status: inviteStatus
        -role: Role
        +accept() : void
    }

    class ImportStatus {
        <<enumeration>>
        Succes
        FileNotFound
        UserNotFound
    }

    class inviteStatus {
        <<enumeration>>
        PENDING
        ACCEPTED
        REJECTED
    }

    %% Relationships
    Main ..> IO : uses
    Main ..> UserRepository : uses
    Main ..> EventRepository : uses
    Main ..> Scheduler : uses
    Main ..> User : uses
    Main ..> Event : uses
    Authentication ..> UserRepository : Use
    UserService --> Authentication
    UserService ..> UserRepository
    UserService --> User
    UserRepository o-- User
    UserRepository --> IO : Relationship
    User *-- UserCalendar
    UserCalendar o-- Event
    User --> Invite : recipient
    Event *-- Invite
    CreatedEvent --|> Event
    ImportedEvent --|> Event
    Scheduler ..> User
    Scheduler ..> UserCalendar
    Scheduler ..> Event
    Scheduler --> EventManager
    Scheduler --> InviteManager
    EventManager ..> Event
    EventManager --> InviteManager
    InviteManager ..> User
    InviteManager ..> Event
    InviteManager ..> UserRepository
    IcsImporter --> UserCalendar : Populates
    IcsImporter --> Event : creates imported events
    IcsImporter --> ImportStatus
    Invite --> inviteStatus
    Repository <|-- EventRepository
    Repository <|-- UserRepository
    EventRepository *-- Event
    UserRepository --> EventRepository : uses
    Scheduler --> EventRepository : uses
    EventManager --> EventRepository
    EventManager --> UserRepository
    EventManager ..> IcsImporter : uses
    AdminUser --|> User
```