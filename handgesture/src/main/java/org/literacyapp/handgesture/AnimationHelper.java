package org.literacyapp.handgesture;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import static org.literacyapp.handgesture.Constants.DEFAULT_ANIMATION_DELAY;
import static org.literacyapp.handgesture.Constants.MILLISECONDS;
import static org.literacyapp.handgesture.Constants.SCALE_DURATION;
import static org.literacyapp.handgesture.Constants.SCALE_FACTOR;

/**
 * Created by GSC on 04/02/2017.
 */
public class AnimationHelper implements Animation.AnimationListener {

    private HandAnimationListener mAnimationListener;

    private boolean repeat;

    private HandGestureListener mHandGestureListener;
    private Animation mAnimation;
    private View mView;

    public AnimationHelper(Context context, int idAnim, HandGestureListener handGestureListener) {
        mAnimation = AnimationUtils.loadAnimation(context, idAnim);
        mAnimation.setAnimationListener(this);
        mHandGestureListener = handGestureListener;

        mAnimationListener = new HandAnimationListener() {
            @Override
            public void onMakeSmallerEnd() {
                mView.setPivotX(.5f);
                mView.setPivotY(.5f);
                mView.setScaleX(SCALE_FACTOR);
                mView.setScaleY(SCALE_FACTOR);
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
        }, delay * MILLISECONDS);
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
        mHandGestureListener.onTouchStart();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mHandGestureListener.onTouchEnd();
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

    public ScaleAnimation makeSmallerImage() {
        return makeSmallerImage(SCALE_DURATION);
    }

    public ScaleAnimation makeSmallerImage(long duration) {
        ScaleAnimation scale = new ScaleAnimation(1f, SCALE_FACTOR, 1f, SCALE_FACTOR);

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