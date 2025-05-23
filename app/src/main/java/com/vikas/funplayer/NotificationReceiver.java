package com.vikas.funplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MusicFunctionality player = MusicPlayDesign.musicControlReference;

        if (player == null) return;

        switch (action) {
            case "ACTION_PLAY":
                player.playCurrentSong();
                break;
            case "ACTION_NEXT":
                player.playNextSong();
                break;
            case "ACTION_PREVIOUS":
                player.playPreviousSong();
                break;
        }

        // Update notification (optional)
//        MusicNotificationService.updateNotification(context);
    }
}
