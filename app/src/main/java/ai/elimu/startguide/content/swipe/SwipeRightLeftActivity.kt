package ai.elimu.startguide.content.swipe

import ai.elimu.handgesture.HandViewListener
import ai.elimu.startguide.R
import ai.elimu.startguide.content.ExitFullScreenActivity
import ai.elimu.startguide.databinding.ActivitySwipeRightLeftBinding
import ai.elimu.startguide.databinding.FragmentSwipeRightLeftBinding
import ai.elimu.startguide.util.MediaPlayerHelper
import ai.elimu.startguide.util.MediaPlayerHelper.MediaPlayerListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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
    
    private lateinit var binding: ActivitySwipeRightLeftBinding

    private var isDetectLeftActive = true
    private var isDetectRightActive = true

    private var mNumAnimations = 0
    private var mAudioResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySwipeRightLeftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(mSectionsPagerAdapter)
        binding.container.addOnPageChangeListener(this)

        binding.leftHand.setHandViewListener(this)
        binding.rightHand.setHandViewListener(this)

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
        binding.rightHand.startAnimation()
    }

    /**
     * Swipe to right explanation with audio and hand animation
     */
    private fun showSlideRight() {
        binding.leftHand.startAnimation()
        resetNumAnimations()
    }

    private fun resetHandPosition() {
        binding.rightHand.setHandViewListener(null)
        binding.rightHand.onTouchEvent(null)
        binding.rightHand.visibility = View.GONE
        binding.leftHand.visibility = View.VISIBLE
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

            val binding = FragmentSwipeRightLeftBinding.inflate(layoutInflater, container, false)

            val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0

            //Images
            val imagesArray = resources.obtainTypedArray(R.array.image_files)


            binding.leftImage.setImageResource(imagesArray.getResourceId(sectionNumber * 2, -1))
            binding.rightImage.setImageResource(imagesArray.getResourceId(sectionNumber * 2 + 1, -1))

            //Colors
            val pagerColors = resources.getIntArray(R.array.pager_colors)

            binding.pagerContainer.setBackgroundColor(pagerColors[sectionNumber])

            imagesArray.recycle()

            return binding.root
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
