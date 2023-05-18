package com.vikas.funplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;


public class VideosFragment extends Fragment {

  TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        textView=view.findViewById(R.id.videosTextview);

        return view;
    }
}