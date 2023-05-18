package com.vikas.funplayer.util;

import android.media.MediaPlayer;

public class MyMusic {

    public  static MediaPlayer instances=null;
    public static int currentSongNumber=-1;
    public static MediaPlayer instanceOfMyMusic(){
        if(instances==null){
            instances=new MediaPlayer();
        }
        return  instances;
    }
}
