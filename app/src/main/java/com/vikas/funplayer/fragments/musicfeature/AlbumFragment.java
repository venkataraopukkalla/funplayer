package com.vikas.funplayer.fragments.musicfeature;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vikas.funplayer.Adpaters.AlbumsAdpter;
import com.vikas.funplayer.Adpaters.AllsongsAdapter;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class AlbumFragment extends Fragment {


    private RecyclerView albumsRecycle;
    private ArrayList<SongsDetails> songsArrayList;
    ArrayList<List<SongsDetails>> albumSongsList =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        albumsRecycle=view.findViewById(R.id.albumsRecycleview);
        songsArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        arrangeAlbum();
       // Log.i("VIKASKING",songsArrayList.get(26).toString());
        albumsRecycle.setLayoutManager(new GridLayoutManager(getContext(),2));
        albumsRecycle.setAdapter(new AlbumsAdpter(albumSongsList,getContext()));
        return view ;
    }
    public  static AlbumFragment instance(ArrayList<SongsDetails> songsDetailsArrayList){
        AlbumFragment albumFragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.KEY_VALUE,songsDetailsArrayList);
        albumFragment.setArguments(bundle);
        return  albumFragment;
    }
    @SuppressLint("NewApi")
    private void arrangeAlbum(){
        Map<String , List<SongsDetails>>albumSongMap= new HashMap<>();
       // Log.i("VIKASKING",songsArrayList.toString());
        for(int i=0;i<songsArrayList.size();i++){

            SongsDetails songsDetails = songsArrayList.get(i);
            addSong(albumSongMap,albumArtlistName(songsDetails),songsDetails);
        }

        for (Map.Entry<String, List<SongsDetails>> entry : albumSongMap.entrySet()) {
            String movieName = entry.getKey();
            List<SongsDetails> songs = entry.getValue();

            albumSongsList.add(songs);


        }



    }
    private  String albumArtlistName(SongsDetails currentSongDetails){
        try {
            // Load the song file
            File songFile = new File(currentSongDetails.getSongData());
            AudioFile audioFile = AudioFileIO.read(songFile);

            // Get the movie name from the metadata
            Tag tag = audioFile.getTag();
            String artistname = tag.getFirst(FieldKey.ALBUM_ARTIST);
            if(artistname.isEmpty() || artistname.equals(null))
                artistname="unknown";

            return artistname;

        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();

        }
        return  null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void addSong(Map<String, List<SongsDetails>> movieSongsMap, String artistName, SongsDetails songName) {
        List<SongsDetails> songs = movieSongsMap.getOrDefault(artistName, new ArrayList<>());
        songs.add(songName);
        movieSongsMap.put(artistName, songs);
    }

}