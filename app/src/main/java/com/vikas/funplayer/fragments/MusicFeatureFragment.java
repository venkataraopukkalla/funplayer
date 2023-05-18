package com.vikas.funplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.vikas.funplayer.Adpaters.MusicViewPageAdapter;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.R;
import com.vikas.funplayer.fragments.musicfeature.AlbumFragment;
import com.vikas.funplayer.fragments.musicfeature.AllSongsFragment;
import com.vikas.funplayer.fragments.musicfeature.FolderMusicFragment;
import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFeatureFragment#} factory method to
 * create an instance of this fragment.
 */
public class MusicFeatureFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 musicViewPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music_feature, container, false);
        // Inflate the layout for this fragment
        tabLayout=view.findViewById(R.id.music_tablayoutList);
        musicViewPage=view.findViewById(R.id.viewPages_musicfragment);
        musicViewPage.setAdapter(new MusicViewPageAdapter(this,getArguments().getParcelableArrayList(MainActivity.KEY_VALUE)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                musicViewPage.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        return  view;
    }
    public  static MusicFeatureFragment instance(ArrayList<SongsDetails> songsDetailsArrayList){
        MusicFeatureFragment musicFeatureFragment = new MusicFeatureFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.KEY_VALUE,songsDetailsArrayList);
        musicFeatureFragment.setArguments(bundle);
        return  musicFeatureFragment;
    }
}