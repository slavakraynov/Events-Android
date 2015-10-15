package ru.kraynov.app.ssaknitu.events;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ru.kraynov.app.ssaknitu.events.sdk.SDK;
import ru.kraynov.app.ssaknitu.events.util.helper.SharedPreferencesHelper;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SDK.initialize(getApplicationContext());
        SharedPreferencesHelper.initialize(getApplicationContext());
    }
}
