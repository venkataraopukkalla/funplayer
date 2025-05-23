package com.vikas.funplayer.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.Adpaters.AllsongsAdapter;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;

public class PlaylistDetailsActivity extends AppCompatActivity {

    public static final String PLAYLIST_SONGS_KEY = "playlist_songs_key";

    private RecyclerView allSongsRecyclerView;
    private AllsongsAdapter allSongsAdapter;
    private ArrayList<SongsDetails> playlistSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_details);

        // Initialize RecyclerView
        allSongsRecyclerView = findViewById(R.id.allsongs_recycleview);
        allSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get songs passed from previous activity
        playlistSongs = getIntent().getParcelableArrayListExtra(PLAYLIST_SONGS_KEY);

        if (playlistSongs != null && !playlistSongs.isEmpty()) {
            allSongsAdapter = new AllsongsAdapter(playlistSongs, this);
            allSongsRecyclerView.setAdapter(allSongsAdapter);
        } else {
            // Optional: show a message if the list is empty
        }
    }
}
