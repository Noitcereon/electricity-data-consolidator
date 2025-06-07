package me.noitcereon.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateConverter {

    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ROOT);

    private DateConverter() {
        // Prevent instantiation.
    }

    /**
     * Converts a String with a date in a specific format to a LocalDate object.
     *
     * @param date A Date in the format yyyy-MM-dd
     * @return The given date as a {@link java.time.LocalDate}
     */
    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DEFAULT_DATE_FORMAT);
    }

    public static String toStringFromLocalDate(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMAT);
    }
}
