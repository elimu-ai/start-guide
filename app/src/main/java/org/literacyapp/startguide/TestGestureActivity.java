package org.literacyapp.startguide;

import android.app.Activity;
import android.os.Bundle;

import org.literacyapp.handgesture.HandView;

import static org.literacyapp.handgesture.Gestures.DOUBLE_TAP;
import static org.literacyapp.handgesture.Gestures.PRESS_AND_HOLD;
import static org.literacyapp.handgesture.Gestures.SINGLE_TAP;

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
    }
}
