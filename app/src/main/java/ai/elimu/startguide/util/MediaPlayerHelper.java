package ai.elimu.startguide.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

/**
 * Utility class which helps releasing the {@link MediaPlayer} instance after
 * finishing playing the audio.
 * <p />
 *
 * See https://developer.android.com/reference/android/media/MediaPlayer.html#create%28android.content.Context,%20int%29
 */
public class MediaPlayerHelper {

    public static final long DEFAULT_PLAYER_DELAY = 1000;

    public static void play(Context context, int resId) {
        play(context, resId, null);
    }

    public static void play(Context context, int resId, final MediaPlayerListener listener) {
        Log.i(MediaPlayerHelper.class.getName(), "play");

        final MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                if (listener != null) {
                    listener.onCompletion();
                }
            }
        });
        mediaPlayer.start();
    }

    /**
     * Play media player with delay DEFAULT_PLAYER_DELAY
     */
    public static void playWithDelay(Context context, int resId, MediaPlayerListener listener) {
        playWithDelay(context, resId, DEFAULT_PLAYER_DELAY, listener);
    }

    public static void playWithDelay(final Context context, final int resId, long delay, final MediaPlayerListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                play(context, resId, listener);
            }
        }, delay);
    }

    public interface MediaPlayerListener {
        void onCompletion();
    }
}
