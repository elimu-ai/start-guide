package ai.elimu.handgesture;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

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

        initAnimationListeners(handGestureListener);
    }

    public AnimationHelper(int translateX, int translateY, float duration, HandGestureListener handGestureListener) {
        mAnimation = new TranslateAnimation(0, translateX, 0, translateY);
        mAnimation.setDuration((long) (duration * Constants.MILLISECONDS));

        initAnimationListeners(handGestureListener);
    }

    private void initAnimationListeners(HandGestureListener handGestureListener) {
        mAnimation.setAnimationListener(this);
        mHandGestureListener = handGestureListener;

        mAnimationListener = new HandAnimationListener() {
            @Override
            public void onMakeSmallerEnd() {
                mView.setPivotX(.5f);
                mView.setPivotY(.5f);
                mView.setScaleX(Constants.SCALE_FACTOR);
                mView.setScaleY(Constants.SCALE_FACTOR);
                mView.startAnimation(mAnimation);
            }
        };
    }

    public void setRepeatMode(boolean repeat) {
        this.repeat = repeat;
    }

    public void animateView(View view) {
        animateView(view, Constants.DEFAULT_ANIMATION_DELAY);
    }

    public void animateView(View view, long delay) {
        this.mView = view;
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.startAnimation(makeSmallerImage());
                mHandGestureListener.onAnimationStarted();
            }
        }, delay * Constants.MILLISECONDS);
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
        return makeSmallerImage(Constants.SCALE_DURATION);
    }

    public ScaleAnimation makeSmallerImage(long duration) {
        ScaleAnimation scale = new ScaleAnimation(1f, Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR);

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