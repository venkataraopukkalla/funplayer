package com.vikas.funplayer.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.vikas.funplayer.model.Playlist;
import com.vikas.funplayer.model.PlaylistSongCrossRef;
import com.vikas.funplayer.model.PlaylistWithSongs;
import com.vikas.funplayer.model.Song;
import com.vikas.funplayer.model.SongWithPlaylists;

import java.util.List;

@Dao
public interface MusicDao {
    @Insert
    long insertPlaylist(Playlist playlist);

    @Insert
    long insertSong(Song song);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlaylistSongCrossRef(PlaylistSongCrossRef crossRef);

    // debug helper: see whether cross-refs actually exist
    @Query("SELECT * FROM PlaylistSongCrossRef WHERE playlistId = :pId")
    List<PlaylistSongCrossRef> getCrossRefsForPlaylist(long pId);

    @Transaction
    @Query("SELECT * FROM Playlist")
    List<PlaylistWithSongs> getPlaylistsWithSongs();

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    PlaylistWithSongs getPlaylistWithSongs(long playlistId);

    @Transaction
    @Query("SELECT * FROM Song")
    List<SongWithPlaylists> getSongsWithPlaylists();

    @Query("SELECT * FROM Playlist")
    List<Playlist> getAllPlaylists();

    @Query("SELECT name FROM Playlist")
    List<String> getAllPlaylistNames();

    @Query("SELECT playlistId FROM Playlist WHERE name = :name LIMIT 1")
    long getPlaylistIdByName(String name);

    @Query("SELECT songId FROM Song WHERE data = :songData LIMIT 1")
    long getSongIdByData(String songData);

    @Query("SELECT COUNT(*) FROM PlaylistSongCrossRef WHERE playlistId = :playlistId AND songId = :songId")
    int isSongInPlaylist(long playlistId, long songId);
}
