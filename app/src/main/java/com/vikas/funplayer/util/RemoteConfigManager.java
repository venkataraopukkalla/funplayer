package com.vikas.funplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class RemoteConfigManager {

    private static final String PREF_NAME = "remote_config_prefs";

    public static void saveRemoteValues(Context context, FirebaseRemoteConfig config) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean("show_ads", config.getBoolean("show_ads"));
        editor.putString("banner_ad_unit_id", config.getString("banner_ad_unit_id"));
        editor.putString("interstitial_ad_unit_id", config.getString("interstitial_ad_unit_id"));
        editor.putString("rewarded_ad_unit_id", config.getString("rewarded_ad_unit_id"));
        editor.putLong("interstitial_cooldown_period", config.getLong("interstitial_cooldown_period"));
        editor.putLong("max_rewarded_ads_per_day", config.getLong("max_rewarded_ads_per_day"));
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

}
