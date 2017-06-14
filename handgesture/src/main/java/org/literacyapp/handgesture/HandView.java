package org.literacyapp.handgesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.literacyapp.handgesture.Gestures.HandGesture;

import static org.literacyapp.handgesture.Gestures.DOUBLE_TAP;
import static org.literacyapp.handgesture.Gestures.PRESS_AND_HOLD;
import static org.literacyapp.handgesture.Gestures.SINGLE_TAP;

/**
 */
public class HandView extends RelativeLayout implements HandGestureListener {

    private View mView;
    private ImageView mHandView;

    private AnimationHelper mAnimationHelper;
    private HandGesture handGesture;
    private HandViewListener mHandViewListener;

    private int mAnimationType;
    private boolean mHideOnTouch = true;
    private boolean mRepeatAnimation = true;
    //Delay in seconds
    private int mAnimationDelay;
    //Flag for double touch gesture
    private boolean firstTouch = true;

    // used in view creation programmatically
    public HandView(Context context) {
        this(context, null);
    }

    // used in XML layout file
    public HandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setHandViewListener(HandViewListener listener) {
        mHandViewListener = listener;
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
            if (mAnimationHelper != null) {
                mAnimationHelper.stopAnimation();
            }
            setVisibility(GONE);
        }
        return false;
    }

    public void startAnimation() {
        if (mAnimationType >= 0) {
            HandGesture gesture = Gestures.getHandGesture(mAnimationType);

            if (gesture.isTranslation()) {
                startAnimation(Gestures.getAnimationResource(mAnimationType));
            } else {
                startGesture(gesture);
            }
        } else {
            Log.e(getClass().getName(), "Animation type not set");
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
        mAnimationHelper.animateView(this, mAnimationDelay);
    }

    private void startGesture(HandGesture handGesture) {
        this.handGesture = handGesture;
        if (handGesture.equals(SINGLE_TAP)) {
            new GestureHelper(this, this).startOneTouchAnimation(mAnimationDelay);
        } else if (handGesture.equals(DOUBLE_TAP)) {
            firstTouch = new GestureHelper(this, this).startDoubleTouchAnimation(mAnimationDelay, firstTouch);
        } else if (handGesture.equals(PRESS_AND_HOLD)) {
            new GestureHelper(this, this).startTouchAndHoldAnimation(mAnimationDelay);
        }
    }

    public void stopAnimation() {
        mAnimationHelper.stopAnimation();
    }

    private void startTouch() {
        mHandView.setImageResource(R.drawable.animation_vector_touch_on);
        ((Animatable) mHandView.getDrawable()).start();
    }

    private void endTouch() {
        mHandView.setImageResource(R.drawable.animation_vector_touch_off);
        ((Animatable) mHandView.getDrawable()).start();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        if (mHandViewListener != null) {
            mHandViewListener.onHandAnimationEnd();
        }
        if (handGesture != null && mRepeatAnimation) {
            startGesture(handGesture);
        }
    }

    //region HandGestureListener

    @Override
    public void onZoomOutEnd() {
        startTouch();
    }

    @Override
    public void onTouchStart() {
        startTouch();
    }

    @Override
    public void onTouchEnd() {
        endTouch();
    }

    //endregion
}