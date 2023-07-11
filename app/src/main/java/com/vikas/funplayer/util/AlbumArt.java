package com.vikas.funplayer.util;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

//
//  Glide.with(context).asBitmap().load(albumArt).into(new CustomTarget<Bitmap>() {
//@Override
//public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
//@Override
//public void onGenerated(@Nullable Palette palette) {
//        if(palette!=null){
//        // Retrieve the desired color from the palette
//        vibrantColor= palette.getVibrantColor(ContextCompat.getColor(context, R.color.tabselecteddcolor));
//
//        }
//        }
//        });
//        }
//
//@Override
//public void onLoadCleared(@Nullable Drawable placeholder) {
//
//        }
//        });