package ru.kraynov.app.ssaknitu.events.util.helper;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.kraynov.app.ssaknitu.events.sdk.api.Api;

import static ru.kraynov.app.ssaknitu.events.util.Logger.log;

public class PushHelper {
    private final static String SENDER_ID = "97243043491";

    GoogleCloudMessaging gcm;
    Activity mActivity;
    private String regid;

    private String getRegistrationId(Context context) {
        String registrationId = SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.PREFS.PUSH_REG_ID);
        if (registrationId.isEmpty()) {
            return "";
        }

        int registeredVersion = SharedPreferencesHelper.getInstance().getInt(SharedPreferencesHelper.PREFS.APP_VERSION, 0);
        int currentVersion = SharedPreferencesHelper.getInstance().getVersionCode();
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    public PushHelper(Activity activity){
        mActivity = activity;

        if (checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(activity);
            regid = getRegistrationId(activity);

            registerInBackground();
        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
             /*   GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show(); */
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask() {

            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mActivity);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend(regid,SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.PREFS.PUSH_REG_ID));
                    storeRegistrationId(regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend(final String newToken, String oldToken) {
        log("new token " + newToken);
/*
        Api.Push().unsubscribe(0, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        */

        Api.Push().subscribe(0, newToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void storeRegistrationId(String regId) {
        int appVersion = SharedPreferencesHelper.getInstance().getVersionCode();
        SharedPreferencesHelper.getInstance().set(SharedPreferencesHelper.PREFS.PUSH_REG_ID, regId);
        SharedPreferencesHelper.getInstance().set(SharedPreferencesHelper.PREFS.APP_VERSION, appVersion);
    }
}
