package com.vikas.funplayer.fragments.musicfeature;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vikas.funplayer.Adpaters.AllsongsAdapter;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.MusicFunctionality;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.fragments.MusicFeatureFragment;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.MyMusic;

import java.io.IOException;
import java.util.ArrayList;

public class AllSongsFragment extends Fragment  {

  private RecyclerView allsongRecycleview;

    ArrayList<SongsDetails> songsArrayList;
    private AllsongsAdapter allSongsAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        allsongRecycleview=view.findViewById(R.id.allsongs_recycleview);

        songsArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        allsongRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        allsongRecycleview.setAdapter(new AllsongsAdapter(songsArrayList,getContext()));





        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        allsongRecycleview.setAdapter(new AllsongsAdapter(songsArrayList,getContext()));
    }



    public  static AllSongsFragment instance(ArrayList<SongsDetails> songsDetailsArrayList){
        AllSongsFragment allSongsFragment = new AllSongsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.KEY_VALUE,songsDetailsArrayList);
        allSongsFragment.setArguments(bundle);
        return  allSongsFragment;
    }







}

