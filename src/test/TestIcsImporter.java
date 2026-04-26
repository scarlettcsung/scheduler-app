package test;

import event.*;
import event.manager.EventManager;
import ics.importer.IcsImporter;
import ics.importer.ImportStatus;
import junit.framework.TestCase;
import net.fortuna.ical4j.data.ParserException;
import user.User;
import repository.EventRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Unit tests for {@link ics.importer.IcsImporter}.
 *
 * @author SN AA NJ
 * @version 3
 */
public class TestIcsImporter extends TestCase {

    private static final String SIMPLE_ICS = "src/test/resources/simpleImport.ics";

    private TimeZone originalTimeZone;
    private EventManager eventManager;
    private EventRepository eventRepository;

    protected void setUp() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
        eventManager = new EventManager();
        eventRepository = new EventRepository();
    }

    protected void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    public void testParseICSReturnsImportedEvents() throws IOException, ParserException {
        IcsImporter importer = new IcsImporter();
        importer.setIcsFilePath(SIMPLE_ICS);

        List<Event> importedEvents = importer.parseIcs();

        assertEquals(1, importedEvents.size());

        Event importedEvent = importedEvents.get(0);
        assertEquals("Simple Import", importedEvent.getEventName());
        assertEquals("Fixture event", importedEvent.getEventDescription());
        assertEquals(LocalDateTime.of(2026, 4, 10, 9, 0), importedEvent.getEventTime());
        assertEquals(60, importedEvent.getEventDuration());
        assertTrue(importedEvent.isImported());
    }


    public void testOverwriteImportedEventsKeepsManualEvents() {
        User user = new User("Charles","password");
        eventManager = new EventManager(null, eventRepository);

        Event manualEvent = new CreatedEvent("Manual", 30, "Keep me", null);
        manualEvent.setOrganizer(user.getUsername());
        manualEvent.setEventTime(LocalDateTime.of(2026, 4, 9, 8, 0));
        eventRepository.save(manualEvent);

        Event oldImportedEvent = new ImportedEvent("Old import", 45, "Replace me", null);
        oldImportedEvent.setOrganizer(user.getUsername());
        oldImportedEvent.setEventTime(LocalDateTime.of(2026, 4, 9, 9, 0));
        eventRepository.save(oldImportedEvent);
        
        ImportStatus status = eventManager.importIcs(user, SIMPLE_ICS);
        List<Event> actualEvents = eventRepository.getUserCalendar(user.getUsername());

        assertEquals(ImportStatus.SUCCESS, status);
        assertEquals(2, actualEvents.size());
        assertNotNull(findEvent(actualEvents, "Manual"));
        assertNull(findEvent(actualEvents, "Old import"));
        assertNotNull(findEvent(actualEvents, "Simple Import"));
    }

    public void testImportCalendarReturnsUserNotFoundWhenUserIsNull() {
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(null);
        importer.setIcsFilePath(SIMPLE_ICS);
        importer.runImport();

        assertEquals(ImportStatus.UserNotFound, importer.getLastImportStatus());
    }

    public void testImportCalendarReturnsFileNotFoundWhenFileIsMissing() {
        User user = new User("Charles", "password");
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath("src/test/resources/missing.ics");
        importer.runImport();

        assertEquals(ImportStatus.FileNotFound, importer.getLastImportStatus());
    }

    // test for ics file = null
    public void testImportCalendarReturnsFileNotFoundWhenFileIsNull() {
        User user = new User("Charles", "password");
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath(null);
        importer.runImport();

        assertEquals(ImportStatus.FileNotFound, importer.getLastImportStatus());
    }

    // test end with Z function
    public void testParseICSHandlesZuluTime() throws IOException, ParserException {
        IcsImporter importer = new IcsImporter();
        importer.setIcsFilePath("src/test/resources/utcImport.ics");

        List<Event> events = importer.parseIcs();

        assertEquals(1, events.size());

        Event event = events.get(0);

        // "Z" should be stripped, so it parses as local time
        assertEquals(LocalDateTime.of(2026, 4, 10, 9, 0), event.getEventTime());
        assertEquals("UTC Event", event.getEventName());
    }

    public void testImportInvalidIcs() {
        User user = new User("Charles", "password");
        eventManager = new EventManager(null, eventRepository);

        Event existingEvent = new CreatedEvent("Meeting", 60, "Stay", null);
        existingEvent.setOrganizer(user.getUsername());
        eventRepository.save(existingEvent);

        ImportStatus status = eventManager.importIcs(user, "src/test/resources/invalid.ics");

        assertNull(status);
        
        List<Event> actualEvents = eventRepository.getUserCalendar(user.getUsername());
        assertEquals(1, actualEvents.size());
        assertEquals("Meeting", actualEvents.get(0).getEventName());
    }

    private Event findEvent(List<Event> events, String eventName) {
        for (Event event : events) {
            if (eventName.equals(event.getEventName())) {
                return event;
            }
        }

        return null;
    }
    
    public void testGetTargetUserReturnsSetUser() {
        User user = new User("Charles", "password");
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
 
        assertEquals(user, importer.getTargetUser());
    }
 
    public void testGetTargetUserReturnsNullWhenNotSet() {
        IcsImporter importer = new IcsImporter();
 
        assertNull(importer.getTargetUser());
    }
 
    public void testGetIcsFilePathReturnsSetPath() {
        IcsImporter importer = new IcsImporter();
        importer.setIcsFilePath(SIMPLE_ICS);
 
        assertEquals(SIMPLE_ICS, importer.getIcsFilePath());
    }
 
    public void testGetIcsFilePathReturnsNullWhenNotSet() {
        IcsImporter importer = new IcsImporter();
 
        assertNull(importer.getIcsFilePath());
    }
 
    public void testGetLastImportedEventsReturnsEventsAfterImport() {
        User user = new User("Charles", "password");
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath(SIMPLE_ICS);
        importer.runImport();
 
        List<Event> events = importer.getLastImportedEvents();
        assertNotNull(events);
        assertEquals(1, events.size());
    }
 
    public void testGetLastImportedEventsReturnsNullBeforeImport() {
        IcsImporter importer = new IcsImporter();
 
        assertNull(importer.getLastImportedEvents());
    }
}