package IcsImporter;

import Event.*;
import User.*;
import UserCalendar.*;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.*;
import net.fortuna.ical4j.model.parameter.*;
import net.fortuna.ical4j.model.property.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class IcsImporter {

    private static final DateTimeFormatter ICS_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public List<Event> parseICS(String icsFile) throws IOException, ParserException {
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
            Event event = new Event(
                    summary.getValue(),
                    durationMinutes,
                    description.getValue(),
                    null,
                    true,
                    null
            );
            event.setEventTime(startTime);
            importedEvents.add(event);
        }

        return importedEvents;
    }

}
