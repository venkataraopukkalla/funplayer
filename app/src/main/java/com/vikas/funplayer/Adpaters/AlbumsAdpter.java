package com.vikas.funplayer.Adpaters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vikas.funplayer.AlbumUIDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumsAdpter extends RecyclerView.Adapter<AlbumsAdpter.AlbumTextViewHolder> {

    private List<List<SongsDetails>> albumSongsList = new ArrayList<>();
    private final Context context;

    public AlbumsAdpter(List<List<SongsDetails>> albumSongsList, Context context) {
        this.albumSongsList = albumSongsList;
        this.context = context;
    }

    /** Swap in a new data set and refresh the grid. */
    public void updateData(List<List<SongsDetails>> newAlbumSongsList) {
        this.albumSongsList = newAlbumSongsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.albumviewholder, parent, false);
        return new AlbumTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumTextViewHolder holder, int position) {
        List<SongsDetails> albumSong = albumSongsList.get(position);

        long albumId = albumSong.get(0).getAlbumId(); // From your song model
        Uri albumArtUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), albumId);

        Glide.with(context)
                .load(albumArtUri)
                .placeholder(R.drawable.music_logo_light)
                .error(R.drawable.music_logo_light)
                .into(holder.albumsLogo);


        // When tapped, open the UI with the full list for this album
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AlbumUIDesign.class);
            intent.putExtra("KEY_VALUE", (Serializable) albumSong);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return albumSongsList.size();
    }

    /** Simple ViewHolder for your album grid items. */
    static class AlbumTextViewHolder extends RecyclerView.ViewHolder {
        ImageView albumsLogo;

        AlbumTextViewHolder(@NonNull View itemView) {
            super(itemView);
            albumsLogo = itemView.findViewById(R.id.albumLogoPic_img);
        }
    }
}
