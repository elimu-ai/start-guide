package ai.elimu.handgesture;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;


/**
 */
public class GestureHelper {

    private static final long ONE_TOUCH_PRESSURE_TIME = 600;
    private static final long DOUBLE_TOUCH_PRESSURE_TIME = 100;
    private static final long TOUCH_AND_HOLD_PRESSURE_TIME = 1400;

    //Offset for second touch in double touch gesture
    private static final long SECOND_TOUCH_OFFSET = 100;

    private boolean mFirstAnimation = true;

    private View mView;
    private HandGestureListener mGestureListener;

    public GestureHelper(View view, HandGestureListener listener) {
        mView = view;
        mGestureListener = listener;
    }

    public void startOneTouchAnimation(int animationDelay) {
        startTouchAnimation(animationDelay, ONE_TOUCH_PRESSURE_TIME);
    }

    public boolean startDoubleTouchAnimation(int animationDelay, boolean firstTouch) {
        mFirstAnimation = firstTouch;
        startTouchAnimation(animationDelay, DOUBLE_TOUCH_PRESSURE_TIME, SECOND_TOUCH_OFFSET);
        return !mFirstAnimation;
    }

    public void startTouchAndHoldAnimation(int animationDelay) {
        startTouchAnimation(animationDelay, TOUCH_AND_HOLD_PRESSURE_TIME);
    }

    private void startTouchAnimation(int animationDelay, long touchTime) {
        startTouchAnimation(animationDelay, touchTime, 0);
    }

    private void startTouchAnimation(int animationDelay, final long touchTime, long secondTouchOffset) {
        long startOffset = (animationDelay > 0) ? animationDelay * Constants.MILLISECONDS : Constants.DEFAULT_ANIMATION_DELAY * Constants.MILLISECONDS ;

        if (isSecondTouchOfDoubleTouchGesture(secondTouchOffset)) {
            startOffset = secondTouchOffset;
        }

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(getZoomOutAnimation(startOffset, touchTime));
        animationSet.addAnimation(getZoomInAnimation(startOffset, touchTime));

        mView.setAnimation(animationSet);

        animationSet.start();
    }

    private boolean isSecondTouchOfDoubleTouchGesture(long secondTouchOffset) {
        return secondTouchOffset > 0 && !mFirstAnimation;
    }

    private ScaleAnimation getZoomOutAnimation(long startOffset, final long touchTime) {
        ScaleAnimation zoomOut = new ScaleAnimation(1f, Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR);
        zoomOut.setStartOffset(startOffset);
        zoomOut.setDuration(Constants.SCALE_DURATION);
        zoomOut.setFillAfter(true);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mGestureListener.onZoomOutEnd();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGestureListener.onTouchEnd();
                    }
                }, touchTime);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return zoomOut;
    }

    private ScaleAnimation getZoomInAnimation(long startOffset, long touchTime) {
        ScaleAnimation zoomIn = new ScaleAnimation(Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR, 1f);
        zoomIn.setStartOffset(startOffset+ Constants.SCALE_DURATION+touchTime);
        zoomIn.setDuration(Constants.SCALE_DURATION);
        zoomIn.setFillAfter(true);
        return zoomIn;
    }

}