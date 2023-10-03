package com.vikas.funplayer.Adpaters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.MyMusic;

import java.util.ArrayList;
import java.util.List;

public class AllsongsAdapter extends  RecyclerView.Adapter<AllsongsViewHolder>{

    ArrayList<SongsDetails> songsDetailsList=new ArrayList<>();
    Context context;
    int rotation=2;

    public AllsongsAdapter(ArrayList<SongsDetails> songsDetailsList, Context context) {
        this.songsDetailsList = songsDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllsongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_viewholder_design, parent, false);
        return new AllsongsViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AllsongsViewHolder holder, int position) {
        holder.songTitle.setText(songsDetailsList.get(position).getSongTitle());

        if(MyMusic.currentSongNumber==position){
            holder.songTitle.setTextColor(Color.parseColor("#FFD9C0"));
            holder.albumMovieImg.setBackgroundResource(R.drawable.checking);

        }else{
            holder.songTitle.setTextColor(R.color.black);
            holder.albumMovieImg.setBackgroundResource(R.drawable.defaulthighlists);

        }
        byte[] albumArt = AlbumArt.getAlbumArt(songsDetailsList.get(position).getSongData());
        if(albumArt!=null){
            Glide.with(context).asBitmap().load(albumArt).into(holder.albumMovieImg);


        }else {
            Glide.with(context).asBitmap()
                    .load(R.drawable.music_logo).into(holder.albumMovieImg);

        }

        holder.itemView.setOnClickListener(e->{


            MyMusic.currentSongNumber=holder.getAdapterPosition();
            Intent intent = new Intent(context, MusicPlayDesign.class);
            intent.putExtra("KEY_VALUE",songsDetailsList);
          context.startActivity(intent);
        });


    }
    public  void updateSongList(ArrayList<SongsDetails> updatedList) {
        songsDetailsList.clear();
        songsDetailsList.addAll(updatedList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return songsDetailsList.size();
    }
}
