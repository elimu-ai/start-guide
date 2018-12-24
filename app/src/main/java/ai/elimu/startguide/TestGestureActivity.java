package ai.elimu.startguide;

import android.app.Activity;
import android.os.Bundle;

import ai.elimu.handgesture.HandView;
import ai.elimu.startguide.R;

import static ai.elimu.handgesture.Gestures.DOUBLE_TAP;
import static ai.elimu.handgesture.Gestures.PRESS_AND_HOLD;
import static ai.elimu.handgesture.Gestures.SINGLE_TAP;

public class TestGestureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gesture);

        HandView mSingleTap = (HandView) findViewById(R.id.single_tap);
        mSingleTap.startAnimation(SINGLE_TAP);

        HandView mDoubleTap = (HandView) findViewById(R.id.double_tap);
        mDoubleTap.startAnimation(DOUBLE_TAP);

        HandView mPressAndHold = (HandView) findViewById(R.id.press_and_hold);
        mPressAndHold.startAnimation(PRESS_AND_HOLD);

        HandView mTranslation = (HandView) findViewById(R.id.translation);
        mTranslation.startAnimation();
    }
}
