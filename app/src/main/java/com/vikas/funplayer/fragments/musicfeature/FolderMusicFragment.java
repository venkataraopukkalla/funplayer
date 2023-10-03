package com.vikas.funplayer.fragments.musicfeature;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vikas.funplayer.R;

public class FolderMusicFragment extends Fragment {

    private RecyclerView playListRecycleView;
    private FloatingActionButton playListActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder_music, container, false);
        playListRecycleView=view.findViewById(R.id.playlistRecycleview);
        playListActionButton=view.findViewById(R.id.float_btn);

        playListActionButton.setOnClickListener(e->{
            cumAlert();
        });


        return view;
    }
    private void cumAlert(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cum_alert_dialog, null);
        TextInputEditText editText =view.findViewById(R.id.playList_textInputEditText);
        AlertDialog materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(requireContext()).setTitle("Playlist")
                        .setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();

        materialAlertDialogBuilder.show();



    }

}