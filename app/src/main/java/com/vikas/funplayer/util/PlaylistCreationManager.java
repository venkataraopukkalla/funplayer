package com.vikas.funplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlaylistCreationManager {

    private static final String PREF_NAME = "playlist_prefs";
    private static final String KEY_LAST_CREATION_DATE = "last_creation_date";
    private static final String KEY_AD_WATCHED_DATE = "ad_watched_date";

    public static boolean canCreateFreePlaylist(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String today = getTodayDate();
        String lastCreationDate = prefs.getString(KEY_LAST_CREATION_DATE, "");

        return !today.equals(lastCreationDate);
    }

    public static boolean hasWatchedAdToday(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String today = getTodayDate();
        String adWatchedDate = prefs.getString(KEY_AD_WATCHED_DATE, "");

        return today.equals(adWatchedDate);
    }

    public static void onFreePlaylistCreated(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LAST_CREATION_DATE, getTodayDate()).apply();
    }

    public static void onAdWatchedForPlaylist(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_AD_WATCHED_DATE, getTodayDate()).apply();
    }

    public static String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}


