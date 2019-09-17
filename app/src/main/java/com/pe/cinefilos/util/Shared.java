package com.pe.cinefilos.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Locale;

public class Shared {
    SharedPreferences pref_data;

    private static final String SHARED_NAME = "WALLETON_SHARED";

    private static final String KEY_IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String KEY_LANGUAGE = "LANGUAGE";
    private static final String KEY_USER = "USER";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_ALIAS = "ALIAS";
    private static final String KEY_TOKEN_FIREBASE = "TOKEN_FIREBASE";

    public Shared(Context context) {
        pref_data = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(String p_user) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_USER, p_user);
        editor_data.commit();
    }

    public String getUser() {
        return pref_data.getString(KEY_USER, null);
    }

    public void setToken(String p_token) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_TOKEN, p_token);
        editor_data.commit();
    }

    public String getToken() {
        return pref_data.getString(KEY_TOKEN, null);
    }

    public void setLanguage(String p_lan) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_LANGUAGE, p_lan);
        editor_data.commit();
    }

    public boolean getIsFirstTime() {
        boolean is_first_time = pref_data.getBoolean(KEY_IS_FIRST_TIME, true);

        if (is_first_time) {
            Editor editor_data = pref_data.edit();
            editor_data.putBoolean(KEY_IS_FIRST_TIME, false);
            editor_data.commit();
        }

        return is_first_time;
    }

    public String getLanguage() {
        String p_lang = pref_data.getString(KEY_LANGUAGE, null);
        if (p_lang == null) {
            p_lang = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
            setLanguage(p_lang);
        }
        return p_lang;
    }

    public void SetAlias(String p_token) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_ALIAS, p_token);
        editor_data.commit();
    }

    public String getAlias() {
        return pref_data.getString(KEY_ALIAS, null);
    }

    public void setTokenFireBase(String p_token_firebase) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_TOKEN_FIREBASE, p_token_firebase);
        editor_data.commit();
    }

    public String getTokenFireBase() {
        return pref_data.getString(KEY_TOKEN_FIREBASE, null);
    }
}
