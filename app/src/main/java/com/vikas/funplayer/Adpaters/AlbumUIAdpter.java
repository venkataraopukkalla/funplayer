package com.vikas.funplayer.Adpaters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.funplayer.AlbumUIDesign;
import com.vikas.funplayer.AlbumUIMusicPlayDesign;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.MyMusic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumUIAdpter extends RecyclerView.Adapter<AlbumUITextViewHolder> {

    private List<SongsDetails> albumSongDetails =new ArrayList<>();
    private Context context;

    public AlbumUIAdpter(List<SongsDetails> albumSongDetails, Context context) {
        this.albumSongDetails = albumSongDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumUITextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.albumuitxtviewholder, parent, false);
        return new AlbumUITextViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumUITextViewHolder holder, int position) {
       // Log.i("SMILE",albumSongDetails.size()+"");
        holder.albumUiSongNum.setText(position+1+"");
        holder.albumUISongName.setText(albumSongDetails.get(position).getSongTitle());
       holder.itemView.setOnClickListener(e->{
         //  Log.i("keepsmile","before :"+albumSongDetails.get(position).getSongNumber()+"");
          // MyMusic.currentSongNumber=albumSongDetails.get(position).getSongNumber();
           MyMusic.currentSongNumber=holder.getAdapterPosition();
           Intent intent = new Intent(context, AlbumUIMusicPlayDesign.class);
           intent.putExtra("ALBUM_UI_KEY", (Serializable) albumSongDetails);
           intent.putExtra("ALBUM_UI_SONG_NO_KEY",albumSongDetails.get(position).getSongNumber());
           context.startActivity(intent);
       });
    }

    @Override
    public int getItemCount() {
        return albumSongDetails.size();
    }
}
