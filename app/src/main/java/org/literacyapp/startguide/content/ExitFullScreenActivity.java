package org.literacyapp.startguide.content;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import org.literacyapp.handgesture.HandView;
import org.literacyapp.handgesture.HandViewListener;
import org.literacyapp.startguide.R;
import org.literacyapp.startguide.util.MediaPlayerHelper;

public class ExitFullScreenActivity extends Activity {

    private static final long SHORT_PLAYER_DELAY = 500;

    private HandView mHandView;
    private HandView mHandViewBottom;
    private View mBottomBar;
    private View mDecorView;

    private boolean mAnimationCompleted = false;

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
//                            onGestureDetected();
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
        if (mAnimationCompleted) {
            mHandView.onTouchEvent(event);
            mHandViewBottom.onTouchEvent(event);
            mBottomBar.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onGestureDetected();
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
        mHandView.setVisibility(View.VISIBLE);
        mHandView.setHandViewListener(new HandViewListener() {
            @Override
            public void onAnimationEnd() {
                mHandView.stopAnimation();
                mBottomBar.setVisibility(View.VISIBLE);

                playTouchTheArrow();
            }
        });
        mHandView.startAnimation();
    }

    private void playTouchTheArrow() {
        MediaPlayerHelper.playWithDelay(this, R.raw.and_touch_the_arrow_below, SHORT_PLAYER_DELAY,
                new MediaPlayerHelper.MediaPlayerListener() {
                    @Override
                    public void onCompletion() {
                        showBottomAnimation();
                }
        });
    }

    private void showBottomAnimation() {
        mHandViewBottom.setVisibility(View.VISIBLE);
        mHandViewBottom.startAnimation();
        mHandViewBottom.setHandViewListener(new HandViewListener() {
            @Override
            public void onAnimationEnd() {
                mBottomBar.setVisibility(View.GONE);
                mHandViewBottom.setVisibility(View.GONE);

                mAnimationCompleted = true;

                mHandView.setVisibility(View.VISIBLE);
                playExitFullScreen();
            }
        });
    }

    private void onGestureDetected() {
        if (mAnimationCompleted) {
            mHandView.stopAnimation();
            mHandView.setVisibility(View.GONE);
            mHandViewBottom.stopAnimation();
            mHandViewBottom.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
            navigateToFinal();
        }
    }

    private void navigateToFinal() {
        finish();
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }
}
