# HandView

### HandView attributes

**animationType**: `moveUp`, `moveDown`, `moveLeft`, `moveRight`, `singleTouch`, `doubleTouch`, `pressAndHold`, `translation`
    
**hideOnTouch**: Hide view when touching screen. Default: `true`

**repeatAnimation**: Repeat animation indefinitely. Default: `true` 

**animationDelay**: Animation delay in seconds. Default: 1 second

**translateX** (only for ```animationType="translation"```): Translation in X coordinate from initial hand position.

**translatey** (only for ```animationType="translation"```): Translation in Y coordinate from initial hand position. 

**duration** (only for ```animationType="translation"```): Duration of hand translation in seconds. Default: 1 second.

    
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
    
###### Custom translation

```
<org.literacyapp.handgesture.HandView
    android:id="@+id/hand_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:animationType="translation"
    app:translateX="300"
    app:translateY="-100"
    app:duration="1.5"/>
```
```
HandView mHandView = (HandView) findViewById(R.id.hand_view);
mHandView.startAnimation();
```