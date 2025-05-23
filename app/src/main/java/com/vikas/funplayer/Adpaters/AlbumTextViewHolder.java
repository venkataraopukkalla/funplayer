package com.vikas.funplayer.Adpaters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.R;

public class AlbumTextViewHolder extends RecyclerView.ViewHolder {
    public ImageView albumsLogo;
    public TextView albumsName;
    public AlbumTextViewHolder(@NonNull View itemView) {
        super(itemView);
      // albumsName= itemView.findViewById(R.id.albumName_txt);
       albumsLogo=itemView.findViewById(R.id.albumLogoPic_img);
    }
}
