package test;

import Event.Event;
import IcsImporter.IcsImporter;
import IcsImporter.ImportStatus;
import User.User;
import UserCalendar.UserCalendar;
import junit.framework.TestCase;
import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
        assertEquals(Boolean.TRUE, importedEvent.getIsImported());
    }

}
