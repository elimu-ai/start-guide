package ai.elimu.handgesture

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

/**
 * Created by GSC on 04/02/2017.
 */
class AnimationHelper : Animation.AnimationListener {
    private var mAnimationListener: HandAnimationListener? = null

    var isRepeatMode: Boolean = false

    private var mHandGestureListener: HandGestureListener? = null
    private val mAnimation: Animation
    private var mView: View? = null

    constructor(context: Context?, idAnim: Int, handGestureListener: HandGestureListener) {
        mAnimation = AnimationUtils.loadAnimation(context, idAnim)

        initAnimationListeners(handGestureListener)
    }

    constructor(
        translateX: Int,
        translateY: Int,
        duration: Float,
        handGestureListener: HandGestureListener
    ) {
        mAnimation = TranslateAnimation(0f, translateX.toFloat(), 0f, translateY.toFloat())
        mAnimation.setDuration((duration * Constants.MILLISECONDS).toLong())

        initAnimationListeners(handGestureListener)
    }

    private fun initAnimationListeners(handGestureListener: HandGestureListener) {
        mAnimation.setAnimationListener(this)
        mHandGestureListener = handGestureListener

        mAnimationListener = object : HandAnimationListener {
            override fun onMakeSmallerEnd() {
                mView!!.setPivotX(.5f)
                mView!!.setPivotY(.5f)
                mView!!.setScaleX(Constants.SCALE_FACTOR)
                mView!!.setScaleY(Constants.SCALE_FACTOR)
                mView!!.startAnimation(mAnimation)
            }
        }
    }

    @JvmOverloads
    fun animateView(view: View, delay: Long = Constants.DEFAULT_ANIMATION_DELAY) {
        this.mView = view
        mView!!.postDelayed(object : Runnable {
            override fun run() {
                mView!!.startAnimation(makeSmallerImage())
                mHandGestureListener!!.onAnimationStarted()
            }
        }, delay * Constants.MILLISECONDS)
    }

    fun stopAnimation() {
        this.isRepeatMode = false
        mView!!.clearAnimation()
    }

    //region Animation.AnimationListener
    override fun onAnimationStart(animation: Animation?) {
        mHandGestureListener!!.onTouchStart()
    }

    override fun onAnimationEnd(animation: Animation?) {
        mHandGestureListener!!.onTouchEnd()
        mView!!.setScaleX(1f)
        mView!!.setScaleY(1f)

        if (this.isRepeatMode) {
            animateView(mView!!)
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    //endregion
    @JvmOverloads
    fun makeSmallerImage(duration: Long = Constants.SCALE_DURATION): ScaleAnimation {
        val scale = ScaleAnimation(1f, Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR)

        scale.setFillEnabled(true)
        scale.setFillAfter(true)
        scale.setDuration(duration)

        scale.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                mAnimationListener!!.onMakeSmallerEnd()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        return scale
    }

    interface HandAnimationListener {
        fun onMakeSmallerEnd()
    }
}