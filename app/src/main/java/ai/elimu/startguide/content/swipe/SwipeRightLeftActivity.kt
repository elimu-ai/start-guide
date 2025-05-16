package ai.elimu.startguide.content.swipe

import ai.elimu.handgesture.HandView
import ai.elimu.handgesture.HandViewListener
import ai.elimu.startguide.R
import ai.elimu.startguide.content.ExitFullScreenActivity
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

/**
 * Activity that explain swipe right and left
 */
class SwipeRightLeftActivity : AppCompatActivity(), OnPageChangeListener, HandViewListener {
    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide fragments for
     * each of the sections. We use a [FragmentPagerAdapter] derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may
     * be best to switch to a [androidx.fragment.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var mViewPager: ViewPager? = null
    private var mLeftHandView: HandView? = null
    private var mRightHandView: HandView? = null

    private var isDetectLeftActive = true
    private var isDetectRightActive = true

    private var mNumAnimations = 0
    private var mAudioResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_swipe_right_left)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(getSupportFragmentManager())

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View?>(R.id.container) as ViewPager
        mViewPager!!.setAdapter(mSectionsPagerAdapter)
        mViewPager!!.addOnPageChangeListener(this)

        //Hand view
        mLeftHandView = findViewById<View?>(R.id.left_hand) as HandView
        mRightHandView = findViewById<View?>(R.id.right_hand) as HandView

        mLeftHandView!!.setHandViewListener(this)
        mRightHandView!!.setHandViewListener(this)

        playMoveLeft()
    }

    private fun playMoveLeft() {
        mAudioResId = R.raw.find_the_images_to_the_right
        mNumAnimations++
        MediaPlayerHelper.playWithDelay(this, mAudioResId, object : MediaPlayerListener {
            override fun onCompletion() {
                showSlideLeft()
            }
        })
    }

    private fun playMoveRight() {
        mAudioResId = R.raw.find_the_images_to_the_left
        mNumAnimations++
        MediaPlayerHelper.playWithDelay(this, mAudioResId, object : MediaPlayerListener {
            override fun onCompletion() {
                showSlideRight()
            }
        })
    }

    private fun playAudio() {
        resetNumAnimations()
        MediaPlayerHelper.play(this, mAudioResId)
    }

    /**
     * Swipe to left explanation with audio and hand animation
     */
    private fun showSlideLeft() {
        mRightHandView!!.startAnimation()
    }

    /**
     * Swipe to right explanation with audio and hand animation
     */
    private fun showSlideRight() {
        mLeftHandView!!.startAnimation()
        resetNumAnimations()
    }

    private fun resetHandPosition() {
        mRightHandView!!.setHandViewListener(null)
        mRightHandView!!.onTouchEvent(null)
        mRightHandView!!.setVisibility(View.GONE)
        mLeftHandView!!.setVisibility(View.VISIBLE)
    }

    private fun resetNumAnimations() {
        mNumAnimations = 0
    }

    private fun setDetectLeft(detectLeft: Boolean) {
        this.isDetectLeftActive = detectLeft
    }

    private fun setDetectRight(detectRight: Boolean) {
        this.isDetectRightActive = detectRight
    }

    //region ViewPager.OnPageChangeListener
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(page: Int) {
        if ((page == 1) && this.isDetectRightActive) {
            setDetectLeft(false)
            resetHandPosition()
            playMoveRight()
        } else if ((page == 0) && !this.isDetectLeftActive) {
            setDetectRight(false)

            finish()

            val intent = Intent(this, ExitFullScreenActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    //endregion
    //region HandViewListener
    override fun onHandAnimationEnd() {
        if (mNumAnimations == NUM_ANIMATIONS_FOR_REPEAT_AUDIO) {
            playAudio()
        }
        mNumAnimations++
    }

    //endregion
    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val rootView = inflater.inflate(R.layout.fragment_swipe_right_left, container, false)

            val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0

            //Images
            val imagesArray = getResources().obtainTypedArray(R.array.image_files)

            val leftImage = rootView.findViewById<View?>(R.id.left_image) as ImageView
            val rightImage = rootView.findViewById<View?>(R.id.right_image) as ImageView

            leftImage.setImageResource(imagesArray.getResourceId(sectionNumber * 2, -1))
            rightImage.setImageResource(imagesArray.getResourceId(sectionNumber * 2 + 1, -1))

            //Colors
            val pagerColors = getResources().getIntArray(R.array.pager_colors)

            val pagerContainer = rootView.findViewById<View>(R.id.pager_container)
            pagerContainer.setBackgroundColor(pagerColors[sectionNumber])

            imagesArray.recycle()

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.setArguments(args)
                return fragment
            }
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class above).
            return PlaceholderFragment.Companion.newInstance(position)
        }

        override fun getCount(): Int {
            return TOTAL_PAGES 
        }
    }

    companion object {
        private const val NUM_ANIMATIONS_FOR_REPEAT_AUDIO = 3
        private const val TOTAL_PAGES = 2
    }
}
