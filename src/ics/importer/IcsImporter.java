package ics.importer;

import event.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

//Additional Packages
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.*;
import net.fortuna.ical4j.model.property.*;
import user.*;
import user.calendar.*;

/**
 * Imports events from ICS calendar files into the application's event model.
 *
 * @author AA SN
 * @version 2
 */
public class IcsImporter {

    private static final DateTimeFormatter ICS_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    /**
     * Imports an ICS file into the given user's calendar.
     *
     * @param user user whose calendar should receive the imported events
     * @param icsFile path to the ICS file to import
     * @return status describing the outcome of the import attempt
     */
    public ImportStatus importCalendar(User user, String icsFile) {
        // Return explicit status codes for the expected invalid input cases.
        if (user == null) {
            return ImportStatus.UserNotFound;
        }

        if (icsFile == null) {
            return ImportStatus.FileNotFound;
        }

        try {
            // Create a calendar on first import so imported events always have a target list.
            if (user.getCalendar() == null) {
                user.setCalendar(new UserCalendar(null));
            }

            List<Event> importedEvents = parseIcs(icsFile);
            // Imported events should belong to the user who initiated the import.
            for (Event event : importedEvents) {
                event.setOrganizer(user.getUsername());
            }

            // Replace old imported events but keep manually created events.
            overwriteImportedEvents(user.getCalendar(), importedEvents);
            return ImportStatus.Succes;
        } catch (FileNotFoundException e) {
            return ImportStatus.FileNotFound;
        } catch (IOException | ParserException e) {
            throw new IllegalStateException("Calendar import failed", e);
        }
    }

    /**
     * Parses the supplied ICS file into application {@link Event} instances.
     *
     * @param icsFile path to the ICS file to parse
     * @return imported events extracted from the file
     * @throws IOException when the file cannot be read
     * @throws ParserException when the ICS content cannot be parsed
     */
    public List<Event> parseIcs(String icsFile) throws IOException, ParserException {
        // Keep parameter parsing minimal: we only need TZID support from our test ICS files.
        CalendarBuilder calendarBuilder = new CalendarBuilder(
                new CalendarParserImpl(),
                () -> Collections.singletonList(new net.fortuna.ical4j.model.parameter.TzId.Factory()),
                new DefaultPropertyFactorySupplier(),
                new DefaultComponentFactorySupplier(),
                TimeZoneRegistryFactory.getInstance().createRegistry()
        );

        net.fortuna.ical4j.model.Calendar calendar;
        try (InputStream inputStream = new FileInputStream(icsFile)) {
            calendar = calendarBuilder.build(inputStream);
        }

        List<Event> importedEvents = new ArrayList<>();
        // Convert each VEVENT entry in the .ics file into our Event model.
        for (Object component : calendar.getComponents(Component.VEVENT)) {
            VEvent calendarEvent = (VEvent) component;
            DateProperty startProperty = calendarEvent.getStartDate();
            String startValue = startProperty.getValue();
            // All timestamps are treated as Amsterdam local time; timezone suffixes are ignored.
            if (startValue.endsWith("Z")) {
                startValue = startValue.substring(0, startValue.length() - 1);
            }
            LocalDateTime startTime = LocalDateTime.parse(startValue, ICS_DATE_TIME_FORMAT);

            DtEnd endProperty = calendarEvent.getEndDate();
            String endValue = endProperty.getValue();
            if (endValue.endsWith("Z")) {
                endValue = endValue.substring(0, endValue.length() - 1);
            }
            LocalDateTime endTime = LocalDateTime.parse(endValue, ICS_DATE_TIME_FORMAT);
            int durationMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);

            Summary summary = calendarEvent.getSummary();
            Description description = calendarEvent.getDescription();
            Event event = new ImportedEvent(
                    summary.getValue(),
                    durationMinutes,
                    description.getValue(),
                    null,
                    null
            );
            event.setEventTime(startTime);
            importedEvents.add(event);
        }

        return importedEvents;
    }

    /**
     * Replaces previously imported events in a calendar with a fresh set of
     * imported events while preserving manually created events.
     *
     * @param calendar calendar to update
     * @param importedEvents events produced by the latest import
     */
    public void overwriteImportedEvents(UserCalendar calendar, List<Event> importedEvents) {
        // Only remove events that came from a previous import.
        calendar.getEvents().removeIf(event -> event.isImported());
        // Then append the latest import result.
        calendar.getEvents().addAll(importedEvents);
    }
}
