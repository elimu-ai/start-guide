package ai.elimu.startguide

import ai.elimu.handgesture.Gestures
import ai.elimu.handgesture.HandView
import android.app.Activity
import android.os.Bundle
import android.view.View

class TestGestureActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_gesture)

        val mSingleTap = findViewById<View?>(R.id.single_tap) as HandView
        mSingleTap.startAnimation(Gestures.SINGLE_TAP)

        val mDoubleTap = findViewById<View?>(R.id.double_tap) as HandView
        mDoubleTap.startAnimation(Gestures.DOUBLE_TAP)

        val mPressAndHold = findViewById<View?>(R.id.press_and_hold) as HandView
        mPressAndHold.startAnimation(Gestures.PRESS_AND_HOLD)

        val mTranslation = findViewById<View?>(R.id.translation) as HandView
        mTranslation.startAnimation()
    }
}
