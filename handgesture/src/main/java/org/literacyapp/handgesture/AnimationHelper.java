package org.literacyapp.handgesture;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

/**
 * Created by GSC on 04/02/2017.
 */
public class AnimationHelper implements Animation.AnimationListener {

    public static final long DEFAULT_ANIMATION_DELAY = 1000;
    private static final long SCALE_DURATION = 500;

    private HandAnimationListener mAnimationListener;

    private boolean repeat;

    private TouchListener mTouchListener;
    private Animation mAnimation;
    private View mView;

    public AnimationHelper(Context context, int idAnim, TouchListener touchListener) {
        mAnimation = AnimationUtils.loadAnimation(context, idAnim);
        mAnimation.setAnimationListener(this);
        mTouchListener = touchListener;

        mAnimationListener = new HandAnimationListener() {
            @Override
            public void onMakeSmallerEnd() {
                mView.setScaleX(.9f);
                mView.setScaleY(.9f);
                mView.startAnimation(mAnimation);
            }
        };
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
                mView.startAnimation(makeSmallerImage());
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
        mView.setScaleX(1);
        mView.setScaleY(1);

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

    public ScaleAnimation makeSmallerImage() {
        return makeSmallerImage(SCALE_DURATION);
    }

    public ScaleAnimation makeSmallerImage(long duration) {
        ScaleAnimation scale = new ScaleAnimation(1f, 0.9f, 1f, 0.9f);

        scale.setFillEnabled(true);
        scale.setFillAfter(true);
        scale.setDuration(duration);

        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationListener.onMakeSmallerEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return scale;
    }

    public interface HandAnimationListener {
        void onMakeSmallerEnd();
    }
}