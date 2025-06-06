package ai.elimu.startguide.content.swipe

import ai.elimu.startguide.databinding.ItemMainBinding
import android.content.res.TypedArray
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by GSC on 30/01/2017.
 */
class SwipeUpDownAdapter(private val images: TypedArray, private val listener: OnScrollListener) :
    RecyclerView.Adapter<SwipeUpDownAdapter.ViewHolder?>() {
    private var isDetectScrollUpActive = true
    private var isDetectScrollDownActive = false

    interface OnScrollListener {
        fun onLastItemReached()
        fun onFirstItemReached()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mLeftView.setImageResource(images.getResourceId(position * 2, -1))
        holder.mRightView.setImageResource(images.getResourceId(position * 2 + 1, -1))
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (this.isDetectScrollUpActive && isLastItemVisible(holder.adapterPosition)) {
            Log.d(javaClass.getName(), "Last item reached")
            setDetectUpActive(false)
            setDetectDownActive(true)

            listener.onLastItemReached()
        } else if (this.isDetectScrollDownActive && isFirstItemVisible(holder.adapterPosition)) {
            Log.d(javaClass.getName(), "First item reached")
            listener.onFirstItemReached()
        }
    }

    override fun getItemCount(): Int {
        return images.length() / 2
    }

    private fun setDetectUpActive(activeUpDetect: Boolean) {
        this.isDetectScrollUpActive = activeUpDetect
    }

    private fun setDetectDownActive(activeDownDetect: Boolean) {
        this.isDetectScrollDownActive = activeDownDetect
    }

    private fun isLastItemVisible(position: Int): Boolean {
        return (position == itemCount - 1)
    }

    private fun isFirstItemVisible(position: Int): Boolean {
        return (position == 0)
    }

    class ViewHolder(binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        var mLeftView: ImageView = binding.leftImage
        var mRightView: ImageView = binding.rightImage
    }
}