package com.vikas.funplayer.util;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.vikas.funplayer.R;

public class AdUtils {

    public static void loadBannerAd(Activity activity, AdView adView) {
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    public static void loadInterstitialAd(Context context, InterstitialAdLoadCallback callback) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.test_interstitial_ad_unit), adRequest, callback);
    }
}
