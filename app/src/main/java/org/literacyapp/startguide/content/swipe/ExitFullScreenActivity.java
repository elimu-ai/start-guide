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
import org.literacyapp.startguide.util.MediaPlayerHelper;

public class ExitFullScreenActivity extends Activity {

    private HandView mHandView;
    private HandView mHandViewBottom;
    private View mBottomBar;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_full_screen);

        mHandView = (HandView) findViewById(R.id.hand_view);
        mHandViewBottom = (HandView) findViewById(R.id.hand_view_bottom);
        mBottomBar = findViewById(R.id.bottom_bar);

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
        // TODO: 04/05/2017 audios en/sw Slide down from outside the screen
        MediaPlayerHelper.playWithDelay(this, R.raw.slide_down_from_outside_the_screen, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showExitFullScreenAnimation();
            }
        });
    }

    /**
     * Hand animation to explain exit full screen
     */
    private void showExitFullScreenAnimation() {
        mHandView.startAnimation(Gestures.MOVE_DOWN);
    }

    private void onGestureDetected() {
        mHandView.stopAnimation();
        mHandView.setVisibility(View.GONE);
        showBottomAnimation();
    }

    private void showBottomAnimation() {
        mBottomBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandViewBottom.setVisibility(View.VISIBLE);
                mHandViewBottom.startAnimation();
            }
        }, 500);
    }

    private void navigateToFinal() {
        finish();
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }
}
