package com.vikas.funplayer.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;

public class MyMusic {

    public static MediaPlayer instances = null;
    public static int currentSongNumber = -1;

    public static ArrayList<SongsDetails> keyValue = null;  // Store song list
    public static String currentSongTitle = "";
    public static String songUri = "";

    private static AudioManager audioManager;
    private static AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

    public static MediaPlayer instanceOfMyMusic() {
        if (instances == null) {
            instances = new MediaPlayer();
        }
        return instances;
    }

    public static boolean requestAudioFocus(Context context) {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        audioFocusChangeListener = focusChange -> {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    pauseMusic(); // Stop completely
                    abandonAudioFocus();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    pauseMusic(); // Pause temporarily
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    resumeMusic(); // Resume playback
                    break;
            }
        };

        int result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
        );

        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public static void abandonAudioFocus() {
        if (audioManager != null && audioFocusChangeListener != null) {
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    public static void playMusic(Context context) {
        if (requestAudioFocus(context)) {
            if (instances != null && !instances.isPlaying()) {
                instances.start();
            }
        }
    }

    public static void pauseMusic() {
        if (instances != null && instances.isPlaying()) {
            instances.pause();
        }
    }

    public static void resumeMusic() {
        if (instances != null && !instances.isPlaying()) {
            instances.start();
        }
    }

    public static void stopMusic() {
        if (instances != null) {
            instances.stop();
            instances.release();
            instances = null;
            abandonAudioFocus();
        }
    }
}
