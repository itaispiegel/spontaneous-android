package com.spontaneous.android.util;

import android.util.Log;

/**
 * Created by Eidan on 4/25/2015.
 */

public class Logger {

    private static final String LOG_TAG = "RideFind";

    private Logger() {

    }

    public static void i(String s) {
        if(s != null) {
            Log.i(LOG_TAG, s);
        }
    }

    public static void d(String s) {
        Log.d(LOG_TAG, s);
    }

    public static void e(String s) {
        Log.e(LOG_TAG, s);
    }

}


