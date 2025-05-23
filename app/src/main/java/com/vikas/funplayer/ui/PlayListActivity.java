package com.vikas.funplayer.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.vikas.funplayer.Adpaters.PlaylistAdapter;
import com.vikas.funplayer.AlbumUIDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.Playlist;
import com.vikas.funplayer.model.PlaylistWithSongs;
import com.vikas.funplayer.model.Song;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.room.MusicDatabaseInstance;
import com.vikas.funplayer.util.AdDisplayManager;
import com.vikas.funplayer.util.AdUtils;
import com.vikas.funplayer.util.NetworkUtils;
import com.vikas.funplayer.util.RemoteConfigManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private TextView noPlaylistMessage;
    private AdView adView;

    // For Interstitial Ads
    private InterstitialAd mInterstitialAd;
    private long lastAdShownTime = 0; // To track last ad show time

    private   String interstitialAdUnitId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        noPlaylistMessage = findViewById(R.id.no_playlist_message_txt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Playlists");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.back_img);
            if (backArrow != null) {
                backArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(backArrow);
            }
        }

        recyclerView = findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadPlaylists();


        String bannerAdUnitId = RemoteConfigManager.getString(this, "banner_ad_unit_id", getString(R.string.test_banner_ad_unit)); // test fallback
        // interstal ad
         interstitialAdUnitId = RemoteConfigManager.getString(this, "interstitial_ad_unit_id", getString(R.string.test_interstitial_ad_unit)); // test fallback

        boolean showAds = RemoteConfigManager.getBoolean(this, "show_ads", true);


        if (NetworkUtils.isNetworkAvailable(this) && showAds) {
            allAdsShow(bannerAdUnitId,interstitialAdUnitId);
        }




    }

    private void allAdsShow(String bannelAdunit,String intersalAdUnit){
        // Add banner ad
        loadAdaptiveBanner(bannelAdunit);

        // Preload interstitial ad
        AdDisplayManager.loadAd(this, intersalAdUnit);

    }

    private void loadAdaptiveBanner(String bannerAdUnit) {
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        adView = new AdView(this);
        adContainerView.addView(adView);

        // Set your real Ad Unit ID here
        adView.setAdUnitId(bannerAdUnit); // <-- Test ID
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        // Load the ad
        AdUtils.loadBannerAd(this, adView);
    }

    private AdSize getAdSize() {
        // Calculate screen width (in dp) for adaptive ad size
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    private void loadPlaylists() {
        executorService.execute(() -> {
            List<Playlist> playlists = MusicDatabaseInstance
                    .getInstance(this)
                    .musicDao()
                    .getAllPlaylists();

            mainHandler.post(() -> {
                if (playlists.isEmpty()) {
                    noPlaylistMessage.setVisibility(View.VISIBLE);
                } else {
                    adapter = new PlaylistAdapter(playlists, this::onPlaylistClicked);
                    recyclerView.setAdapter(adapter);
                }
            });
        });
    }

    private void onPlaylistClicked(Playlist playlist) {
        AdDisplayManager.maybeShowInterstitialAd(this, interstitialAdUnitId, () -> {
            executorService.execute(() -> {
                PlaylistWithSongs pws = MusicDatabaseInstance
                        .getInstance(this)
                        .musicDao()
                        .getPlaylistWithSongs(playlist.playlistId);

                List<Song> songs = (pws != null && pws.songs != null)
                        ? pws.songs
                        : Collections.emptyList();

                ArrayList<SongsDetails> songDetailsList = new ArrayList<>();
                for (Song song : songs) {
                    songDetailsList.add(new SongsDetails(
                            song.getTitle(),
                            song.getDuration(),
                            song.getData(),
                            song.getArtist(),
                            song.getSongNumber()
                    ));
                }

                Intent intent = new Intent(this, AlbumUIDesign.class);
                intent.putExtra("KEY_VALUE", songDetailsList);
                startActivity(intent);
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
