package ai.elimu.handgesture

import ai.elimu.handgesture.Gestures.HandGesture
import ai.elimu.handgesture.Gestures.getAnimationResource
import ai.elimu.handgesture.Gestures.getHandGesture
import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

/**
 */
class HandView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs), HandGestureListener {
    private var mView: View? = null
    private var mHandView: ImageView? = null

    private var mAnimationHelper: AnimationHelper? = null
    private var handGesture: HandGesture? = null
    private var mHandViewListener: HandViewListener? = null

    private var mAnimationType = 0
    private var mHideOnTouch = true
    private var mRepeatAnimation = true

    //Delay in seconds
    private var mAnimationDelay = 0
    private var mTranslateX = 0
    private var mTranslateY = 0
    private var mTranslationDuration = 0f

    //Flag for double touch gesture
    private var firstTouch = true
    private var mDetectTouchEvent = false

    // used in XML layout file
    // used in view creation programmatically
    init {
        init(context, attrs)
    }

    fun setHandViewListener(listener: HandViewListener?) {
        mHandViewListener = listener
    }

    fun setHideOnTouch(hideOnTouch: Boolean) {
        mHideOnTouch = hideOnTouch
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mView = inflate(context, R.layout.hand_layout, this)

        mHandView = mView?.findViewById<View?>(R.id.animated_hand) as? ImageView

        if (attrs != null) {
            // Attribute initialization
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HandView, 0, 0)

            mAnimationType = typedArray.getInt(R.styleable.HandView_animationType, -1)
            mHideOnTouch = typedArray.getBoolean(R.styleable.HandView_hideOnTouch, mHideOnTouch)
            mRepeatAnimation =
                typedArray.getBoolean(R.styleable.HandView_repeatAnimation, mRepeatAnimation)
            mAnimationDelay = typedArray.getInt(R.styleable.HandView_animationDelay, 1)

            if (mAnimationType == Gestures.TRANSLATION.ordinal) {
                mTranslateX = typedArray.getInt(R.styleable.HandView_translateX, 0)
                mTranslateY = typedArray.getInt(R.styleable.HandView_translateY, 0)
                mTranslationDuration =
                    typedArray.getFloat(R.styleable.HandView_transition_duration, 1f)
            }

            typedArray.recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mHideOnTouch && mDetectTouchEvent) {
            visibility = GONE
            if (mAnimationHelper != null) {
                mAnimationHelper?.stopAnimation()
            }
        }
        return false
    }

    fun startAnimation() {
        if (mAnimationType >= 0) {
            val gesture = getHandGesture(mAnimationType)

            if (!gesture!!.isTranslation) {
                startGesture(gesture)
            } else if (gesture.isCustomTranslation) {
                startCustomAnimation()
            } else {
                startAnimation(getAnimationResource(mAnimationType))
            }
        } else {
            Log.e(javaClass.getName(), "Animation type not set")
        }
    }

    fun startAnimation(handAnimation: HandGesture) {
        if (!handAnimation.isTranslation) {
            startGesture(handAnimation)
        } else if (handAnimation.isCustomTranslation) {
            startCustomAnimation()
        } else {
            startAnimation(handAnimation.animationResource)
        }
    }

    fun startAnimation(idAnimResource: Int) {
        mDetectTouchEvent = false
        mAnimationHelper = AnimationHelper(context, idAnimResource, this)
        mAnimationHelper?.isRepeatMode = mRepeatAnimation
        mAnimationHelper?.animateView(this, mAnimationDelay.toLong())
    }

    private fun startCustomAnimation() {
        mDetectTouchEvent = false
        mAnimationHelper = AnimationHelper(mTranslateX, mTranslateY, mTranslationDuration, this)
        mAnimationHelper?.isRepeatMode = mRepeatAnimation
        mAnimationHelper?.animateView(this, mAnimationDelay.toLong())
    }

    fun stopAnimation() {
        mHideOnTouch = true
        onTouchEvent(null)
    }

    private fun startGesture(handGesture: HandGesture) {
        mDetectTouchEvent = false
        this.handGesture = handGesture
        if (handGesture == Gestures.SINGLE_TAP) {
            GestureHelper(this, this).startOneTouchAnimation(mAnimationDelay)
        } else if (handGesture == Gestures.DOUBLE_TAP) {
            firstTouch =
                GestureHelper(this, this).startDoubleTouchAnimation(mAnimationDelay, firstTouch)
        } else if (handGesture == Gestures.PRESS_AND_HOLD) {
            GestureHelper(this, this).startTouchAndHoldAnimation(mAnimationDelay)
        }
    }

    private fun startTouch() {
        mHandView?.setImageResource(R.drawable.animation_vector_touch_on)
        (mHandView?.getDrawable() as Animatable).start()
    }

    private fun endTouch() {
        mHandView?.setImageResource(R.drawable.animation_vector_touch_off)
        (mHandView?.getDrawable() as? Animatable)?.start()
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        mHandViewListener?.onHandAnimationEnd()
        if (handGesture != null && mRepeatAnimation) {
            startGesture(handGesture!!)
        }
    }

    //region HandGestureListener
    override fun onAnimationStarted() {
        mDetectTouchEvent = true
    }

    override fun onZoomOutEnd() {
        startTouch()
    }

    override fun onTouchStart() {
        startTouch()
    }

    override fun onTouchEnd() {
        endTouch()
    } //endregion
}