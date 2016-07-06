package com.olebokolo.findmycar.core.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.olebokolo.findmycar.core.app.FindMyCar;

public class SharedPreferencesManager {

    public static final String FIND_MY_CAR = "FindMyCar";
    private final SharedPreferences preferences;

    public SharedPreferencesManager() {
        FindMyCar application = FindMyCar.getInstance();
        preferences = application.getSharedPreferences(FIND_MY_CAR, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
}
