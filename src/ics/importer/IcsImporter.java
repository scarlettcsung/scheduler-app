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
 * Holds import configuration and results as instance state so that all methods
 * are either accessors or mutators.
 *
 * @author AA SN NJ EO
 * @version 3
 */
public class IcsImporter {

    private static final DateTimeFormatter ICS_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    // ── Instance state ────────────────────────────────────────────────────────

    /** The user whose calendar will receive the imported events. */
    private User targetUser;

    /** Path to the ICS file to be imported. */
    private String icsFilePath;

    /** Events produced by the most recent successful parse. */
    private List<Event> lastImportedEvents;

    /** Outcome of the most recent import attempt. */
    private ImportStatus lastImportStatus;

    // ── Mutators ──────────────────────────────────────────────────────────────

    /**
     * Sets the user whose calendar should receive imported events.
     *
     * @param user target user for the next import
     */
    public void setTargetUser(User user) {
        this.targetUser = user;
    }

    /**
     * Sets the path of the ICS file to import.
     *
     * @param icsFilePath path to the ICS file
     */
    public void setIcsFilePath(String icsFilePath) {
        this.icsFilePath = icsFilePath;
    }

    /**
     * Runs the import using the current {@link #targetUser} and
     * {@link #icsFilePath}. Updates {@link #lastImportedEvents} and
     * {@link #lastImportStatus} with the outcome.
     */
    public void runImport() {
        if (this.targetUser == null) {
            this.lastImportStatus = ImportStatus.UserNotFound;
            return;
        }

        if (this.icsFilePath == null) {
            this.lastImportStatus = ImportStatus.FileNotFound;
            return;
        }

        try {
            // Create a calendar on first import so imported events always have a target list.
            if (this.targetUser.getCalendar() == null) {
                this.targetUser.setCalendar(new UserCalendar(null));
            }

            this.lastImportedEvents = parseIcs();

            // Imported events should belong to the user who initiated the import.
            for (Event event : this.lastImportedEvents) {
                event.setOrganizer(this.targetUser.getUsername());
            }

            // Replace old imported events but keep manually created events.
            overwriteImportedEvents(this.targetUser.getCalendar(), this.lastImportedEvents);
            this.lastImportStatus = ImportStatus.Succes;
        } catch (FileNotFoundException e) {
            this.lastImportStatus = ImportStatus.FileNotFound;
        } catch (IOException | ParserException e) {
            throw new IllegalStateException("Calendar import failed", e);
        }
    }

    /**
     * Replaces previously imported events in a calendar with a fresh set of
     * imported events while preserving manually created events.
     *
     * @param calendar       calendar to update
     * @param importedEvents events produced by the latest import
     */
    public void overwriteImportedEvents(UserCalendar calendar, List<Event> importedEvents) {
        // Only remove events that came from a previous import.
        calendar.getEvents().removeIf(event -> event.isImported());
        // Then append the latest import result.
        calendar.getEvents().addAll(importedEvents);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /**
     * Returns the target user configured for this importer.
     *
     * @return target user, or {@code null} if not set
     */
    public User getTargetUser() {
        return this.targetUser;
    }

    /**
     * Returns the ICS file path configured for this importer.
     *
     * @return ICS file path, or {@code null} if not set
     */
    public String getIcsFilePath() {
        return this.icsFilePath;
    }

    /**
     * Returns the events produced by the most recent import.
     *
     * @return last imported events, or {@code null} if no import has run yet
     */
    public List<Event> getLastImportedEvents() {
        return this.lastImportedEvents;
    }

    /**
     * Returns the status of the most recent import attempt.
     *
     * @return last import status, or {@code null} if no import has run yet
     */
    public ImportStatus getLastImportStatus() {
        return this.lastImportStatus;
    }

    /**
     * Parses the ICS file at {@link #icsFilePath} and returns the resulting
     * events. Does not modify any instance state.
     *
     * @return events extracted from the file
     * @throws IOException     when the file cannot be read
     * @throws ParserException when the ICS content cannot be parsed
     */
    public List<Event> parseIcs() throws IOException, ParserException {
        // Keep parameter parsing minimal: we only need TZID support from our test ICS files.
        CalendarBuilder calendarBuilder = new CalendarBuilder(
                new CalendarParserImpl(),
                () -> Collections.singletonList(new net.fortuna.ical4j.model.parameter.TzId.Factory()),
                new DefaultPropertyFactorySupplier(),
                new DefaultComponentFactorySupplier(),
                TimeZoneRegistryFactory.getInstance().createRegistry()
        );

        net.fortuna.ical4j.model.Calendar calendar;
        try (InputStream inputStream = new FileInputStream(this.icsFilePath)) {
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
}