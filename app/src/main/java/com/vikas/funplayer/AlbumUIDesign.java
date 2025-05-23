package com.vikas.funplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.vikas.funplayer.Adpaters.AlbumUIAdpter;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AdUtils;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.ConstraintlayoutSetBackGroundColor;
import com.vikas.funplayer.util.NetworkUtils;
import com.vikas.funplayer.util.RemoteConfigManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlbumUIDesign extends AppCompatActivity {

    private ImageView albumUiImg,albumUIfullscreen;
    private RecyclerView albumUIRecycle;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_uidesign);
        albumUiImg=findViewById(R.id.albumUIImage);
        albumUIRecycle=findViewById(R.id.albumUIRecycleview);
        constraintLayout=findViewById(R.id.constraintLayoutAlbumUI);

        findViewById(R.id.back_option_img).setOnClickListener(e->onBackPressed());


        List<SongsDetails> albumSongs =(List<SongsDetails>)getIntent().getSerializableExtra("KEY_VALUE");


        if(albumSongs.size()>0){
            byte[] albumArt = AlbumArt.getAlbumArt(albumSongs.get(0).getSongData());
            if(albumArt!=null){
                Glide.with(this).asBitmap().load(albumArt).into(albumUiImg);


            }else {
                Glide.with(this).asBitmap()
                        .load(R.drawable.music_logo_light).into(albumUiImg);
            }
            // for background color of constartLayout

            try {
                ConstraintlayoutSetBackGroundColor.setBackGroundColorFromGlide(this,albumArt,constraintLayout);
            }catch (Exception e){
                e.printStackTrace();
            }



            albumUIRecycle.setLayoutManager(new LinearLayoutManager(this));
            albumUIRecycle.setAdapter(new AlbumUIAdpter(albumSongs,this));
        }


        String bannerAdUnitId = RemoteConfigManager.getString(this, "banner_ad_unit_id", getString(R.string.test_banner_ad_unit)); // test fallback
        boolean showAds = RemoteConfigManager.getBoolean(this, "show_ads", true);


        // Load the Adaptive Banner Ad
        if(NetworkUtils.isNetworkAvailable(this) && showAds){
            loadAdaptiveBanner(bannerAdUnitId);
        }





    }


    private void loadAdaptiveBanner(String bannerAdUnit) {
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        AdView adView = new AdView(this);
        adContainerView.addView(adView);

        adView.setAdUnitId(bannerAdUnit); // Use your real Ad Unit ID here
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdUtils.loadBannerAd(this, adView);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


}