package com.vikas.funplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vikas.funplayer.Adpaters.CategorieModel;
import com.vikas.funplayer.Adpaters.CategoriesAdpater;
import com.vikas.funplayer.fragments.HomeFragment;
import com.vikas.funplayer.fragments.MeFragment;
import com.vikas.funplayer.fragments.MusicFragment;
import com.vikas.funplayer.fragments.VideosFragment;
import com.vikas.funplayer.fragments.musicfeature.AllSongsFragment;
import com.vikas.funplayer.model.SongsDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Fragment selectFrament;
    private BottomNavigationView bottomNavigationView;
    private  final String PERMISSION=Manifest.permission.READ_EXTERNAL_STORAGE;

    ArrayList<SongsDetails> songsDetailsList=new ArrayList<>();
    public  final static String KEY_VALUE="VIKAS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottonNavbar);


        // check permission
        if(!checkPermissions()){
            // if user not accept permission that time again show request
            setPermissionRequest();
        }

        //url
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // string array of required projection
        String projection[]={MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST};
        //selection
        String selection= MediaStore.Audio.Media.IS_MUSIC+" != 0";
        // cursor
        Cursor cursor = getContentResolver().query(contentUri, projection, selection, null, null);
        int i=0;
        while (cursor.moveToNext()){
            SongsDetails details = new SongsDetails(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),i);
            if(new File(details.getSongData()).exists() && details.getSongData().endsWith(".mp3")&& details.getSongData().indexOf("_")<0){
                songsDetailsList.add(details);
                i++;
            }
        }
        //set Framgents into activity
        setAllFragments();


    }

    private boolean checkPermissions() {
        int i = ContextCompat.checkSelfPermission(this, PERMISSION);
        if(i== PackageManager.PERMISSION_GRANTED) return  true;
        return  false;
    }
    private  void setPermissionRequest(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSION)){
            Toast.makeText(this, "Read permission is required,Please allow permission", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{PERMISSION},141);
        }
    }

    private void setAllFragments() {

        selectFrament=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,selectFrament).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home_nav_menu:
                    selectFrament =new HomeFragment();
                    break;
                case R.id.videos_nav_menu:
                    selectFrament=new VideosFragment();
                    break;
                case R.id.music_nav_menu:
                    selectFrament=new MusicFragment();
                    break;
                case R.id.me_nav_menu:
                    selectFrament=new MeFragment();
                    break;
            }
            if(selectFrament!=null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_VALUE,songsDetailsList);
               // bundle.putString(KEY_VALUE,songsDetailsList.get(1).getSongTitle());
                selectFrament.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout, selectFrament).commit();
            }

            return true;
        });
    }
}