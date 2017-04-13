package org.literacyapp.handgesture;

/**
 */
public class Gestures {

    public static HandGesture MOVE_UP = HandGesture.MOVE_UP;
    public static HandGesture MOVE_DOWN = HandGesture.MOVE_DOWN;
    public static HandGesture MOVE_LEFT = HandGesture.MOVE_LEFT;
    public static HandGesture MOVE_RIGHT = HandGesture.MOVE_RIGHT;
    public static HandGesture SINGLE_TAP = HandGesture.SINGLE_TAP;
    public static HandGesture DOUBLE_TAP = HandGesture.DOUBLE_TAP;
    public static HandGesture PRESS_AND_HOLD = HandGesture.PRESS_AND_HOLD;

    protected enum HandGesture {
        MOVE_UP(R.anim.slide_up),
        MOVE_DOWN(R.anim.slide_down),
        MOVE_LEFT(R.anim.slide_left),
        MOVE_RIGHT(R.anim.slide_right),
        SINGLE_TAP(0),
        DOUBLE_TAP(0),
        PRESS_AND_HOLD(0);

        private int idAnim;

        HandGesture(int idAnim) {
            this.idAnim = idAnim;
        }

        public int getAnimationResource() {
            return idAnim;
        }

        public boolean isTranslation() {
            return idAnim > 0;
        }
    }

    public int getAnimationResource(HandGesture handGesture) {
        return handGesture.getAnimationResource();
    }
}
