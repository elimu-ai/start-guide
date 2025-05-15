package ai.elimu.handgesture

import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation

/**
 */
class GestureHelper(private val mView: View, private val mGestureListener: HandGestureListener) {
    private var mFirstAnimation = true

    fun startOneTouchAnimation(animationDelay: Int) {
        startTouchAnimation(animationDelay, ONE_TOUCH_PRESSURE_TIME)
    }

    fun startDoubleTouchAnimation(animationDelay: Int, firstTouch: Boolean): Boolean {
        mFirstAnimation = firstTouch
        startTouchAnimation(animationDelay, DOUBLE_TOUCH_PRESSURE_TIME, SECOND_TOUCH_OFFSET)
        return !mFirstAnimation
    }

    fun startTouchAndHoldAnimation(animationDelay: Int) {
        startTouchAnimation(animationDelay, TOUCH_AND_HOLD_PRESSURE_TIME)
    }

    private fun startTouchAnimation(
        animationDelay: Int,
        touchTime: Long,
        secondTouchOffset: Long = 0
    ) {
        var startOffset =
            if (animationDelay > 0) (animationDelay * Constants.MILLISECONDS).toLong() else Constants.DEFAULT_ANIMATION_DELAY * Constants.MILLISECONDS

        if (isSecondTouchOfDoubleTouchGesture(secondTouchOffset)) {
            startOffset = secondTouchOffset
        }

        val animationSet = AnimationSet(false)
        animationSet.addAnimation(getZoomOutAnimation(startOffset, touchTime))
        animationSet.addAnimation(getZoomInAnimation(startOffset, touchTime))

        mView.setAnimation(animationSet)

        animationSet.start()
    }

    private fun isSecondTouchOfDoubleTouchGesture(secondTouchOffset: Long): Boolean {
        return secondTouchOffset > 0 && !mFirstAnimation
    }

    private fun getZoomOutAnimation(startOffset: Long, touchTime: Long): ScaleAnimation {
        val zoomOut = ScaleAnimation(1f, Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR)
        zoomOut.setStartOffset(startOffset)
        zoomOut.setDuration(Constants.SCALE_DURATION)
        zoomOut.setFillAfter(true)
        zoomOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                mGestureListener.onZoomOutEnd()

                Handler().postDelayed(object : Runnable {
                    override fun run() {
                        mGestureListener.onTouchEnd()
                    }
                }, touchTime)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        return zoomOut
    }

    private fun getZoomInAnimation(startOffset: Long, touchTime: Long): ScaleAnimation {
        val zoomIn = ScaleAnimation(Constants.SCALE_FACTOR, 1f, Constants.SCALE_FACTOR, 1f)
        zoomIn.setStartOffset(startOffset + Constants.SCALE_DURATION + touchTime)
        zoomIn.setDuration(Constants.SCALE_DURATION)
        zoomIn.setFillAfter(true)
        return zoomIn
    }

    companion object {
        private const val ONE_TOUCH_PRESSURE_TIME: Long = 600
        private const val DOUBLE_TOUCH_PRESSURE_TIME: Long = 100
        private const val TOUCH_AND_HOLD_PRESSURE_TIME: Long = 1400

        //Offset for second touch in double touch gesture
        private const val SECOND_TOUCH_OFFSET: Long = 100
    }
}