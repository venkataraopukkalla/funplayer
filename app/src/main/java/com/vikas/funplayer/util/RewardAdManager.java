package com.vikas.funplayer.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.vikas.funplayer.R;

public class RewardAdManager {
    private static RewardedAd rewardedAd;
    private static boolean isLoading = false;

    public static void loadAd(Context context, String adUnitId) {
        if (isLoading || rewardedAd != null) return;

        isLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                isLoading = false;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                rewardedAd = null;
                isLoading = false;
                Log.d("RewardAdManager", "Failed to load rewarded ad: " + adError.getMessage());
            }
        });
    }

    public static void showAd(Activity activity, Runnable onAdComplete) {
        if (rewardedAd != null) {
            rewardedAd.show(activity, rewardItem -> {
                onAdComplete.run();
                rewardedAd = null;
                loadAd(activity, activity.getString(R.string.test_reward_ad_unit));
            });
        } else {
            Toast.makeText(activity, "Ad not ready, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}

