package ru.kraynov.app.ssaknitu.events.util.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SharedPreferencesHelper {

    public void notifyDataSetChanged() {
        initPreferencesMap();
    }

    // List the names of the preferences
    public enum PREFS {
        PUSH_REG_ID,
        APP_VERSION,
        APP_IS_FIRST_LAUNCH,
        APP_PUSH_EVENTS_ORGS,
        APP_PUSH_NEWS_ORGS,
        APP_PUSH_SOUND,
        PUSH_EVENTS_ENABLE,
        PUSH_POSTS_ENABLE,
        PUSH_NEWS_ENABLE
    };

    private static SharedPreferencesHelper sSharedPreferences;
    private Context mContext;
    private HashMap<String, Object> preferencesMap;

    private SharedPreferencesHelper(Context context){
        mContext = context;
    }

    public static SharedPreferencesHelper getInstance(){
        if (sSharedPreferences==null) throw new NullPointerException("SharedPreferencesHepler must be initialized; Add 'SharedPreferencesHelper.initialize(this);' to Application.class");

        return sSharedPreferences;
    }

    private HashMap<String,Object> getPreferencesMap(){
        if (preferencesMap==null) initPreferencesMap();

        return preferencesMap;
    }

    private void initPreferencesMap(){
        preferencesMap = new HashMap<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferencesMap.putAll(sp.getAll());
    }

    public static SharedPreferencesHelper initialize(Context context){
        sSharedPreferences = new SharedPreferencesHelper(context);

        return sSharedPreferences;
    }

    private void remove(PREFS pref){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().remove(pref.name()).apply();
    }

    public void set(PREFS pref, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = sp.edit();
        e.putString(pref.name(), ""+value);
        e.apply();

        initPreferencesMap();
    }

    public void set(PREFS pref, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = sp.edit();
        e.putString(pref.name(), value);
        e.apply();

        initPreferencesMap();
    }

    public void set(PREFS pref, boolean value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(pref.name(), value);
        e.apply();

        initPreferencesMap();
    }

    public int getInt(PREFS pref, int initial){
        return (getPreferencesMap().get(pref.name())==null) ? initial : Integer.parseInt(String.valueOf(getPreferencesMap().get(pref.name())));
    }

    public float getFloat(PREFS pref, float initial){
        return (getPreferencesMap().get(pref.name())==null) ? initial : Float.parseFloat(String.valueOf(getPreferencesMap().get(pref.name())));
    }

    public String getString(PREFS pref){
        if (getPreferencesMap().get(pref.name())==null) return "";
        return String.valueOf(getPreferencesMap().get(pref.name()));
    }

    public Boolean getBoolean(PREFS pref, Boolean initial){
        return (getPreferencesMap().get(pref.name())==null) ? initial : (Boolean) getPreferencesMap().get(pref.name());
    }

    public int getVersionCode(){
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionCode;
    }
}
