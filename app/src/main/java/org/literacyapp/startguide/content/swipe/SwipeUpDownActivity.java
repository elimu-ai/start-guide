package org.literacyapp.startguide.content.swipe;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.literacyapp.startguide.R;
import org.literacyapp.startguide.util.AnimationHelper;


/**
 *
 */
public class SwipeUpDownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TypedArray imagesArray = getResources().obtainTypedArray(R.array.image_files);
        SwipeUpDownAdapter adapter = new SwipeUpDownAdapter(imagesArray);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // TODO: 04/02/2017 play "Swipe to scroll up"
        //MediaPlayerHelper.play(this, R.raw.);

        AnimationHelper.animateView(this, findViewById(R.id.hand), R.anim.slide_up);
    }
}