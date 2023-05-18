package com.vikas.funplayer.Adpaters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vikas.funplayer.fragments.musicfeature.AlbumFragment;
import com.vikas.funplayer.fragments.musicfeature.AllSongsFragment;
import com.vikas.funplayer.fragments.musicfeature.FolderMusicFragment;
import com.vikas.funplayer.model.SongsDetails;

import java.util.ArrayList;

public class MusicViewPageAdapter extends FragmentStateAdapter {

  ArrayList<SongsDetails>songsDetailsArrayList;
    public MusicViewPageAdapter(@NonNull Fragment fragment, ArrayList<SongsDetails> songsDetailsArrayList) {
        super(fragment);
        this.songsDetailsArrayList=songsDetailsArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  AllSongsFragment.instance(songsDetailsArrayList);
            case 1:
                return  new AlbumFragment();

        }
        return  new FolderMusicFragment();
    }

    @Override
    public int getItemCount() {
        return  3;
    }
}


