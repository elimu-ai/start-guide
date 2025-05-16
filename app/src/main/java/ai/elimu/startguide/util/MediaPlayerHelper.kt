package ai.elimu.startguide.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Handler
import android.util.Log

/**
 * Utility class which helps releasing the [MediaPlayer] instance after
 * finishing playing the audio.
 *
 *
 *
 * See https://developer.android.com/reference/android/media/MediaPlayer.html#create%28android.content.Context,%20int%29
 */
object MediaPlayerHelper {
    const val DEFAULT_PLAYER_DELAY: Long = 1000

    @JvmOverloads
    fun play(context: Context?, resId: Int, listener: MediaPlayerListener? = null) {
        Log.i(MediaPlayerHelper::class.java.getName(), "play")

        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                mediaPlayer.release()
                listener?.onCompletion()
            }
        })
        mediaPlayer.start()
    }

    /**
     * Play media player with delay DEFAULT_PLAYER_DELAY
     */
    fun playWithDelay(context: Context?, resId: Int, listener: MediaPlayerListener?) {
        playWithDelay(context, resId, DEFAULT_PLAYER_DELAY, listener)
    }

    fun playWithDelay(context: Context?, resId: Int, delay: Long, listener: MediaPlayerListener?) {
        Handler().postDelayed(object : Runnable {
            override fun run() {
                play(context, resId, listener)
            }
        }, delay)
    }

    interface MediaPlayerListener {
        fun onCompletion()
    }
}
