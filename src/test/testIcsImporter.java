package test;

import IcsImporter.IcsImporter;
import IcsImporter.ImportStatus;
import User.User;
import UserCalendar.UserCalendar;
import event.*;
import junit.framework.TestCase;
import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Unit tests for {@link IcsImporter.IcsImporter}.
 *
 * @author SN AA NJ
 * @version 2
 */
public class testIcsImporter extends TestCase {

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

        List<Event> importedEvents = importer.parseICS(SIMPLE_ICS);

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

        ImportStatus status = importer.importCalendar(user, SIMPLE_ICS);

        assertEquals(ImportStatus.Succes, status);
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

        ImportStatus status = importer.importCalendar(user, SIMPLE_ICS);

        assertEquals(ImportStatus.Succes, status);
        assertEquals(2, calendar.getEvents().size());
        assertNotNull(findEvent(calendar.getEvents(), "Manual"));
        assertNull(findEvent(calendar.getEvents(), "Old import"));

        Event importedEvent = findEvent(calendar.getEvents(), "Simple Import");
        assertNotNull(importedEvent);
        assertTrue(importedEvent.isImported());
    }

    public void testImportCalendarReturnsUserNotFoundWhenUserIsNull() {
        IcsImporter importer = new IcsImporter();

        ImportStatus status = importer.importCalendar(null, SIMPLE_ICS);

        assertEquals(ImportStatus.UserNotFound, status);
    }

    public void testImportCalendarReturnsFileNotFoundWhenFileIsMissing() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();

        ImportStatus status = importer.importCalendar(user, "src/test/resources/missing.ics");

        assertEquals(ImportStatus.FileNotFound, status);
    }

    private Event findEvent(List<Event> events, String eventName) {
        for (Event event : events) {
            if (eventName.equals(event.getEventName())) {
                return event;
            }
        }

        return null;
    }
    
    //test for ics file = null
    public void testImportCalendarReturnsFileNotFoundWhenFileIsNull() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();

        ImportStatus status = importer.importCalendar(user, null);

        assertEquals(ImportStatus.FileNotFound, status);
    }
    
    //test end with Z function
    public void testParseICSHandlesZuluTime() throws IOException, ParserException {
        IcsImporter importer = new IcsImporter();

        List<Event> events = importer.parseICS("src/test/resources/utcImport.ics");

        assertEquals(1, events.size());

        Event event = events.get(0);

        // "Z" should be stripped, so it parses as local time
        assertEquals(LocalDateTime.of(2026, 4, 10, 9, 0), event.getEventTime());
        assertEquals("UTC Event", event.getEventName());
    }
    
    public void testImportCalendarThrowsIllegalStateOnInvalidICS() {
        User user = new User("Charles", "password", null);
        IcsImporter importer = new IcsImporter();

        try {
        	//these two lines below are red in the test but without them the test is not testing dont know why
            importer.importCalendar(user, "src/test/resources/invalid.ics");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Calendar import failed", e.getMessage());
            assertTrue(e.getCause() instanceof ParserException);
        }
    }
}
