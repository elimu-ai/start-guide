package org.literacyapp.startguide.content.swipe;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import org.literacyapp.handgesture.Gestures;
import org.literacyapp.handgesture.HandView;
import org.literacyapp.handgesture.HandViewListener;
import org.literacyapp.startguide.R;
import org.literacyapp.startguide.util.MediaPlayerHelper;


/**
 *
 */
public class SwipeUpDownActivity extends AppCompatActivity implements SwipeUpDownAdapter.OnScrollListener,
        HandViewListener {

    private static final int NUM_ANIMATIONS_FOR_REPEAT_AUDIO = 3;

    private HandView mHandView;
    private SwipeUpDownAdapter mAdapter;
    private int mNumAnimations;
    private int mAudioResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_up_down);

        //List of images
        TypedArray imagesArray = getResources().obtainTypedArray(R.array.image_files);
        mAdapter = new SwipeUpDownAdapter(imagesArray, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        //Hand view
        mHandView = (HandView) findViewById(R.id.hand);
        mHandView.setHandViewListener(this);

        playMoveBottom();
    }

    private void playMoveBottom() {
        mAudioResId = R.raw.move_to_the_bottom_of_the_list;
        mNumAnimations++;
        MediaPlayerHelper.playWithDelay(this, mAudioResId, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showMoveBottom();
            }
        });
    }

    private void playMoveTop() {
        mAudioResId = R.raw.move_to_the_top_of_the_list;
        mNumAnimations++;
        MediaPlayerHelper.play(this, mAudioResId, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showMoveTop();
            }
        });
    }

    private void playAudio() {
        resetNumAnimations();
        MediaPlayerHelper.play(this, mAudioResId);
    }

    /**
     * Hand animation to explain scroll to the bottom
     */
    private void showMoveBottom() {
        mHandView.startAnimation(Gestures.MOVE_UP);
    }

    /**
     * Hand animation to explain scroll to the top
     */
    private void showMoveTop() {
        resetNumAnimations();

        mHandView.setHideOnTouch(false);
        mHandView.startAnimation(Gestures.MOVE_DOWN);
    }

    private void resetHandPosition() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mHandView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        mHandView.setLayoutParams(params);
        mHandView.setVisibility(View.VISIBLE);
    }

    private void resetNumAnimations() {
        mNumAnimations = 0;
    }

    //region SwipeUpDownAdapter.OnScrollListener
    @Override
    public void onLastItemReached() {
        resetHandPosition();
        mHandView.stopAnimation();
        playMoveTop();
    }

    @Override
    public void onFirstItemReached() {
        Intent intent = new Intent(this, SwipeRightLeftActivity.class);
        startActivity(intent);
    }
    //endregion

    //region HandViewListener

    @Override
    public void onHandAnimationEnd() {
        if (mNumAnimations == NUM_ANIMATIONS_FOR_REPEAT_AUDIO) {
            playAudio();
        }
        mNumAnimations++;
    }

    //endregion
}