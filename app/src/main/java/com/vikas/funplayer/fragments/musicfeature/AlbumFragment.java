package com.vikas.funplayer.fragments.musicfeature;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vikas.funplayer.Adpaters.AlbumsAdpter;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumFragment extends Fragment {

    private RecyclerView albumsRecycle;
    private AlbumsAdpter adapter;
    private static final String ARG_SONGS = "arg_songs";

    private final ExecutorService bgExecutor = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        albumsRecycle = view.findViewById(R.id.albumsRecycleview);
        albumsRecycle.setLayoutManager(new GridLayoutManager(getContext(), 2));
        albumsRecycle.setHasFixedSize(true);

        // 1) Set a placeholder adapter so recyclerview can draw immediately
        adapter = new AlbumsAdpter(new ArrayList<>(), getContext());
        albumsRecycle.setAdapter(adapter);

        // 2) Kick off background grouping
        ArrayList<SongsDetails> allSongs =
                getArguments().getParcelableArrayList(ARG_SONGS);
        bgExecutor.execute(() -> {
            List<List<SongsDetails>> grouped = buildAlbumGroups(allSongs);
            uiHandler.post(() -> {
                // 3) Update RecyclerView on UI thread
                adapter.updateData(grouped);
            });
        });

        return view;
    }

    /** Factory to create the fragment and pass in the song list. */
    public static AlbumFragment instance(ArrayList<SongsDetails> songsDetails) {
        AlbumFragment frag = new AlbumFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SONGS, songsDetails);
        frag.setArguments(args);
        return frag;
    }

    /** Runs entirely off the UI thread: groups songs by album name. */
    private List<List<SongsDetails>> buildAlbumGroups(List<SongsDetails> songs) {
        Map<String, List<SongsDetails>> map = new LinkedHashMap<>();
        for (SongsDetails d : songs) {
            String album = extractAlbumName(d);
            if (album == null || album.isEmpty()) album = "Unknown";

            // Manual computeIfAbsent for API < 24:
            List<SongsDetails> list = map.get(album);
            if (list == null) {
                list = new ArrayList<>();
                map.put(album, list);
            }
            list.add(d);
        }
        return new ArrayList<>(map.values());
    }

    /** Extracts the ALBUM metadata; safe to call off the UI thread. */
    private String extractAlbumName(SongsDetails d) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(d.getSongData());
            return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                mmr.release();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
