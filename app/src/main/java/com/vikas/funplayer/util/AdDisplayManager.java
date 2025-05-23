package com.vikas.funplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Calendar;

public class AdDisplayManager {

    private static final String PREF_NAME = "ad_prefs";
    private static final String KEY_LAST_AD_TIME = "last_ad_time";
    private static final String KEY_ADS_SHOWN_TODAY = "ads_shown_today";
    private static final String KEY_LAST_AD_DATE = "last_ad_date";


    private static final int MAX_ADS_PER_DAY = 5;

    private static InterstitialAd interstitialAd;
    private static boolean isAdLoading = false;




    public interface OnAdFinished {
        void onDone();
    }

    public static void maybeShowInterstitialAd(Activity activity, String adUnitId, OnAdFinished callback) {


        // 5 minutes
       final long MIN_AD_INTERVAL_MS = RemoteConfigManager.getLong(activity, "interstitial_cooldown_period", 8) * 60 * 1000;


        // 🚨 1. First check: Are ads disabled remotely?
        boolean showAds = RemoteConfigManager.getBoolean(activity, "show_ads", true);
        // 🚨 Updated: Skip if ads are disabled remotely
        if (!showAds) {
            if (callback != null) callback.onDone();
            return;
        }

        // Proceed with normal logic...
        if (!NetworkUtils.isNetworkAvailable(activity)) {
            if (callback != null) callback.onDone();
            return;
        }


        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long currentTime = System.currentTimeMillis();
        long lastAdTime = prefs.getLong(KEY_LAST_AD_TIME, 0);
        int adsToday = prefs.getInt(KEY_ADS_SHOWN_TODAY, 0);
        long lastAdDate = prefs.getLong(KEY_LAST_AD_DATE, 0);

        // Reset daily count if date changed
        Calendar today = Calendar.getInstance();
        Calendar lastShown = Calendar.getInstance();
        lastShown.setTimeInMillis(lastAdDate);
        if (today.get(Calendar.DAY_OF_YEAR) != lastShown.get(Calendar.DAY_OF_YEAR)) {
            adsToday = 0;
        }

        // Check conditions
        if ((currentTime - lastAdTime) >= MIN_AD_INTERVAL_MS && adsToday < MAX_ADS_PER_DAY) {
            if (interstitialAd != null) {
                interstitialAd.show(activity);
                updateAdPrefs(prefs, currentTime, adsToday + 1);
                interstitialAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        interstitialAd = null;
                        loadAd(activity, adUnitId); // Preload next
                        if (callback != null) callback.onDone();
                    }
                });
            } else if (!isAdLoading) {
                isAdLoading = true;
                loadAd(activity, adUnitId);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(activity, "Ad is loading...", Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onDone(); // continue anyway
                });
            } else {
                if (callback != null) callback.onDone(); // fallback
            }
        } else {
            if (callback != null) callback.onDone();
        }
    }

    private static void updateAdPrefs(SharedPreferences prefs, long time, int adsToday) {
        prefs.edit()
                .putLong(KEY_LAST_AD_TIME, time)
                .putInt(KEY_ADS_SHOWN_TODAY, adsToday)
                .putLong(KEY_LAST_AD_DATE, time)
                .apply();
    }

    public static void loadAd(Context context, String adUnitId) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd ad) {
                interstitialAd = ad;
                isAdLoading = false;
            }

            @Override
            public void onAdFailedToLoad(com.google.android.gms.ads.LoadAdError adError) {
                interstitialAd = null;
                isAdLoading = false;
            }
        });
    }
}
