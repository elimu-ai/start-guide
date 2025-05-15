package ai.elimu.startguide.content.swipe;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import ai.elimu.handgesture.HandView;
import ai.elimu.handgesture.HandViewListener;
import ai.elimu.startguide.R;
import ai.elimu.startguide.content.ExitFullScreenActivity;
import ai.elimu.startguide.util.MediaPlayerHelper;

/**
 * Activity that explain swipe right and left
 */
public class SwipeRightLeftActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        HandViewListener {

    private static final int NUM_ANIMATIONS_FOR_REPEAT_AUDIO = 3;

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide fragments for
     * each of the sections. We use a {@link androidx.fragment.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may
     * be best to switch to a {@link androidx.fragment.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private HandView mLeftHandView;
    private HandView mRightHandView;

    private boolean detectLeft = true;
    private boolean detectRight = true;

    private int mNumAnimations;
    private int mAudioResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe_right_left);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        //Hand view
        mLeftHandView = (HandView) findViewById(R.id.left_hand);
        mRightHandView = (HandView) findViewById(R.id.right_hand);

        mLeftHandView.setHandViewListener(this);
        mRightHandView.setHandViewListener(this);

        playMoveLeft();
    }

    private void playMoveLeft() {
        mAudioResId = R.raw.find_the_images_to_the_right;
        mNumAnimations++;
        MediaPlayerHelper.playWithDelay(this, mAudioResId, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showSlideLeft();
            }
        });
    }

    private void playMoveRight() {
        mAudioResId = R.raw.find_the_images_to_the_left;
        mNumAnimations++;
        MediaPlayerHelper.playWithDelay(this, mAudioResId, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showSlideRight();
            }
        });
    }

    private void playAudio() {
        resetNumAnimations();
        MediaPlayerHelper.play(this, mAudioResId);
    }

    /**
     * Swipe to left explanation with audio and hand animation
     */
    private void showSlideLeft() {
        mRightHandView.startAnimation();
    }

    /**
     * Swipe to right explanation with audio and hand animation
     */
    private void showSlideRight() {
        mLeftHandView.startAnimation();
        resetNumAnimations();
    }

    private void resetHandPosition() {
        mRightHandView.setHandViewListener(null);
        mRightHandView.onTouchEvent(null);
        mRightHandView.setVisibility(View.GONE);
        mLeftHandView.setVisibility(View.VISIBLE);
    }

    private void resetNumAnimations() {
        mNumAnimations = 0;
    }

    private boolean isDetectLeftActive() {
        return detectLeft;
    }

    private boolean isDetectRightActive() {
        return detectRight;
    }

    private void setDetectLeft(boolean detectLeft) {
        this.detectLeft = detectLeft;
    }

    private void setDetectRight(boolean detectRight) {
        this.detectRight = detectRight;
    }

    //region ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int page) {
        if ((page == 1) && isDetectRightActive()) {
            setDetectLeft(false);
            resetHandPosition();
            playMoveRight();
        } else if ((page == 0) && !isDetectLeftActive()) {
            setDetectRight(false);

            finish();

            Intent intent = new Intent(this, ExitFullScreenActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swipe_right_left, container, false);

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            //Images
            TypedArray imagesArray = getResources().obtainTypedArray(R.array.image_files);

            ImageView leftImage = (ImageView) rootView.findViewById(R.id.left_image);
            ImageView rightImage = (ImageView) rootView.findViewById(R.id.right_image);

            leftImage.setImageResource(imagesArray.getResourceId(sectionNumber*2, -1));
            rightImage.setImageResource(imagesArray.getResourceId(sectionNumber*2 + 1, -1));

            //Colors
            int[] pagerColors = getResources().getIntArray(R.array.pager_colors);

            View pagerContainer = rootView.findViewById(R.id.pager_container);
            pagerContainer.setBackgroundColor(pagerColors[sectionNumber]);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private static final int TOTAL_PAGES = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class above).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }
    }
}
