package ai.elimu.startguide.content

import ai.elimu.handgesture.HandViewListener
import ai.elimu.startguide.R
import ai.elimu.startguide.databinding.ActivityExitFullScreenBinding
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener

class ExitFullScreenActivity : Activity() {

    private lateinit var binding: ActivityExitFullScreenBinding

    private var mDecorView: View? = null

    private var mAnimationCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExitFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDecorView = window.decorView

        hideSystemBars()
        playExitFullScreen()
    }

    override fun onResume() {
        super.onResume()
        mDecorView!!.setOnSystemUiVisibilityChangeListener(object :
            OnSystemUiVisibilityChangeListener {
            override fun onSystemUiVisibilityChange(visibility: Int) {
                // System bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // The system bars are visible.
                    Handler().postDelayed(object : Runnable {
                        override fun run() {
                            hideSystemBars()
                            //                            onGestureDetected();
                        }
                    }, 3000)
                } else {
                    // The system bars are NOT visible.
                    hideSystemBars()
                }
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mAnimationCompleted) {
            binding.handView.onTouchEvent(event)
            binding.handViewBottom.onTouchEvent(event)
            binding.bottomBar.visibility = View.GONE
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onGestureDetected()
    }

    private fun hideSystemBars() {
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView!!.setSystemUiVisibility(
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        )
    }

    private fun playExitFullScreen() {
        // TODO: 04/05/2017 audios en/sw Slide down from outside the screen
        MediaPlayerHelper.playWithDelay(
            this,
            R.raw.slide_down_from_outside_the_screen,
            object : MediaPlayerListener {
                override fun onCompletion() {
                    showExitFullScreenAnimation()
                }
            })
    }

    /**
     * Hand animation to explain exit full screen
     */
    private fun showExitFullScreenAnimation() {
        binding.handView.visibility = View.VISIBLE
        binding.handView.setHandViewListener(object : HandViewListener {
            override fun onHandAnimationEnd() {
                binding.handView.stopAnimation()
                binding.bottomBar.visibility = View.VISIBLE

                playTouchTheArrow()
            }
        })
        binding.handView.startAnimation()
    }

    private fun playTouchTheArrow() {
        MediaPlayerHelper.playWithDelay(
            this, R.raw.and_touch_the_arrow_below, SHORT_PLAYER_DELAY,
            object : MediaPlayerListener {
                override fun onCompletion() {
                    showBottomAnimation()
                }
            })
    }

    private fun showBottomAnimation() {
        binding.handViewBottom.visibility = View.VISIBLE
        binding.handViewBottom.startAnimation()
        binding.handViewBottom.setHandViewListener(object : HandViewListener {
            override fun onHandAnimationEnd() {
                binding.bottomBar.visibility = View.GONE
                binding.handViewBottom.visibility = View.GONE

                mAnimationCompleted = true

                binding.handView.visibility = View.VISIBLE
                binding.handView.setHideOnTouch(false)
                playExitFullScreen()
            }
        })
    }

    private fun onGestureDetected() {
        if (mAnimationCompleted) {
            binding.handView.stopAnimation()
            binding.handView.visibility = View.GONE
            binding.handViewBottom.stopAnimation()
            binding.handViewBottom.visibility = View.GONE
            binding.bottomBar.visibility = View.GONE
            navigateToFinal()
        }
    }

    private fun navigateToFinal() {
        finish()
        val intent = Intent(this, FinalActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val SHORT_PLAYER_DELAY: Long = 500
    }
}
