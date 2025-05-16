package ai.elimu.startguide.content

import ai.elimu.startguide.R
import ai.elimu.startguide.databinding.ActivityFinalBinding
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FinalActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        // Animate checkmark
        binding.finalCheckmark.postDelayed(object : Runnable {
            override fun run() {
                binding.finalCheckmark.setVisibility(View.VISIBLE)
                val drawable = binding.finalCheckmark.getDrawable()
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
