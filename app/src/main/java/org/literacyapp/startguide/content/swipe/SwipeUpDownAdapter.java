package org.literacyapp.startguide.content.swipe;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.literacyapp.startguide.R;

/**
 * Created by GSC on 30/01/2017.
 */

public class SwipeUpDownAdapter extends RecyclerView.Adapter<SwipeUpDownAdapter.ViewHolder> {

    private TypedArray images;
    private boolean detectUp = true;
    private boolean detectDown = false;
    private OnScrollListener listener;

    public interface OnScrollListener {
        void onLastItemReached();
        void onFirstItemReached();
    }

    public SwipeUpDownAdapter(TypedArray images, OnScrollListener listener) {
        this.images = images;
        this.listener = listener;
    }

    @Override
    public SwipeUpDownAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeUpDownAdapter.ViewHolder holder, int position) {
        holder.mLeftView.setImageResource(images.getResourceId(position*2, -1));
        holder.mRightView.setImageResource(images.getResourceId(position*2 + 1, -1));

        if (isDetectScrollUpActive() && isLastItemVisible(position)) {
            Log.d(getClass().getName(), "Last item reached");
            setDetectUpActive(false);
            setDetectDownActive(true);

            listener.onLastItemReached();
        } else if (isDetectScrollDownActive() && isFirstItemVisible(position)) {
            Log.d(getClass().getName(), "First item reached");
            listener.onFirstItemReached();
        }
    }

    @Override
    public int getItemCount() {
        return images.length() / 2;
    }

    private void setDetectUpActive(boolean activeUpDetect) {
        detectUp = activeUpDetect;
    }

    private void setDetectDownActive(boolean activeDownDetect) {
        detectDown = activeDownDetect;
    }

    private boolean isDetectScrollUpActive() {
        return detectUp;
    }

    private boolean isDetectScrollDownActive() {
        return detectDown;
    }

    private boolean isLastItemVisible(int position) {
        return (position == getItemCount() - 1);
    }

    private boolean isFirstItemVisible(int position) {
        return (position == 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mLeftView;
        public ImageView mRightView;

        public ViewHolder(View v) {
            super(v);
            mLeftView = (ImageView) v.findViewById(R.id.left_image);
            mRightView = (ImageView) v.findViewById(R.id.right_image);
        }
    }
}