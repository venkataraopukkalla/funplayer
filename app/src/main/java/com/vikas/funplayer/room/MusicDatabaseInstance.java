package com.vikas.funplayer.room;

import android.content.Context;

import androidx.room.Room;

public class MusicDatabaseInstance {

    private static MusicDatabase instance;

    public static synchronized MusicDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MusicDatabase.class, "music-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
