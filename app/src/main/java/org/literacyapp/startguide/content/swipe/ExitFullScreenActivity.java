package org.literacyapp.startguide.content.swipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import org.literacyapp.handgesture.Gestures;
import org.literacyapp.handgesture.HandView;
import org.literacyapp.startguide.R;
import org.literacyapp.startguide.content.FinalActivity;

public class ExitFullScreenActivity extends Activity {

    private HandView mHandView;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_full_screen);

        mHandView = (HandView) findViewById(R.id.hand_view);
        mDecorView = getWindow().getDecorView();

        hideSystemBars();
        playExitFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // System bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // The system bars are visible.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideSystemBars();
                            onGestureDetected();
                        }
                    }, 3000);
                } else {
                    // The system bars are NOT visible.
                    hideSystemBars();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHandView.onTouchEvent(event);
        return false;
    }

    private void hideSystemBars() {
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void playExitFullScreen() {
        // TODO: 04/05/2017 audio move bottom to exit full screen
//        MediaPlayerHelper.playWithDelay(this, R.raw.exit_full_screen, new MediaPlayerHelper.MediaPlayerListener() {
//            @Override
//            public void onCompletion() {
                showExitFullScreenAnimation();
//            }
//        });
    }

    /**
     * Hand animation to explain exit full screen
     */
    private void showExitFullScreenAnimation() {
        mHandView.startAnimation(Gestures.MOVE_DOWN);
    }

    private void onGestureDetected() {
        finish();
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }
}
