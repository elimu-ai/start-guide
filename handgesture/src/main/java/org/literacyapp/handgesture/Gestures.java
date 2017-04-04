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
        SINGLE_TAP(0, false),
        DOUBLE_TAP(0, false),
        PRESS_AND_HOLD(0, false);

        private int idAnim;
        private boolean translation;

        HandGesture(int idAnim) {
            this(idAnim, true);
        }

        HandGesture(int idAnim, boolean translation) {
            this.idAnim = idAnim;
            this.translation = translation;
        }

        public int getAnimationResource() {
            return idAnim;
        }

        public boolean isTranslation() {
            return translation;
        }
    }

    public static int getAnimationResource(int animationType) {
        return HandGesture.values()[animationType].getAnimationResource();
    }

    public int getAnimationResource(HandGesture handGesture) {
        return handGesture.getAnimationResource();
    }
}
