package com.vikas.funplayer.Adpaters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.R;


public class AllsongsViewHolder  extends RecyclerView.ViewHolder {
    public ImageView albumMovieImg;
    public TextView songTitle;

    public  TextView artistName;
  //  public ImageView isItlikedMusicImg;
    public AllsongsViewHolder(@NonNull View itemView) {
        super(itemView);
        albumMovieImg=itemView.findViewById(R.id.setAlbumArt_img);
        songTitle=itemView.findViewById(R.id.songname_txt);
        artistName=itemView.findViewById(R.id.artistname_txt);
    //    isItlikedMusicImg=itemView.findViewById(R.id.isItLike_song_img);
    }
}
