package com.pragyaware.anu.solarpack.mUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sarbjit on 06/15/2017.
 */
public class PreferencesUtil {

    private static final String USERNAME = "userName";
    private static final String PROJECT_TYPR = "OfficerProjectType";
    private static PreferencesUtil instance;
    private SharedPreferences prefs;
    private Context appContext;
    private String PREF_USER_CONTACT_ID = "PREF_USER_CONTACT_ID";
    private String PREF_LOGGED_IN = "PREF_LOGGED_IN";
    private String PREF_USER_TYPE = "PREF_USER_TYPE";
    public static final int ENGG = 15;
    public static final int APPROVAL = 16;

    public static PreferencesUtil getInstance(Context appContext) {
        if (instance == null) {
            instance = new PreferencesUtil();
            instance.appContext = appContext;

        }
        if (instance.prefs == null) {
            instance.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        }

        return instance;
    }

    private SharedPreferences.Editor getEditor() {
        if (prefs == null) {
            instance.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        }
        return prefs.edit();
    }


    public boolean isLoggedIn() {
        return instance.prefs.getBoolean(PREF_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isForce) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(PREF_LOGGED_IN, isForce); // value to store
        editor.commit();
    }

    private int getUserType() {
        return instance.prefs.getInt(PREF_USER_TYPE, 0);
    }

    public void setUserType(int userType) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(PREF_USER_TYPE, userType); // value to store
        editor.commit();
    }

    public String getUserContactId() {
        return instance.prefs.getString(PREF_USER_CONTACT_ID, "0");

    }

    public void setUserContactId(String contactId) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(PREF_USER_CONTACT_ID, contactId); // value to store
        editor.commit();
    }

    public String getUsername() {
        return instance.prefs.getString(USERNAME, "");
    }

    public void setUsername(String name) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(USERNAME, name); // value to store
        editor.commit();
    }

    public String getProjectType(){
        return instance.prefs.getString(PROJECT_TYPR, "");
    }

    public void setProjectTypr(String designation){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(PROJECT_TYPR, designation); // value to store
        editor.commit();
    }

    public boolean isOfficer() {
        int userType = getUserType();
        return userType == ENGG;
    }

}
