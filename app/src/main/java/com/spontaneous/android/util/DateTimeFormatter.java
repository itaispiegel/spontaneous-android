package com.spontaneous.android.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * This is a util class for formatting date and time.
 */
public class DateTimeFormatter {

    private static final String DEFAULT_FORMAT = "dd-MM-yyyy, H:mm";

    public static String format(DateTime date) {
        return DateTimeFormat.forPattern(DEFAULT_FORMAT).print(date);
    }
}
