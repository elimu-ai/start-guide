package ai.elimu.startguide.content

import ai.elimu.startguide.R
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FinalActivity : AppCompatActivity() {
    private var mFinalCheckmarkImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        mFinalCheckmarkImageView = findViewById<View?>(R.id.final_checkmark) as ImageView
    }

    override fun onStart() {
        super.onStart()

        // Animate checkmark
        mFinalCheckmarkImageView!!.postDelayed(object : Runnable {
            override fun run() {
                mFinalCheckmarkImageView!!.setVisibility(View.VISIBLE)
                val drawable = mFinalCheckmarkImageView!!.getDrawable()
                (drawable as Animatable).start()

                playGoodJob()
            }
        }, MediaPlayerHelper.DEFAULT_PLAYER_DELAY)
    }

    private fun playGoodJob() {
        MediaPlayerHelper.play(this, R.raw.good_job, object : MediaPlayerListener {
            override fun onCompletion() {
                Handler().postDelayed(object : Runnable {
                    override fun run() {
                        finish()
                    }
                }, MediaPlayerHelper.DEFAULT_PLAYER_DELAY)
            }
        })
    }
}
