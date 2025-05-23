package com.vikas.funplayer.util;

import android.media.MediaMetadataRetriever;

import androidx.annotation.Nullable;

import java.io.IOException;

public class AlbumArt {
    @Nullable
    public static byte[] getAlbumArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] art = null;
        try {
            retriever.setDataSource(path); // May throw exception
            art = retriever.getEmbeddedPicture();
        } catch (RuntimeException e) {
            e.printStackTrace(); // Handle invalid file paths or unsupported formats
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException | IOException e) {
                // Shouldn't happen, but just in case
                e.printStackTrace();
            }
        }
        return art;
    }
}
