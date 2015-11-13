package com.spontaneous.android.util;

import android.util.Log;

/**
 * Created by Eidan on 4/25/2015.
 */

public class Logger {

    private static final String LOG_TAG = "RideFind";

    private Logger() {

    }

    /**
     * Send an info log message.
     */
    public static void info(String s) {
        if(s != null) {
            Log.i(LOG_TAG, s);
        }
    }

    /**
     * Send a debug log message.
     */
    public static void debug(String s) {
        Log.d(LOG_TAG, s);
    }

    /**
     * Send an error log message.
     */
    public static void error(String s) {
        Log.e(LOG_TAG, s);
    }

}


