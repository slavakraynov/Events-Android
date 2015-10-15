package ru.kraynov.app.ssaknitu.events.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;

public class UtilsHelper {
    public static final String SALT = "J84esFo6J";

    /**
     * Convert nameValuePairs params to HashMap
     * @param params
     * @return
     */
    public static HashMap<String, String> getParams(NameValuePair[] params){
        HashMap<String, String> paramsMap = new HashMap<String, String>();

        for (NameValuePair param: params){
            paramsMap.put(param.getName(), param.getValue());
        }

        return paramsMap;
    }

    /**
     * All device info to JSON String
     * @param context
     * @return JSON String
     * @throws org.json.JSONException
     */
    public static String getDeviceInfo(Context context) {
        try {
            HashMap<String, String> mapInfo = new HashMap<String, String>();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            String os = "Android";
            String osVersion = Build.VERSION.RELEASE;
            String deviceName = Build.MODEL;

            mapInfo.put("app_version", pInfo.versionName);
            mapInfo.put("app_build",pInfo.versionCode+"");
            mapInfo.put("locale", Locale.getDefault().getLanguage());
            mapInfo.put("os", os);
            mapInfo.put("os_version", osVersion);
            mapInfo.put("id", imei);
            mapInfo.put("name", deviceName);

            JSONObject deviceInfo = new JSONObject();
            try {
                deviceInfo.put("device", new JSONObject(mapInfo));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return deviceInfo.toString();
        } catch (Exception e){return ""; }
    }

    public static HashMap<String,String> getDeviceInfoMap(Context context) {
        try {
            HashMap<String, String> mapInfo = new HashMap<String, String>();
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            String os = "Android";
            String osVersion = Build.VERSION.RELEASE;
            String deviceName = Build.MODEL;

            mapInfo.put("app_version", pInfo.versionName);
            mapInfo.put("app_build",pInfo.versionCode+"");
            mapInfo.put("locale", Locale.getDefault().getLanguage());
            mapInfo.put("os", os);
            mapInfo.put("os_version", osVersion);
            mapInfo.put("id", imei);
            mapInfo.put("name", deviceName);

            return mapInfo;
        } catch (Exception e){return null; }
    }
}
