package com.vikas.funplayer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vikas.funplayer.R;

public class ConstraintlayoutSetBackGroundColor {

    public static void setBackGroundColorFromGlide(Context context, byte[] albumArt, ConstraintLayout constraintLayout) {
        if (albumArt == null || constraintLayout == null) return;

        Glide.with(context)
                .asBitmap()
                .load(albumArt)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Extract the dominant color using Palette
                        Palette.from(resource).generate(palette -> {
                            int fallbackColor = Color.parseColor("#070A52"); // status color
                            int dominantColor = palette.getDominantColor(fallbackColor);

                            // If too light, use fallback
                            if (isColorTooLight(dominantColor)) {
                                constraintLayout.setBackgroundColor(fallbackColor);
                            } else {
                                constraintLayout.setBackgroundColor(dominantColor);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Optional: handle if needed
                    }
                });
    }

    // Light color checker based on luminance
    private static boolean isColorTooLight(int color) {
        double r = Color.red(color) / 255.0;
        double g = Color.green(color) / 255.0;
        double b = Color.blue(color) / 255.0;
        double luminance = 0.299 * r + 0.587 * g + 0.114 * b;
        return luminance > 0.85;
    }
}
