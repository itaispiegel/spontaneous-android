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

    private static final String DEFAULT_FORMAT = "dd-MM-yyyy, HH:mm";

    public static String format(DateTime date) {
        return DateTimeFormat.forPattern(DEFAULT_FORMAT)
                .print(date);
    }

    public static String format(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
