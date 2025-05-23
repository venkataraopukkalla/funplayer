package com.vikas.funplayer.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    public long playlistId;

    @NonNull
    public String name;

    // Optional: constructor
    public Playlist(@NonNull String name) {
        this.name = name;
    }

    // Optional: getter and setter methods (good practice for Room)
    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
