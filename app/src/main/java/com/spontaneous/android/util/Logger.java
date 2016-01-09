package com.spontaneous.android.util;

import android.util.Log;

/**
 * This class contains method four logging output.
 */
public abstract class Logger {

    /**
     * Logcat tag name.
     */
    private static final String LOG_TAG = "Spontaneous";

    /**
     * Send an info log message.
     */
    public static void info(String s) {
        if (s == null) {
            s = "";
        }

        Log.i(LOG_TAG, s);
    }

    /**
     * Send a debug log message.
     */
    public static void debug(String s) {
        if (s == null) {
            s = "";
        }

        Log.d(LOG_TAG, s);
    }

    /**
     * Send an error log message.
     */
    public static void error(String s) {
        if (s == null) {
            s = "";
        }

        Log.e(LOG_TAG, s);
    }

    /**
     * @return Log tag string
     */
    public static String getTag() {
        return LOG_TAG;
    }
}


