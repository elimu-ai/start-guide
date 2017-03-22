package org.literacyapp.handgesture;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by GSC on 04/02/2017.
 */
public class AnimationHelper implements Animation.AnimationListener {

    public static final long DEFAULT_ANIMATION_DELAY = 1000;

    private boolean repeat;

    private TouchListener mTouchListener;
    private Animation mAnimation;
    private View mView;

    public AnimationHelper(Context context, int idAnim, TouchListener touchListener) {
        mAnimation = AnimationUtils.loadAnimation(context, idAnim);
        mAnimation.setAnimationListener(this);
        mTouchListener = touchListener;
    }

    public void setRepeatMode(boolean repeat) {
        this.repeat = repeat;
    }

    public void animateView(View view) {
        animateView(view, DEFAULT_ANIMATION_DELAY);
    }

    public void animateView(View view, long delay) {
        this.mView = view;
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.startAnimation(mAnimation);
            }
        }, delay);
    }

    public void stopAnimation() {
        repeat = false;
        mView.clearAnimation();
    }

    public boolean isRepeatMode() {
        return repeat;
    }

    //region Animation.AnimationListener
    @Override
    public void onAnimationStart(Animation animation) {
        mTouchListener.onTouchStart();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mTouchListener.onTouchEnd();

        if (isRepeatMode()) {
            animateView(mView);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    //endregion

    public interface TouchListener {
        void onTouchStart();
        void onTouchEnd();
    }
}