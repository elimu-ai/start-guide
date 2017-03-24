package org.literacyapp.handgesture;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 */
public class HandView extends RelativeLayout implements AnimationHelper.TouchListener {

    private View mView;

    private AnimationHelper mAnimationHelper;

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
            startAnimation(Gestures.getAnimationResource(mAnimationType));
        } else {
            Log.d(getClass().getName(), "Animation type is not stablished");
        }
    }

    public void startAnimation(Gestures.HandGesture handAnimation) {
        startAnimation(handAnimation.getAnimationResource());
    }

    public void startAnimation(int idAnimResource) {
        mAnimationHelper = new AnimationHelper(getContext(), idAnimResource, this);
        mAnimationHelper.setRepeatMode(mRepeatAnimation);
        mAnimationHelper.animateView(this, mAnimationDelay*1000);
    }

    public void startTouch() {
        mView.findViewById(R.id.circles).setVisibility(VISIBLE);
    }

    public void endTouch() {
        mView.findViewById(R.id.circles).setVisibility(GONE);
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
}
