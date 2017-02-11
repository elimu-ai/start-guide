package org.literacyapp.startguide.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by GSC on 04/02/2017.
 */
public class AnimationHelper {

    public static void animateView(final Context context, final View imageView, final int idAnimation) {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Play animation
                Animation animation = AnimationUtils.loadAnimation(context, idAnimation);
                imageView.startAnimation(animation);
            }
        }, 1000);
    }
}