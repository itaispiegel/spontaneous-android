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
    public static void info(String message) {
        if (message == null) {
            message = "";
        }

        Log.i(LOG_TAG, message);
    }

    /**
     * Send a debug log message.
     */
    public static void debug(String message) {
        if (message == null) {
            message = "";
        }

        Log.d(LOG_TAG, message);
    }

    /**
     * Send an error log message.
     */
    public static void error(String message) {
        if (message == null) {
            message = "";
        }

        Log.e(LOG_TAG, message);
    }

    /**
     * Send an error log message.
     */
    public static void error(String message, Throwable throwable) {
        if (message == null) {
            message = "";
        }

        Log.e(LOG_TAG, message, throwable);
    }

    /**
     * @return Log tag string
     */
    public static String getTag() {
        return LOG_TAG;
    }
}


