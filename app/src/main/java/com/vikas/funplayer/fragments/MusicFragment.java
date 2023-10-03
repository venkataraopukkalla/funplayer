package com.vikas.funplayer.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.MyMusic;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {

    private Toolbar currentSongShortCut_toolbar;
    private TextView displayCurrentSongShortCutTile_txt;
    private ImageView displayCurrentSongShortCutArtPic_img,currentSongShortCutPlay_imgBtn;
    MediaPlayer mediaPlayer = MyMusic.instanceOfMyMusic();
    ArrayList<SongsDetails> parcelableArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);
         parcelableArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        MusicFeatureFragment musicFeatureFragment = MusicFeatureFragment.instance(parcelableArrayList);
        getChildFragmentManager().beginTransaction().replace(R.id.framlayout_musicFeature,musicFeatureFragment).commit();

        currentSongShortCut_toolbar=view.findViewById(R.id.currentSongDisplay_toolbar);
        displayCurrentSongShortCutTile_txt=view.findViewById(R.id.currentSongTitle_txt);
        displayCurrentSongShortCutArtPic_img=view.findViewById(R.id.currentSongAlbumArt_img);
        currentSongShortCutPlay_imgBtn=view.findViewById(R.id.cureentSongpausebtn_img);



        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(getActivity()!=null){

                    getActivity().runOnUiThread( ()->{
                        if(mediaPlayer!=null &&mediaPlayer.isPlaying() ){
                            cusumOnstart();

                        }

                        new Handler().postDelayed(this,1000);
                    });
                }
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();


        currentSongShortCutPlay_imgBtn.setOnClickListener(e->{

            if(mediaPlayer.isPlaying() && mediaPlayer!=null) {
                mediaPlayer.pause();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }else {
                mediaPlayer.start();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }

        });

        currentSongShortCut_toolbar.setOnClickListener(e->{
            try {
                Intent intent = new Intent(getContext(), MusicPlayDesign.class);
                intent.putExtra("KEY_VALUE",parcelableArrayList);
                intent.putExtra("VIKAS",true);
                getContext().startActivity(intent);
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }


        });

        
        return view;
    }
    private void cusumOnstart(){
        if(MyMusic.instanceOfMyMusic()!=null && MyMusic.currentSongNumber!=-1)
        {
            if(mediaPlayer.isPlaying()){
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }else{
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }
            String songTitle = parcelableArrayList.get(MyMusic.currentSongNumber).getSongTitle();
            currentSongShortCut_toolbar.setVisibility(View.VISIBLE);
            displayCurrentSongShortCutTile_txt.setText(songTitle);
            byte[] albumArt = AlbumArt.getAlbumArt(parcelableArrayList.get(MyMusic.currentSongNumber).getSongData());
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(500);

            if(albumArt!=null && isAdded() ){

                Glide.with(this).asBitmap().load(albumArt).into(displayCurrentSongShortCutArtPic_img);
                displayCurrentSongShortCutArtPic_img.startAnimation(rotateAnimation);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.music_logo).into(displayCurrentSongShortCutArtPic_img);
            }


            // Toast.makeText(this,MyMusst.LENGTH_ic.currentSongNumber+"",ToaLONG).show();
        }
    }


}