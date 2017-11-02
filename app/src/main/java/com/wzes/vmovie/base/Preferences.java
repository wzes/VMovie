package com.wzes.vmovie.base;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KRY_LAT = "latitude";
    private static final String KRY_LON = "longitude";
    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }



    private static void saveString(String key, String value) {
        Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    private static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("Demo", 0);
    }
}
