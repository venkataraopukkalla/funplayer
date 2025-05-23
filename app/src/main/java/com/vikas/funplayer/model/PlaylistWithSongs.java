package com.vikas.funplayer.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaylistWithSongs {
    @Embedded
    public Playlist playlist;

    @Relation(
            parentColumn  = "playlistId",      // PK in Playlist
            entityColumn  = "songId",          // PK in Song
            associateBy   = @Junction(
                    value         = PlaylistSongCrossRef.class,
                    parentColumn  = "playlistId",    // FK column in cross-ref to Playlist
                    entityColumn  = "songId"         // FK column in cross-ref to Song
            )
    )
    public List<Song> songs;
}
