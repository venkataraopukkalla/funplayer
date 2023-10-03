package com.vikas.funplayer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.ConstraintlayoutSetBackGroundColor;
import com.vikas.funplayer.util.MyMusic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MusicPlayDesign extends AppCompatActivity implements  MusicFunctionality {
    private ImageView backOption_img,moreOption_img;
    private ImageView setImageBackground_img,likedTheSongs_img;
    private TextView setSongTitle,movieSongtxt;
    private SeekBar seekBar;
    private  TextView setSeekBarStartTime,setSeekBarEndTime;
    private  ImageView playSong_imgBtn,playPreviousSong_imgBtn,playNextSong_imgBtn;

    private ConstraintLayout musicPlayConstrainLayout_design;


    MediaPlayer mediaPlayer = MyMusic.instanceOfMyMusic();
    ArrayList<SongsDetails> keyValue;
    SongsDetails currentSongDetails;

    boolean isSongContinue=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play_design);
        backOption_img=findViewById(R.id.back_option_img);
        moreOption_img=findViewById(R.id.more_option_img);
        setImageBackground_img=findViewById(R.id.songAlbumArt_img);
        likedTheSongs_img=findViewById(R.id.isItSongLiked_img);
        setSongTitle=findViewById(R.id.songTitle_txt);
        movieSongtxt=findViewById(R.id.songMovieNametxt);
        seekBar=findViewById(R.id.seekbarTimer);
        setSeekBarStartTime=findViewById(R.id.starttime_txt);
        setSeekBarEndTime=findViewById(R.id.endtime_seekbar_txt);
        playSong_imgBtn=findViewById(R.id.play_nav_img);
        playNextSong_imgBtn=findViewById(R.id.forw_nav_img);
        playPreviousSong_imgBtn=findViewById(R.id.back_nav_img);
        musicPlayConstrainLayout_design=findViewById(R.id.music_play_design_constlayout);

        keyValue= (ArrayList<SongsDetails>) getIntent().getSerializableExtra("KEY_VALUE");
        boolean check=getIntent().getBooleanExtra("VIKAS",false);


        if(check){
            if(mediaPlayer!=null &&mediaPlayer.isPlaying()) {
                playSong_imgBtn.setImageResource(R.drawable.pause_img);
                mediaPlayer.start();
            }
            else
            {
                mediaPlayer.pause();
                playSong_imgBtn.setImageResource(R.drawable.play_img);
            }
            sameSongPressed();
        }else{
            setResources();
        }



        playSong_imgBtn.setOnClickListener(e->{
            if(mediaPlayer.isPlaying()){
                playSong_imgBtn.setImageResource(R.drawable.play_img);
                mediaPlayer.pause();
                isSongContinue=true;
            }else{
                if(isSongContinue||check){
                    mediaPlayer.start();
                }else {
                    playCurrentSong();
                }
                playSong_imgBtn.setImageResource(R.drawable.pause_img);
            }
        });
        playPreviousSong_imgBtn.setOnClickListener(e->playPreviousSong());
        playNextSong_imgBtn.setOnClickListener(e->playNextSong());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlayDesign.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

              if(mediaPlayer!=null){
                  seekBar.setProgress(mediaPlayer.getCurrentPosition());
                  setSeekBarStartTime.setText(convertMilsecToMinAndSec(mediaPlayer.getCurrentPosition()+""));
              }

              new Handler().postDelayed(this,100);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (seekBar.getProgress()!=0 && MyMusic.currentSongNumber<keyValue.size()) {
                    playNextSong();
                }else  if(MyMusic.currentSongNumber==keyValue.size()){
                    MyMusic.currentSongNumber-=1;
                    Toast.makeText(getApplicationContext(),"All songs Completed",Toast.LENGTH_SHORT).show();
                    mediaPlayer.reset();
                    playSong_imgBtn.setImageResource(R.drawable.play_img);

                   return;
                }

            }
        });

    }
    private    void setResources(){
        mediaPlayer.reset();
        try {
             currentSongDetails = keyValue.get(MyMusic.currentSongNumber);
            byte[] albumArt = AlbumArt.getAlbumArt(currentSongDetails.getSongData());
            if(albumArt!=null) {
                Glide.with(getApplicationContext()).asBitmap().load(albumArt).into(setImageBackground_img);

            }
            else
                Glide.with(getApplicationContext()).asBitmap().load(R.drawable.music_logo).into(setImageBackground_img);
            setSongTitle.setText(currentSongDetails.getSongTitle());

            // for background color of constartLayout

            try {
                ConstraintlayoutSetBackGroundColor.setBackGroundColorFromGlide(this,albumArt,musicPlayConstrainLayout_design);
            }catch (Exception e){
                e.printStackTrace();
            }
            artlistNames();




            seekBar.setMax(Integer.valueOf(currentSongDetails.getSongDuration()));
            setSeekBarEndTime.setText(convertMilsecToMinAndSec(currentSongDetails.getSongDuration()));
            playCurrentSong();
            //mediaPlayer.start();
        }catch (Exception exception){

        }


    }
    private void sameSongPressed(){
        try {
            currentSongDetails = keyValue.get(MyMusic.currentSongNumber);
            byte[] albumArt = AlbumArt.getAlbumArt(currentSongDetails.getSongData());
            if(albumArt!=null) {
                Glide.with(getApplicationContext()).asBitmap().load(albumArt).into(setImageBackground_img);

            }
            else
                Glide.with(getApplicationContext()).asBitmap().load(R.drawable.music_logo).into(setImageBackground_img);
            setSongTitle.setText(currentSongDetails.getSongTitle());

            // for background color of constartLayout

            try {
                ConstraintlayoutSetBackGroundColor.setBackGroundColorFromGlide(this,albumArt,musicPlayConstrainLayout_design);
            }catch (Exception e){
                e.printStackTrace();
            }
            artlistNames();

            seekBar.setMax(Integer.valueOf(currentSongDetails.getSongDuration()));
            setSeekBarEndTime.setText(convertMilsecToMinAndSec(currentSongDetails.getSongDuration()));
        }catch (Exception exception){

        }
    }
    private String convertMilsecToMinAndSec(String time ){
        long miliisec = Integer.valueOf(time);
        long min= miliisec/(1000*60);
        long sec=(miliisec/1000)%60;
        return min+":"+sec;

    }

    @Override
    public void playCurrentSong() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSongDetails.getSongData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playSong_imgBtn.setImageResource(R.drawable.pause_img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playNextSong() {

        MyMusic.currentSongNumber+=1;
            setResources();

    }

    @Override
    public void playPreviousSong() {
        MyMusic.currentSongNumber-=1;
        setResources();
    }
    private void artlistNames(){
        try {
            // Load the song file
            File songFile = new File(currentSongDetails.getSongData());
            AudioFile audioFile = AudioFileIO.read(songFile);

            // Get the movie name from the metadata
            Tag tag = audioFile.getTag();
            String artistname = tag.getFirst(FieldKey.ALBUM_ARTIST);
            movieSongtxt.setText(artistname);



        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }


}