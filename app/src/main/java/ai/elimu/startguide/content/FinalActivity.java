package ai.elimu.startguide.content;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ai.elimu.startguide.R;
import ai.elimu.startguide.util.MediaPlayerHelper;

import static ai.elimu.startguide.util.MediaPlayerHelper.DEFAULT_PLAYER_DELAY;


public class FinalActivity extends AppCompatActivity {

    private ImageView mFinalCheckmarkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        mFinalCheckmarkImageView = (ImageView) findViewById(R.id.final_checkmark);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Animate checkmark
        mFinalCheckmarkImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFinalCheckmarkImageView.setVisibility(View.VISIBLE);
                Drawable drawable = mFinalCheckmarkImageView.getDrawable();
                ((Animatable) drawable).start();

                playGoodJob();
            }
        }, DEFAULT_PLAYER_DELAY);
    }

    private void playGoodJob() {
        MediaPlayerHelper.play(this, R.raw.good_job, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, DEFAULT_PLAYER_DELAY);
            }
        });
    }
}
