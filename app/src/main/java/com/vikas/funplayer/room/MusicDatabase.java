package com.vikas.funplayer.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vikas.funplayer.model.Playlist;
import com.vikas.funplayer.model.PlaylistSongCrossRef;
import com.vikas.funplayer.model.Song;

@Database(entities = {Playlist.class, Song.class, PlaylistSongCrossRef.class}, version = 1)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract MusicDao musicDao();
}

