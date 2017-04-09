package org.literacyapp.handgesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.literacyapp.handgesture.Gestures.HandGesture;

import static org.literacyapp.handgesture.Gestures.DOUBLE_TAP;
import static org.literacyapp.handgesture.Gestures.PRESS_AND_HOLD;
import static org.literacyapp.handgesture.Gestures.SINGLE_TAP;

/**
 */
public class HandView extends RelativeLayout implements AnimationHelper.TouchListener {

    private static final long SCALE_DURATION = 500;

    private View mView;
    private ImageView mHandView;

    private AnimationHelper mAnimationHelper;
    private HandGesture handGesture;

    private int mAnimationType;
    private boolean mHideOnTouch = true;
    private boolean mRepeatAnimation = true;
    //Delay in seconds
    private int mAnimationDelay;


    // used in view creation programmatically
    public HandView(Context context) {
        this(context, null);
    }

    // used in XML layout file
    public HandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = inflate(context, R.layout.hand_layout, this);

        mHandView = (ImageView) mView.findViewById(R.id.animated_hand);

        if (attrs != null) {
            // Attribute initialization
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HandView, 0, 0);

            mAnimationType = typedArray.getInt(R.styleable.HandView_animationType, -1);
            mHideOnTouch = typedArray.getBoolean(R.styleable.HandView_hideOnTouch, mHideOnTouch);
            mRepeatAnimation = typedArray.getBoolean(R.styleable.HandView_repeatAnimation, mRepeatAnimation);
            mAnimationDelay = typedArray.getInt(R.styleable.HandView_animationDelay, 1);

            typedArray.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getVisibility() == VISIBLE && mHideOnTouch) {
            setVisibility(GONE);
            if (mAnimationHelper != null) {
                mAnimationHelper.stopAnimation();
            }
            return true;
        }
        return false;
    }

    public void startAnimation() {
        if (mAnimationType >= 0) {
            startAnimation(HandGesture.values()[mAnimationType].getAnimationResource());
        } else {
            Log.d(getClass().getName(), "Animation type is not stablished");
        }
    }

    public void startAnimation(HandGesture handAnimation) {
        if (handAnimation.isTranslation()) {
            startAnimation(handAnimation.getAnimationResource());
        } else {
            startGesture(handAnimation);
        }
    }

    public void startAnimation(int idAnimResource) {
        mAnimationHelper = new AnimationHelper(getContext(), idAnimResource, this);
        mAnimationHelper.setRepeatMode(mRepeatAnimation);
        mAnimationHelper.animateView(this, mAnimationDelay*1000);
    }

    //region TouchListener
    @Override
    public void onTouchStart() {
        startTouch();
    }

    @Override
    public void onTouchEnd() {
        endTouch();
    }
    //endregion

    private void startTouch() {
        mHandView.setImageResource(R.drawable.animation_vector_touch_on);
        ((Animatable) mHandView.getDrawable()).start();
    }

    private void endTouch() {
        mHandView.setImageResource(R.drawable.animation_vector_touch_off);
        ((Animatable) mHandView.getDrawable()).start();
    }

    private void startGesture(HandGesture handGesture) {
        this.handGesture = handGesture;
        if (handGesture.equals(SINGLE_TAP)) {
            startOneTouchAnimation();
        } else if (handGesture.equals(DOUBLE_TAP)) {
            startDoubleTouchAnimation();
        } else if (handGesture.equals(PRESS_AND_HOLD)) {
            startPressAndHoldAnimation();
        }
    }

    private void startOneTouchAnimation() {
        startTouchAnimation(1000, false);
    }

    private void startDoubleTouchAnimation() {
        startTouchAnimation(200, true);
    }

    private void startPressAndHoldAnimation() {
        startTouchAnimation(2000, false);
    }

    private void startTouchAnimation(final long pressTime, boolean repeat) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);

        ScaleAnimation scale = new ScaleAnimation(1f, 0.9f, 1f, 0.9f);
        scale.setStartOffset(1000);
        scale.setDuration(SCALE_DURATION);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startTouch();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endTouch();
                    }
                }, pressTime);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        animationSet.addAnimation(scale);

        ScaleAnimation scale2 = new ScaleAnimation(0.9f, 1f, 0.9f, 1f);
        scale2.setStartOffset(1000+SCALE_DURATION+pressTime);
        scale2.setDuration(SCALE_DURATION);
        animationSet.addAnimation(scale2);

        mView.setAnimation(animationSet);

        if (repeat)
            animationSet.setRepeatCount(2);

        animationSet.start();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (handGesture != null) {
            startGesture(handGesture);
        }
    }
}