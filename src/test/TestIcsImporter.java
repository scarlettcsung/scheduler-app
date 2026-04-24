package test;

import event.*;
import ics.importer.IcsImporter;
import ics.importer.ImportStatus;
import junit.framework.TestCase;
import net.fortuna.ical4j.data.ParserException;
import user.User;
import user.calendar.UserCalendar;

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

    protected void setUp() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Amsterdam"));
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

    public void testImportCalendarCreatesCalendarAndSetsOrganizer() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath(SIMPLE_ICS);
        importer.runImport();

        assertEquals(ImportStatus.Succes, importer.getLastImportStatus());
        assertNotNull(user.getCalendar());
        assertEquals(1, user.getCalendar().getEvents().size());

        Event importedEvent = user.getCalendar().getEvents().get(0);
        assertEquals("Charles", importedEvent.getOrganizer());
        assertTrue(importedEvent.isImported());
    }

    public void testOverwriteImportedEventsKeepsManualEvents() {
        UserCalendar calendar = new UserCalendar(new ArrayList<>());

        Event manualEvent = new CreatedEvent("Manual", 30, "Keep me", "Charles", null);
        manualEvent.setEventTime(LocalDateTime.of(2026, 4, 9, 8, 0));
        calendar.addEvent(manualEvent);

        Event oldImportedEvent = new ImportedEvent("Old import", 45, "Replace me", "Charles", null);
        oldImportedEvent.setEventTime(LocalDateTime.of(2026, 4, 9, 9, 0));
        calendar.addEvent(oldImportedEvent);

        User user = new User("Charles", "password", calendar);
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath(SIMPLE_ICS);
        importer.runImport();

        assertEquals(ImportStatus.Succes, importer.getLastImportStatus());
        assertEquals(2, calendar.getEvents().size());
        assertNotNull(findEvent(calendar.getEvents(), "Manual"));
        assertNull(findEvent(calendar.getEvents(), "Old import"));

        Event importedEvent = findEvent(calendar.getEvents(), "Simple Import");
        assertNotNull(importedEvent);
        assertTrue(importedEvent.isImported());
    }

    public void testImportCalendarReturnsUserNotFoundWhenUserIsNull() {
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(null);
        importer.setIcsFilePath(SIMPLE_ICS);
        importer.runImport();

        assertEquals(ImportStatus.UserNotFound, importer.getLastImportStatus());
    }

    public void testImportCalendarReturnsFileNotFoundWhenFileIsMissing() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath("src/test/resources/missing.ics");
        importer.runImport();

        assertEquals(ImportStatus.FileNotFound, importer.getLastImportStatus());
    }

    // test for ics file = null
    public void testImportCalendarReturnsFileNotFoundWhenFileIsNull() {
        User user = new User("Charles", "password", null);
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

    public void testImportCalendarThrowsIllegalStateOnInvalidICS() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();
        importer.setTargetUser(user);
        importer.setIcsFilePath("src/test/resources/invalid.ics");

        try {
            importer.runImport();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Calendar import failed", e.getMessage());
            assertTrue(e.getCause() instanceof ParserException);
        }
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
        User user = new User("Charles", "password", null);
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
        User user = new User("Charles", "password", null);
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