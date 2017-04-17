package org.literacyapp.startguide.content.swipe;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.literacyapp.handgesture.Gestures;
import org.literacyapp.handgesture.HandView;
import org.literacyapp.startguide.R;
import org.literacyapp.startguide.util.MediaPlayerHelper;

/**
 * Activity that explain swipe right and left
 */
public class SwipeRightLeftActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnTouchListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for
     * each of the sections. We use a {@link FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may
     * be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private HandView mHandView;

    private boolean detectLeft = true;
    private boolean detectRight = true;

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
        mViewPager.setOnTouchListener(this);

        //Hand view
        mHandView = (HandView) findViewById(R.id.hand);

        playMoveLeft();
    }

    private void playMoveLeft() {
        //TODO: 04/02/2017 audio file (en, sw) "Find the images on the right"
        MediaPlayerHelper.playWithDelay(this, R.raw.find_right, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showSlideLeft();
            }
        });
    }

    private void playMoveRight() {
        //TODO: 04/02/2017 audio file (en, sw) "Find the images on the left"
        MediaPlayerHelper.playWithDelay(this, R.raw.find_left, new MediaPlayerHelper.MediaPlayerListener() {
            @Override
            public void onCompletion() {
                showSlideRight();
            }
        });
    }

    /**
     * Swipe to left explanation with audio and hand animation
     */
    private void showSlideLeft() {
        mHandView.startAnimation();
    }

    /**
     * Swipe to right explanation with audio and hand animation
     */
    private void showSlideRight() {
        mHandView.startAnimation(Gestures.MOVE_RIGHT);
    }

    private void resetHandPosition() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)mHandView.getLayoutParams();
        params.anchorGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

        mHandView.setLayoutParams(params);
        mHandView.setVisibility(View.VISIBLE);
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
            // TODO: 12/02/2017 go to the 'exit full screen' explanation
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    //endregion

    //region OnTouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mHandView.onTouchEvent(event);
        return false;
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
