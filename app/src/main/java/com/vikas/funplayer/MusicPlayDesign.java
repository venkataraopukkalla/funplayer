package com.vikas.funplayer;




import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.vikas.funplayer.model.Playlist;
import com.vikas.funplayer.model.PlaylistSongCrossRef;
import com.vikas.funplayer.model.Song;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.room.MusicDao;
import com.vikas.funplayer.room.MusicDatabaseInstance;
import com.vikas.funplayer.util.AdUtils;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.ConstraintlayoutSetBackGroundColor;
import com.vikas.funplayer.util.MusicConstants;
import com.vikas.funplayer.util.MyMusic;
import com.vikas.funplayer.util.NetworkUtils;
import com.vikas.funplayer.util.PlaylistCreationManager;
import com.vikas.funplayer.util.RemoteConfigManager;
import com.vikas.funplayer.util.RewardAdManager;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicPlayDesign extends AppCompatActivity implements  MusicFunctionality {
    private ImageView backOption_img,moreOption_img;
    private ImageView setImageBackground_img,likedTheSongs_img;
    private TextView setSongTitle,movieSongtxt;
    private SeekBar seekBar;
    private  TextView setSeekBarStartTime,setSeekBarEndTime;
    private  ImageView playPreviousSong_imgBtn,playNextSong_imgBtn;

    private ConstraintLayout musicPlayConstrainLayout_design;


    MediaPlayer mediaPlayer = MyMusic.instanceOfMyMusic();
    public  static ArrayList<SongsDetails> keyValue;
    SongsDetails currentSongDetails;

    boolean isSongContinue=false;


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());


    public static MusicFunctionality musicControlReference;

    private AdView adView;

    private final Handler seekBarHandler = new Handler(Looper.getMainLooper());

    public static ImageView playSong_imgBtn;






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






        String bannerAdUnitId = RemoteConfigManager.getString(this, "banner_ad_unit_id", getString(R.string.test_banner_ad_unit)); // test fallback
        String rewardAdUnitId = RemoteConfigManager.getString(this, "rewarded_ad_unit_id", getString(R.string.test_reward_ad_unit)); // test fallback

        Log.d("bannerAdUnitId",bannerAdUnitId);
//        adView.setAdUnitId(bannerAdUnitId);

        boolean showAds = RemoteConfigManager.getBoolean(this, "show_ads", true);

        if(NetworkUtils.isNetworkAvailable(this) && showAds){
            loadAdaptiveBanner(bannerAdUnitId);

            RewardAdManager.loadAd(this, rewardAdUnitId);

        }


        keyValue= (ArrayList<SongsDetails>) getIntent().getSerializableExtra("KEY_VALUE");
        boolean check=getIntent().getBooleanExtra("VIKAS",false);


        if (check) {
            // 1) Refresh UI from the current song
            sameSongPressed();

            // 2) Resume playback explicitly
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

            // 3) Update the play/pause button icon
            playSong_imgBtn.setImageResource(
                    mediaPlayer != null && mediaPlayer.isPlaying()
                            ? R.drawable.pause_icon
                            : R.drawable.play_icon
            );

            // 4) (Optional) Sync the notification icon
            updateNotification();
        } else {
            setResources();
        }


        backOption_img.setOnClickListener(e->onBackPressed());


        playSong_imgBtn.setOnClickListener(e->{
            if(mediaPlayer.isPlaying()){
                playSong_imgBtn.setImageResource(R.drawable.play_icon);
                mediaPlayer.pause();
                isSongContinue=true;




            }else{
                if(isSongContinue||check){
                    mediaPlayer.start();
                }else {
                    playCurrentSong();
                }
                playSong_imgBtn.setImageResource(R.drawable.pause_icon);

            }

            updateUI();           // 🔁 Update play/pause image
            updateNotification(); // 🔁 Sync notification icon
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
        final Runnable seekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    setSeekBarStartTime.setText(convertMilsecToMinAndSec(mediaPlayer.getCurrentPosition() + ""));
                }
                seekBarHandler.postDelayed(this, 100);
            }
        };

        seekBarHandler.post(seekBarRunnable);



        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (seekBar.getProgress()!=0 && MyMusic.currentSongNumber<keyValue.size()) {
                    playNextSong();
                }else  if(MyMusic.currentSongNumber==keyValue.size()){
                    MyMusic.currentSongNumber-=1;
                    Toast.makeText(getApplicationContext(),"All songs Completed",Toast.LENGTH_SHORT).show();
                    mediaPlayer.reset();
                    playSong_imgBtn.setImageResource(R.drawable.play_icon);

                    return;
                }

            }
        });


        // save song into playlist
        likedTheSongs_img.setOnClickListener(e->{

            Log.i("keepsmile","clicked");
            showAddToPlaylistDialog(this,currentSongDetails);
        });

        musicControlReference = this;





       // MyMusic.playMusic(this);








    }

    public static void updateUI() {
        if (playSong_imgBtn == null) return;

        if (MyMusic.instanceOfMyMusic().isPlaying()) {
            playSong_imgBtn.setImageResource(R.drawable.pause_icon);
        } else {
            playSong_imgBtn.setImageResource(R.drawable.play_icon);
        }
    }

    private void updateNotification() {
        Intent intent = new Intent(this, MusicNotificationService.class);
        startService(intent);
    }



    private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(MusicConstants.EXTRA_CONTROL_TYPE);
            if (MusicConstants.CONTROL_PLAY.equals(type)) {
                playSong_imgBtn.setImageResource(R.drawable.pause_icon);
            } else if (MusicConstants.CONTROL_PAUSE.equals(type)) {
                playSong_imgBtn.setImageResource(R.drawable.play_icon);
            }
        }
    };


    private void loadAdaptiveBanner(String bannerAdUnit) {
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        adView = new AdView(this);
        adContainerView.addView(adView);

        // Set your real Ad Unit ID here
        adView.setAdUnitId(bannerAdUnit); // <-- Test ID
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        // Load the ad
        AdUtils.loadBannerAd(this, adView);
    }
    private AdSize getAdSize() {
        // Calculate screen width (in dp) for adaptive ad size
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }





    private void showCreatePlaylistDialog(Context context, SongsDetails song) {
        SharedPreferences prefs = context.getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE);
        String today = PlaylistCreationManager.getTodayDate();
        String lastCreationDate = prefs.getString("last_creation_date", "");
        String adWatchedDate = prefs.getString("ad_watched_date", "");

        if (!today.equals(lastCreationDate)) {
            // First playlist of the day (free)
            PlaylistCreationManager.onFreePlaylistCreated(context);
            launchPlaylistDialog(context, song, false);
        } else if (today.equals(adWatchedDate)) {
            // Already watched ad today, allow unlimited creations
            launchPlaylistDialog(context, song, true);
        } else {
            // Show rewarded ad before allowing more creations
            if (NetworkUtils.isNetworkAvailable(context)) {

                // admin allow ads or not using remote config
                boolean showAds = RemoteConfigManager.getBoolean(context, "show_ads", true);
                if (!showAds) {
                    // First playlist of the day (free)
                    PlaylistCreationManager.onFreePlaylistCreated(context);
                    launchPlaylistDialog(context, song, false);
                    return;
                }


                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_reward_ad);
                dialog.setCancelable(true);

                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnWatchAd = dialog.findViewById(R.id.btnWatchAd);

                btnCancel.setOnClickListener(v -> dialog.dismiss());

                btnWatchAd.setOnClickListener(v -> {
                    dialog.dismiss();
                    RewardAdManager.showAd((Activity) context, () -> {
                        PlaylistCreationManager.onAdWatchedForPlaylist(context);
                        launchPlaylistDialog(context, song, true);
                    });
                });

                dialog.show();
            } else {
                // First playlist of the day (free)
                PlaylistCreationManager.onFreePlaylistCreated(context);
                launchPlaylistDialog(context, song, false);            }
        }
    }



    private void launchPlaylistDialog(Context context, SongsDetails song, boolean fromRewardAd) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_create_playlist, null);
        EditText input = view.findViewById(R.id.etPlaylistName);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create New Playlist");
        builder.setView(view);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String playlistName = input.getText().toString().trim();
            if (!playlistName.isEmpty()) {
            // ✅ Only mark the date if this was the free playlist
                if (PlaylistCreationManager.canCreateFreePlaylist(context)) {
                    PlaylistCreationManager.onFreePlaylistCreated(context);
                }

                addSongToPlaylist(context, playlistName, song);
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void showAddToPlaylistDialog(Context context, SongsDetails song) {
        executorService.execute(() -> {
            MusicDao musicDao = MusicDatabaseInstance.getInstance(context).musicDao();
            List<String> playlistNames = musicDao.getAllPlaylistNames();

            mainHandler.post(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add to Playlist");

                List<String> options = new ArrayList<>(playlistNames);
                options.add("Create New Playlist");


                builder.setItems(options.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == options.size() - 1) {
                        // Create new playlist
                        showCreatePlaylistDialog(context, song);
                    } else {
                        String selectedPlaylist = options.get(which);
                        addSongToPlaylist(context, selectedPlaylist, song);
                    }
                });

                builder.show();
            });
        });
    }

//    // In your Activity or wherever you need it:
//    private void showCreatePlaylistDialog(Context context, SongsDetails song) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.dialog_create_playlist, null);
//        EditText input = view.findViewById(R.id.etPlaylistName);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Create New Playlist");
//        builder.setView(view);
//
//        builder.setPositiveButton("Create", (dialog, which) -> {
//            String playlistName = input.getText().toString().trim();
//            if (!playlistName.isEmpty()) {
//                addSongToPlaylist(context, playlistName, song);
//            } else {
//                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//        builder.show();
//    }

    private void addSongToPlaylist(Context context, String playlistName, SongsDetails songDetails) {
        // Run DB work off the UI thread
        executorService.execute(() -> {
            MusicDao dao = MusicDatabaseInstance.getInstance(context).musicDao();

            // 1) Get or create the playlist
            long playlistId = dao.getPlaylistIdByName(playlistName);
            if (playlistId <= 0) {
                playlistId = dao.insertPlaylist(new Playlist(playlistName));
                Log.d("AddToPlaylist", "Created playlist '" + playlistName + "' with ID=" + playlistId);
            } else {
                Log.d("AddToPlaylist", "Found existing playlist '" + playlistName + "' with ID=" + playlistId);
            }

            // 2) Get or insert the song
            long songId = dao.getSongIdByData(songDetails.getSongData());
            if (songId <= 0) {
                Song newSong = new Song(
                        songDetails.getSongTitle(),
                        songDetails.getSongData(),
                        songDetails.getSongDuration(),
                        songDetails.getSongArtist()
                );
                newSong.setSongNumber(MyMusic.currentSongNumber);
                songId = dao.insertSong(newSong);
                Log.d("AddToPlaylist", "Inserted song '" + songDetails.getSongTitle() +
                        "' with ID=" + songId);
            } else {
                Log.d("AddToPlaylist", "Song already exists with ID=" + songId);
            }

            // 3) Check & insert cross-ref
            int existing = dao.isSongInPlaylist(playlistId, songId);
            if (existing == 0) {
                dao.insertPlaylistSongCrossRef(new PlaylistSongCrossRef(playlistId, songId));
                Log.d("AddToPlaylist", "Linked song " + songId + " → playlist " + playlistId);
            } else {
                Log.d("AddToPlaylist", "Song " + songId + " already in playlist " + playlistId);
            }

            // 4) Feedback on the main thread
            mainHandler.post(() ->
                    Toast.makeText(context,
                            existing == 0
                                    ? "Added '" + songDetails.getSongTitle() + "' to \"" + playlistName + "\""
                                    : "'" + songDetails.getSongTitle() + "' was already in \"" + playlistName + "\"",
                            Toast.LENGTH_SHORT
                    ).show()
            );
        });
    }

    private    void setResources(){
        mediaPlayer.reset();
        try {
             currentSongDetails = keyValue.get(MyMusic.currentSongNumber);
            // ✅ Update global data for notification service
            MyMusic.keyValue = keyValue;
            MyMusic.currentSongTitle = currentSongDetails.getSongTitle();
            MyMusic.songUri = currentSongDetails.getSongData();



            byte[] albumArt = AlbumArt.getAlbumArt(currentSongDetails.getSongData());
            if(albumArt!=null) {
                Glide.with(getApplicationContext()).asBitmap().load(albumArt).into(setImageBackground_img);

            }
            else
                Glide.with(getApplicationContext()).asBitmap().load(R.drawable.music_logo_light).into(setImageBackground_img);
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
                Glide.with(getApplicationContext()).asBitmap().load(R.drawable.music_logo_light).into(setImageBackground_img);
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
    private String convertMilsecToMinAndSec(String time) {
        long milliseconds = Long.parseLong(time);
        long minutes = milliseconds / (1000 * 60);
        long seconds = (milliseconds / 1000) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }


    @Override
    public void playCurrentSong() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSongDetails.getSongData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playSong_imgBtn.setImageResource(R.drawable.pause_icon);

            // ————————————————
            // START THE FOREGROUND SERVICE
            startNotificationService();

            // ————————————————

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playNextSong() {

        MyMusic.currentSongNumber+=1;
            setResources();

        startNotificationService();


    }

    @Override
    public void playPreviousSong() {
        MyMusic.currentSongNumber-=1;
        if(MyMusic.currentSongNumber>=0){
            setResources();
            startNotificationService();
        }else{

            Toast.makeText(this,"No Previous Song",Toast.LENGTH_SHORT).show();
            MyMusic.currentSongNumber=0;
        }

    }

    private void startNotificationService() {
        Intent svc = new Intent(this, MusicNotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(svc);
        } else {
            startService(svc);
        }
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



    @Override
    protected void onPause() {
        if (adView != null) adView.pause();
        super.onPause();

        androidx.localbroadcastmanager.content.LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(controlReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
        androidx.localbroadcastmanager.content.LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(controlReceiver,
                        new IntentFilter(MusicConstants.ACTION_MEDIA_CONTROL));

        if (!mediaPlayer.isPlaying()) {
            updateUI();
            updateNotification();
        }

    }

    @Override
    protected void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
        seekBarHandler.removeCallbacksAndMessages(null);


    }


}