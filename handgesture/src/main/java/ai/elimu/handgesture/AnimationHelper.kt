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
                mView?.pivotX = .5f
                mView?.pivotY = .5f
                mView?.scaleX = Constants.SCALE_FACTOR
                mView?.scaleY = Constants.SCALE_FACTOR
                mView?.startAnimation(mAnimation)
            }
        }
    }

    @JvmOverloads
    fun animateView(view: View, delay: Long = Constants.DEFAULT_ANIMATION_DELAY) {
        this.mView = view
        mView?.postDelayed(object : Runnable {
            override fun run() {
                mView?.startAnimation(makeSmallerImage())
                mHandGestureListener?.onAnimationStarted()
            }
        }, delay * Constants.MILLISECONDS)
    }

    fun stopAnimation() {
        this.isRepeatMode = false
        mView?.clearAnimation()
    }

    //region Animation.AnimationListener
    override fun onAnimationStart(animation: Animation?) {
        mHandGestureListener?.onTouchStart()
    }

    override fun onAnimationEnd(animation: Animation?) {
        mHandGestureListener?.onTouchEnd()
        mView?.scaleX = 1f
        mView?.scaleY = 1f

        if (this.isRepeatMode) {
            mView?.let { view ->
                animateView(view)
            }
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    //endregion
    @JvmOverloads
    fun makeSmallerImage(duration: Long = Constants.SCALE_DURATION): ScaleAnimation {
        val scale = ScaleAnimation(1f, Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR)

        scale.isFillEnabled = true
        scale.fillAfter = true
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