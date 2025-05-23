package com.vikas.funplayer.Adpaters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.R;

public class AlbumUITextViewHolder extends RecyclerView.ViewHolder {
    public TextView albumUiSongNum,albumUISongName;
    public AlbumUITextViewHolder(@NonNull View itemView) {
        super(itemView);
        albumUiSongNum=itemView.findViewById(R.id.albumUiSongNo_txt);
        albumUISongName=itemView.findViewById(R.id.albumUidesignSongName_txt);

    }
}
