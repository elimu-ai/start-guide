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
    public static HandGesture TRANSLATION = HandGesture.TRANSLATION;

    protected enum HandGesture {
        MOVE_UP(R.anim.slide_up),
        MOVE_DOWN(R.anim.slide_down),
        MOVE_LEFT(R.anim.slide_left),
        MOVE_RIGHT(R.anim.slide_right),
        SINGLE_TAP(0),
        DOUBLE_TAP(0),
        PRESS_AND_HOLD(0),
        TRANSLATION(true);

        private int idAnim;
        private boolean customTranslation = false;

        HandGesture(int idAnim) {
            this.idAnim = idAnim;
        }

        HandGesture(boolean translation) {
            this.customTranslation = translation;
        }

        public int getAnimationResource() {
            return idAnim;
        }

        public boolean isTranslation() {
            return (idAnim > 0) || customTranslation;
        }

        public boolean isCustomTranslation() {
            return customTranslation;
        }
    }

    public static int getAnimationResource(int animationType) {
        return HandGesture.values()[animationType].getAnimationResource();
    }

    public static HandGesture getHandGesture(int animationType) {
        return HandGesture.values()[animationType];
    }
}
