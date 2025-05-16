package ai.elimu.startguide.content.swipe

import ai.elimu.handgesture.Gestures
import ai.elimu.handgesture.HandView
import ai.elimu.handgesture.HandViewListener
import ai.elimu.startguide.R
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 */
class SwipeUpDownActivity : AppCompatActivity(), SwipeUpDownAdapter.OnScrollListener,
    HandViewListener {
    private var mHandView: HandView? = null
    private var mAdapter: SwipeUpDownAdapter? = null
    private var mNumAnimations = 0
    private var mAudioResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_up_down)

        //List of images
        val imagesArray = getResources().obtainTypedArray(R.array.image_files)
        mAdapter = SwipeUpDownAdapter(imagesArray, this)

        val recyclerView = findViewById<View?>(R.id.recyclerView) as RecyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.setAdapter(mAdapter)

        //Hand view
        mHandView = findViewById<View?>(R.id.hand) as HandView
        mHandView!!.setHandViewListener(this)

        playMoveBottom()
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
        mHandView!!.startAnimation(Gestures.MOVE_UP)
    }

    /**
     * Hand animation to explain scroll to the top
     */
    private fun showMoveTop() {
        resetNumAnimations()

        mHandView!!.setHideOnTouch(false)
        mHandView!!.startAnimation(Gestures.MOVE_DOWN)
    }

    private fun resetHandPosition() {
        val params = mHandView!!.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)

        mHandView!!.setLayoutParams(params)
        mHandView!!.visibility = View.VISIBLE
    }

    private fun resetNumAnimations() {
        mNumAnimations = 0
    }

    //region SwipeUpDownAdapter.OnScrollListener
    override fun onLastItemReached() {
        resetHandPosition()
        mHandView!!.stopAnimation()
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