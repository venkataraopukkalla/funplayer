package com.vikas.funplayer.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.vikas.funplayer.MainActivity;
import com.vikas.funplayer.MusicPlayDesign;
import com.vikas.funplayer.R;
import com.vikas.funplayer.fragments.musicfeature.AlbumFragment;
import com.vikas.funplayer.model.SongsDetails;
import com.vikas.funplayer.ui.PlayListActivity;
import com.vikas.funplayer.util.AlbumArt;
import com.vikas.funplayer.util.MyMusic;
import com.vikas.funplayer.util.RemoteConfigManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {

    private LinearLayout currentSongDisplay_container;
    private TextView displayCurrentSongShortCutTile_txt;
    private ImageView displayCurrentSongShortCutArtPic_img,currentSongShortCutPlay_imgBtn;
    MediaPlayer mediaPlayer = MyMusic.instanceOfMyMusic();
    ArrayList<SongsDetails> parcelableArrayList;

    private    Handler handler;
    private Runnable runnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);
         parcelableArrayList = getArguments().getParcelableArrayList(MainActivity.KEY_VALUE);
        MusicFeatureFragment musicFeatureFragment = MusicFeatureFragment.instance(parcelableArrayList);
        getChildFragmentManager().beginTransaction().replace(R.id.framlayout_musicFeature,musicFeatureFragment).commit();

        currentSongDisplay_container=view.findViewById(R.id.currentSongDisplay_container);
        displayCurrentSongShortCutTile_txt=view.findViewById(R.id.currentSongTitle_txt);
        displayCurrentSongShortCutArtPic_img=view.findViewById(R.id.currentSongAlbumArt_img);
        currentSongShortCutPlay_imgBtn=view.findViewById(R.id.cureentSongpausebtn_img);







         handler = new Handler(Looper.getMainLooper());
         runnable = new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && mediaPlayer != null && mediaPlayer.isPlaying()) {
                    cusumOnstart(); // your custom UI update method
                }
                handler.postDelayed(this, 1000); // Re-post itself every 1 second
            }
        };

// Start the loop
        handler.post(runnable);


        currentSongShortCutPlay_imgBtn.setOnClickListener(e->{

            if(mediaPlayer.isPlaying() && mediaPlayer!=null) {
                mediaPlayer.pause();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }else {
                mediaPlayer.start();
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }

        });

        currentSongDisplay_container.setOnClickListener(e->{
            try {
                Intent intent = new Intent(getContext(), MusicPlayDesign.class);
                intent.putExtra("KEY_VALUE",parcelableArrayList);
                intent.putExtra("VIKAS",true);
                getContext().startActivity(intent);
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }


        });




        // playlist on click lister

        view.findViewById(R.id.playlist_feature_card).setOnClickListener(e -> {
           startActivity(new Intent(requireContext(), PlayListActivity.class));
        });

        view.findViewById(R.id.ai_mix_feature_card).setOnClickListener(e -> {

            // show alert box this feature coming soon stay update
            showAlertDialog();

        });









        return view;
    }

    private void showAlertDialog() {
        // Create the AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle("AI Mix Feature Coming Soon!")
                .setMessage("We will notify you when the feature is rolled out.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Dismiss the dialog when OK is clicked
                .setCancelable(false);  // Optionally set to false to prevent the user from canceling it by tapping outside

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable); // cleanup
    }

    private void cusumOnstart(){
        if(MyMusic.instanceOfMyMusic()!=null && MyMusic.currentSongNumber>-1)
        {
            if(mediaPlayer.isPlaying()){
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.pause_img);
            }else{
                currentSongShortCutPlay_imgBtn.setImageResource(R.drawable.play_img);
            }
            String songTitle = parcelableArrayList.get(MyMusic.currentSongNumber).getSongTitle();
            currentSongDisplay_container.setVisibility(View.VISIBLE);
            displayCurrentSongShortCutTile_txt.setText(songTitle);
            byte[] albumArt = AlbumArt.getAlbumArt(parcelableArrayList.get(MyMusic.currentSongNumber).getSongData());
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(500);

            if(albumArt!=null && isAdded() ){

                Glide.with(this).asBitmap().load(albumArt).into(displayCurrentSongShortCutArtPic_img);
                displayCurrentSongShortCutArtPic_img.startAnimation(rotateAnimation);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.music_logo_light).into(displayCurrentSongShortCutArtPic_img);
            }


            // Toast.makeText(this,MyMusst.LENGTH_ic.currentSongNumber+"",ToaLONG).show();
        }
    }


}