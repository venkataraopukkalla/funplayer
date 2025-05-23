package com.vikas.funplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vikas.funplayer.fragments.MusicFragment;
import com.vikas.funplayer.fragments.VideosFragment;
import com.vikas.funplayer.model.SongsDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Fragment selectFragment;

    // Dynamic permission depending on Android version
    private final String PERMISSION = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ? Manifest.permission.READ_MEDIA_AUDIO
            : Manifest.permission.READ_EXTERNAL_STORAGE;

    private boolean dialogAlreadyShown = false;
    private boolean comingFromSettings = false;


    ArrayList<SongsDetails> songsDetailsList = new ArrayList<>();
    public static final String KEY_VALUE = "VIKAS";


    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavbar);



        // Request Notification Permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }



        if (!hasReadPermission()) {
            requestReadPermission();
        } else {
            loadSongsInBackground();
//            setAllFragments();
        }


        // Initialize AdMob
        MobileAds.initialize(this, initializationStatus -> {});



    }

    private void showNotificationSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Notifications")
                .setMessage("Notifications are disabled for this app. Please enable them from system settings.")
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    // Request notification permission for Android 13+ (API 33+)
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private boolean hasReadPermission() {
        return ActivityCompat.checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
            Toast.makeText(this, "Read permission is required. Please allow it.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, 141);
    }

    private void loadSongsInBackground() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            songsDetailsList.clear();
            int i = 0;

            Uri[] uris = {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            };

            for (Uri contentUri : uris) {
                String[] projection = {
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATE_ADDED,
                        MediaStore.Audio.Media.ALBUM_ID
                };

                String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

                try (Cursor cursor = getContentResolver().query(contentUri, projection, selection, null, sortOrder)) {
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            SongsDetails details = new SongsDetails(
                                    cursor.getString(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3),
                                    i
                            );
                            details.setAlbumId(cursor.getLong(5));

                            if (isValidSong(details.getSongData())) {
                                songsDetailsList.add(details);
                                i++;
                            }
                        }
                    }
                }
            }

            runOnUiThread(this::setAllFragments);
        });
    }

    private boolean isValidSong(String path) {
        return new File(path).exists()
                && path.endsWith(".mp3")
                && !path.toLowerCase().contains("record")
                && !path.toLowerCase().contains("voice")
                && !path.toLowerCase().contains("sound");
    }

    private void setMusicFragment() {
        MusicFragment musicFragment = new MusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_VALUE, songsDetailsList);
        musicFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framlayout, musicFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 141) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dialogAlreadyShown = false;
                loadSongsInBackground();
            } else {
                showPermissionRequiredDialog();
            }
        }
    }

    private void showPermissionRequiredDialog() {
        if (dialogAlreadyShown) return;

        dialogAlreadyShown = true;

        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Permission is required to load all songs. Please enable it in Settings.")
                .setCancelable(false)
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    comingFromSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Exit App", (dialog, which) -> {
                    dialogAlreadyShown = false;
                    finishAffinity(); // Properly closes app (avoids splash)
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Properly closes app (avoids splash)

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if notifications are disabled at system level
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            showNotificationSettingsDialog();
        }
    }





    /**
     * BottomNavigation setup (enable in future updates)
     */

    private void setAllFragments() {
        selectFragment = new MusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_VALUE, songsDetailsList);
        selectFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framlayout, selectFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.videos_nav_menu:
                    selectFragment = new VideosFragment();
                    break;
                case R.id.music_nav_menu:
                    selectFragment = new MusicFragment();
                    Bundle b = new Bundle();
                    b.putParcelableArrayList(KEY_VALUE, songsDetailsList);
                    selectFragment.setArguments(b);
                    break;
            }

            if (selectFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framlayout, selectFragment)
                        .commit();
            }
            return true;
        });
    }




}
