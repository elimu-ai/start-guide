package ai.elimu.startguide

import ai.elimu.handgesture.Gestures
import ai.elimu.startguide.databinding.ActivityTestGestureBinding
import android.app.Activity
import android.os.Bundle

class TestGestureActivity : Activity() {

    private lateinit var binding: ActivityTestGestureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestGestureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singleTap.startAnimation(Gestures.SINGLE_TAP)

        binding.doubleTap.startAnimation(Gestures.DOUBLE_TAP)

        binding.pressAndHold.startAnimation(Gestures.PRESS_AND_HOLD)

        binding.translation.startAnimation()
    }
}
