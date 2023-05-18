package com.vikas.funplayer.util;

import android.media.MediaMetadataRetriever;

import java.io.IOException;

public class AlbumArt {
    public  static  byte[] getAlbumArt(String path)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] embeddedPicture = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  embeddedPicture;

    }
}
