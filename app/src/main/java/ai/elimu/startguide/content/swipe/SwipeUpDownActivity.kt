package ai.elimu.startguide.content.swipe

import ai.elimu.common.utils.ui.setLightStatusBar
import ai.elimu.handgesture.Gestures
import ai.elimu.handgesture.HandViewListener
import ai.elimu.startguide.R
import ai.elimu.startguide.databinding.ActivitySwipeUpDownBinding
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

/**
 *
 */
class SwipeUpDownActivity : AppCompatActivity(), SwipeUpDownAdapter.OnScrollListener,
    HandViewListener {

    private lateinit var binding: ActivitySwipeUpDownBinding
    private var mAdapter: SwipeUpDownAdapter? = null
    private var mNumAnimations = 0
    private var mAudioResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeUpDownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //List of images
        val imagesArray = getResources().obtainTypedArray(R.array.image_files)
        mAdapter = SwipeUpDownAdapter(imagesArray, this)

        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        binding.recyclerView.setAdapter(mAdapter)

        //Hand view
        binding.hand.setHandViewListener(this)

        playMoveBottom()

        window.apply {
            setLightStatusBar()
        }
    }

    private fun playMoveBottom() {
        mAudioResId = R.raw.move_to_the_bottom_of_the_list
        mNumAnimations++
        MediaPlayerHelper.playWithDelay(this, mAudioResId, object : MediaPlayerListener {
            override fun onCompletion() {
                showMoveBottom()
            }
        })
    }

    private fun playMoveTop() {
        mAudioResId = R.raw.move_to_the_top_of_the_list
        mNumAnimations++
        MediaPlayerHelper.play(this, mAudioResId, object : MediaPlayerListener {
            override fun onCompletion() {
                showMoveTop()
            }
        })
    }

    private fun playAudio() {
        resetNumAnimations()
        MediaPlayerHelper.play(this, mAudioResId)
    }

    /**
     * Hand animation to explain scroll to the bottom
     */
    private fun showMoveBottom() {
        binding.hand.startAnimation(Gestures.MOVE_UP)
    }

    /**
     * Hand animation to explain scroll to the top
     */
    private fun showMoveTop() {
        resetNumAnimations()

        binding.hand.setHideOnTouch(false)
        binding.hand.startAnimation(Gestures.MOVE_DOWN)
    }

    private fun resetHandPosition() {
        val params = binding.hand.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)

        binding.hand.setLayoutParams(params)
        binding.hand.visibility = View.VISIBLE
    }

    private fun resetNumAnimations() {
        mNumAnimations = 0
    }

    //region SwipeUpDownAdapter.OnScrollListener
    override fun onLastItemReached() {
        resetHandPosition()
        binding.hand.stopAnimation()
        playMoveTop()
    }

    override fun onFirstItemReached() {
        val intent = Intent(this, SwipeRightLeftActivity::class.java)
        startActivity(intent)
    }

    //endregion
    //region HandViewListener
    override fun onHandAnimationEnd() {
        if (mNumAnimations == NUM_ANIMATIONS_FOR_REPEAT_AUDIO) {
            playAudio()
        }
        mNumAnimations++
    } //endregion

    companion object {
        private const val NUM_ANIMATIONS_FOR_REPEAT_AUDIO = 3
    }
}