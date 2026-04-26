package ics.importer;

import event.*;
import invite.Invite;
import invite.Role;

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
            this.lastImportedEvents = parseIcs();

            // Imported events should belong to the user who initiated the import.
            for (Event event : this.lastImportedEvents) {
                Invite organizerInvite = new Invite(this.targetUser.getUsername(), event.getEventId(), Role.ORGANIZER);
                event.getInvites().add(organizerInvite);
            }
            this.lastImportStatus = ImportStatus.SUCCESS;
        } catch (FileNotFoundException e) {
            this.lastImportStatus = ImportStatus.FileNotFound;
        } catch (Exception e) {
        	System.err.println("Import failed: " + e.getMessage());
            // Log or handle parsing errors without crashing the whole application
            this.lastImportStatus = null; // Signal internal error or parsing failure
            e.printStackTrace();
        }
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
            
            // Safe property access
            DateProperty startProperty = calendarEvent.getStartDate();
            if (startProperty == null) continue;
            
            String startValue = startProperty.getValue();
            // Handle basic all-day events by adding a default time
            if (!startValue.contains("T") && startValue.length() == 8) {
                startValue += "T000000";
            }
            
            // All timestamps are treated as Amsterdam local time; timezone suffixes are ignored.
            if (startValue.endsWith("Z")) {
                startValue = startValue.substring(0, startValue.length() - 1);
            }
            
            LocalDateTime startTime;
            try {
                startTime = LocalDateTime.parse(startValue, ICS_DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                continue; // Skip malformed dates
            }

            DtEnd endProperty = calendarEvent.getEndDate();
            LocalDateTime endTime = null;
            int durationMinutes = 60; // Default duration

            if (endProperty != null) {
                String endValue = endProperty.getValue();
                if (!endValue.contains("T") && endValue.length() == 8) {
                    endValue += "T000000";
                }
                if (endValue.endsWith("Z")) {
                    endValue = endValue.substring(0, endValue.length() - 1);
                }
                try {
                    endTime = LocalDateTime.parse(endValue, ICS_DATE_TIME_FORMAT);
                    durationMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
                } catch (DateTimeParseException e) {
                    // fallback to default duration
                }
            }

            Summary summary = calendarEvent.getSummary();
            String summaryText = (summary != null) ? summary.getValue() : "Imported Event";
            
            Description description = calendarEvent.getDescription();
            String descriptionText = (description != null) ? description.getValue() : "";

            Event event = new ImportedEvent(
                    summaryText,
                    durationMinutes,
                    descriptionText,
                    null
            );
            event.setEventTime(startTime);
            importedEvents.add(event);
        }

        return importedEvents;
    }
}