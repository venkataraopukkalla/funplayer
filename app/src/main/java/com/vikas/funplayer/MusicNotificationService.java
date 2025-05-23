package com.vikas.funplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.MusicConstants;
import com.vikas.funplayer.util.MyMusic;

import java.io.IOException;

public class MusicNotificationService extends Service {
    public static final String CHANNEL_ID = "music_channel";
    private static final int NOTIF_ID = 1;

    public static final String ACTION_PREVIOUS = "com.vikas.funplayer.ACTION_PREVIOUS";
    public static final String ACTION_PLAY    = "com.vikas.funplayer.ACTION_PLAY";
    public static final String ACTION_NEXT    = "com.vikas.funplayer.ACTION_NEXT";

    public static final String ACTION_STOP = "com.vikas.funplayer.ACTION_STOP";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Ensure the service doesn't crash while resolving song info
        Notification fallback = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Loading...")
                .setContentText("Preparing music player")
                .setSmallIcon(R.drawable.music_logo_light)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        // 🔐 Always call startForeground immediately
        startForeground(NOTIF_ID, fallback);

        // Now update with real notification
        Notification notification = buildNotification();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, notification);

        // Handle any action
        handleIntentAction(intent);

        return START_STICKY;
    }

    private Notification buildNotification() {

        SongsDetails song=null;
        // Grab the current song
        if (MyMusic.keyValue != null &&
                MyMusic.currentSongNumber >= 0 &&
                MyMusic.currentSongNumber < MyMusic.keyValue.size()) {

             song= MyMusic.keyValue.get(MyMusic.currentSongNumber);
            // Build notification with this song info

        }

        if (song == null) {
            // No valid song, stop the service or show a default notification
            stopSelf();  // Optional: stop service if there's no valid song
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("No Song Playing")
                    .setContentText("Please select a song.")
                    .setSmallIcon(R.drawable.music_logo_light)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
        }

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 10, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        // PendingIntents that re-launch this service with the given action:
        PendingIntent prev = PendingIntent.getService(
                this, 0,
                new Intent(this, MusicNotificationService.class)
                        .setAction(ACTION_PREVIOUS),
                PendingIntent.FLAG_IMMUTABLE
        );
        PendingIntent play = PendingIntent.getService(
                this, 1,
                new Intent(this, MusicNotificationService.class)
                        .setAction(ACTION_PLAY),
                PendingIntent.FLAG_IMMUTABLE
        );
        PendingIntent next = PendingIntent.getService(
                this, 2,
                new Intent(this, MusicNotificationService.class)
                        .setAction(ACTION_NEXT),
                PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent stop = PendingIntent.getService(
                this, 3,
                new Intent(this, MusicNotificationService.class)
                        .setAction(ACTION_STOP),
                PendingIntent.FLAG_IMMUTABLE
        );


        boolean isPlaying = MyMusic.instanceOfMyMusic().isPlaying();
        int playIcon = isPlaying
                ? R.drawable.pause_img
                : R.drawable.play_img;
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(song.getSongTitle())
                .setContentText(song.getSongArtist())
                .setSmallIcon(R.drawable.music_logo_light)
                .addAction(R.drawable.nav_back_img, "Prev", prev)
                .addAction(playIcon,                "Play/Pause", play)
                .addAction(R.drawable.nav_forword_img, "Next", next)
                // text‐only close button:
                .addAction(R.drawable.remove,                  "Close", stop)
                .setContentIntent(contentIntent)  // 👈 Handle notification tap

                .setStyle(new MediaStyle()
                        .setShowActionsInCompactView(0,1,2))
                .setOnlyAlertOnce(true)
                .setOngoing(isPlaying)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

    }

    private void handleIntentAction(Intent intent) {

        if (intent == null || intent.getAction() == null) return;

        boolean wasPause = false, wasPlay = false;

        switch (intent.getAction()) {
            case ACTION_PREVIOUS:
                if (MusicPlayDesign.musicControlReference != null) {
                    MusicPlayDesign.musicControlReference.playPreviousSong();
                }

                break;

            case ACTION_PLAY:
                if (MyMusic.instanceOfMyMusic().isPlaying()) {
                    MyMusic.instanceOfMyMusic().pause();
                    wasPause = true;
                } else {
                    MyMusic.instanceOfMyMusic().start();
                    wasPlay = true;
                }

                MusicPlayDesign.updateUI();

                break;

            case ACTION_NEXT:
                if (MusicPlayDesign.musicControlReference != null) {
                    MusicPlayDesign.musicControlReference.playNextSong();
                }

                break;

            case ACTION_STOP:
                if (MyMusic.instanceOfMyMusic().isPlaying()) {
                    MyMusic.instanceOfMyMusic().pause();
                    wasPause = true;
                }
                stopForeground(true);
                stopSelf();
                return;
        }

        // 🔁 Broadcast change to UI
        if (wasPause || wasPlay) {
            Intent broadcast = new Intent(MusicConstants.ACTION_MEDIA_CONTROL);
            broadcast.putExtra(MusicConstants.EXTRA_CONTROL_TYPE, wasPause
                    ? MusicConstants.CONTROL_PAUSE
                    : MusicConstants.CONTROL_PLAY);
            androidx.localbroadcastmanager.content.LocalBroadcastManager
                    .getInstance(this)
                    .sendBroadcast(broadcast);
        }

        // ✅ Always update notification icon
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, buildNotification());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager mgr = getSystemService(NotificationManager.class);
            mgr.createNotificationChannel(ch);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Not a bound service
    }
}
