# HandView

## Gradle Import

To import the library, you first need to add our repository in app/build.gradle:

```
repositories {
    mavenLocal()
    maven {
        url "https://dl.bintray.com/elimu-ai/maven/"
    }
}
```

Then, add the following dependency:

```
dependencies {
   ...
   compile 'ai.elimu.startguide:1.0.0'
}
``` 

You can add the HandView to the Layout
    
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    ... >
 
    <ai.elimu.handgesture.HandView
        android:id="@+id/hand_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:animationType="moveLeft"
        app:hideOnTouch="false"
        app:repeatAnimation="true"
        app:animationDelay="2"/>
 
</RelativeLayout>
```

and start the animation

    HandView mHandView = (HandView) findViewById(R.id.hand_view);
    mHandView.startAnimation();

or

    mHandView.startAnimation(Gestures.MOVE_UP);

###### Custom translation

```
<ai.elimu.handgesture.HandView
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

#### HandView attributes

**animationType**: `moveUp`, `moveDown`, `moveLeft`, `moveRight`, `singleTouch`, `doubleTouch`, `pressAndHold`, `translation`
    
**hideOnTouch**: Hide view when touching screen. Default: `true`

**repeatAnimation**: Repeat animation indefinitely. Default: `true` 

**animationDelay**: Animation delay in seconds. Default: 1 second

**translateX** (only for ```animationType="translation"```): Translation in X coordinate from initial hand position.

**translatey** (only for ```animationType="translation"```): Translation in Y coordinate from initial hand position. 

**duration** (only for ```animationType="translation"```): Duration of hand translation in seconds. Default: 1 second.

