package com.bts.adamcrm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
    private static final String MY_APP_PREFERENCES = "adam_app_pref";
    private static SharedPreferencesManager instance;
    private SharedPreferences sharePrefs;

    private SharedPreferencesManager(Context context){
        this.sharePrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context){
        SharedPreferencesManager sharedPreferencesManager;
        synchronized (SharedPreferencesManager.class){
            if (instance == null){
                instance = new SharedPreferencesManager(context);
            }
            sharedPreferencesManager = instance;
        }
        return sharedPreferencesManager;
    }

    public boolean getBooleanValue(String str){
        return this.sharePrefs.getBoolean(str, false);
    }

    public void setBooleanValue(String str, boolean z){
        SharedPreferences.Editor editor = this.sharePrefs.edit();
        editor.putBoolean(str, z);
        editor.apply();
    }

    public String getStringValue(String str){
        return this.sharePrefs.getString(str, "");
    }

    public void setStringValue(String str, String value){
        SharedPreferences.Editor editor = this.sharePrefs.edit();
        editor.putString(str, value);
        editor.apply();
    }
}
