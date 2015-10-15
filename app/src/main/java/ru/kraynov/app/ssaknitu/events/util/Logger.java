package ru.kraynov.app.ssaknitu.events.util;

import android.util.Log;

import ru.kraynov.app.ssaknitu.events.BuildConfig;

public class Logger {
    static final boolean DEBUG = BuildConfig.DEBUG;

    public static void log(String s){
        if (DEBUG) Log.e("events",s);
    }
}
