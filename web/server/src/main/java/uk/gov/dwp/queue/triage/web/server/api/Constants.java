package uk.gov.dwp.queue.triage.web.server.api;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public final class Constants {

    private static DateTimeFormatter ISO_DATE_TIME_WITH_MS = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(UTC);

    private Constants() {
    }

    public static String toIsoDateTimeWithMs(Instant instant) {
        return (instant != null) ? Constants.ISO_DATE_TIME_WITH_MS.format(instant) : null;
    }

}
