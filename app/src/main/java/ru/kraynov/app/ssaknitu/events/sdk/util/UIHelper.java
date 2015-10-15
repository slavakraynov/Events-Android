package ru.kraynov.app.ssaknitu.events.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import ru.kraynov.app.ssaknitu.events.sdk.SDK;

public class UIHelper {
    private static Activity sTopActivity;
    private static Context sApplicationContext;

    public static Activity getTopActivity()
    {
        return sTopActivity;
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static void onCreate(Activity activity){
        if (sTopActivity != activity)
            sTopActivity = activity;
        if (sApplicationContext == null && activity != null) {
            sApplicationContext = activity.getApplicationContext();
        }
    }

    public static void onDestroy(Activity activity){
        if (sTopActivity == activity)
            sTopActivity = null;
    }

    public static void onResume(Activity activity){
        if (sTopActivity != activity)
            sTopActivity = activity;
        if (sApplicationContext == null && activity != null) {
            sApplicationContext = activity.getApplicationContext();
        }
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data){
        sTopActivity = activity;
        if (requestCode == SDK.SDK_REQUEST_CODE) {
            SDK.getInstance().processActivityResult(resultCode, data);
        }
    }
}
