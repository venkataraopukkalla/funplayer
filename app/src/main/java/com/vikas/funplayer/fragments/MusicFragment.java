package com.vikas.funplayer.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ArrayList<SongsDetails> parcelableArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        MusicFeatureFragment musicFeatureFragment = MusicFeatureFragment.instance(parcelableArrayList);
        getChildFragmentManager().beginTransaction().replace(R.id.framlayout_musicFeature,musicFeatureFragment).commit();

        
        return inflater.inflate(R.layout.fragment_music, container, false);
    }
}