package proguide.walleton.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Locale;

public class Shared {
    SharedPreferences pref_data;
    Context context;

    private static final String SHARED_NAME = "WALLETON_SHARED";

    private static final String KEY_IS_FIRST_TIME = "IS_FIRST_TIME";
    private static final String KEY_LANGUAGE = "LANGUAGE";
    private static final String KEY_USER = "USER";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_ALIAS = "ALIAS";
    private static final String KEY_LOGOFI = "LOGOFI";
    private static final String KEY_IS_SUPERVISOR = "IS_SUPERVISOR";

    public Shared(Context context) {
        this.context = context;
        pref_data = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    public void SetUser(String p_user) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_USER, p_user);
        editor_data.commit();
    }

    public String getUser() {
        return pref_data.getString(KEY_USER, null);
    }

    public void SetToken(String p_token) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_TOKEN, p_token);
        editor_data.commit();
    }

    public String getToken() {
        return pref_data.getString(KEY_TOKEN, null);
    }

    public void SetLanguage(String p_lan) {
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
            SetLanguage(p_lang);
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

    public void SetLogoFI(String p_parameter) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_LOGOFI, p_parameter);
        editor_data.commit();
    }

    public String getLogoFI() {
        return pref_data.getString(KEY_LOGOFI, "");
    }

    public void SetIsSupervisor(String p_parameter) {
        Editor editor_data = pref_data.edit();
        editor_data.putString(KEY_IS_SUPERVISOR, p_parameter);
        editor_data.commit();
    }

    public String getIsSupervisor() {
        return pref_data.getString(KEY_IS_SUPERVISOR, "");
    }
}
