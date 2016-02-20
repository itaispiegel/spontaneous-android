package com.spontaneous.android.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a util class for formatting date and time.
 */
public class DateTimeFormatter {

    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm";

    /**
     * Format a date by the default format.
     * @param date The given date object to format.
     * @return String representation of the given date.
     */
    public static String format(DateTime date) {
        return DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT + ", " + DEFAULT_TIME_FORMAT)
                .print(date);
    }

    /**
     * Format a date by a given format.
     * @param format Format to format by.
     * @param date The given date to format.
     * @return String representation of the given date by the given format.
     */
    public static String format(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
