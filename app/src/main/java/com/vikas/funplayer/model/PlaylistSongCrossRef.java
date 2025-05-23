package com.vikas.funplayer.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"playlistId", "songId"})
public class PlaylistSongCrossRef {
    public long playlistId;
    public long songId;

    public PlaylistSongCrossRef(long playlistId, long songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    // Getters and setters

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }
}
