# HandView

### HandView attributes

**animationType**: `moveUp`, `moveDown`, `moveLeft`, `moveRight`, `singleTouch`, `doubleTouch`, `pressAndHold`
    
**hideOnTouch**: Hide view when touching screen. Default: `true`

**repeatAnimation**: Repeat animation indefinitely. Default: `true` 

**animationDelay**: Animation delay in seconds. Default: 1 second

    
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    ... >
 
    <org.literacyapp.handgesture.HandView
        android:id="@+id/hand_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:animationType="moveLeft" />
 
</RelativeLayout>
```


    HandView mHandView = (HandView) findViewById(R.id.hand_view);
    mHandView.startAnimation();


You can use other animations passing the animation to `startAnimation`

    mHandView.startAnimation(R.anim.my_animation);

or

    mHandView.startAnimation(Gestures.MOVE_UP);