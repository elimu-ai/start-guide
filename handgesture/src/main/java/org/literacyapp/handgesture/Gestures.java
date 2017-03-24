package org.literacyapp.handgesture;

/**
 */
public class Gestures {

    public static HandGesture MOVE_UP = HandGesture.MOVE_UP;
    public static HandGesture MOVE_DOWN = HandGesture.MOVE_DOWN;
    public static HandGesture MOVE_LEFT = HandGesture.MOVE_LEFT;
    public static HandGesture MOVE_RIGHT = HandGesture.MOVE_RIGHT;
    public static HandGesture ONE_TOUCH = HandGesture.ONE_TOUCH;

    public enum HandGesture {
        MOVE_UP(R.anim.slide_up),
        MOVE_DOWN(R.anim.slide_down),
        MOVE_LEFT(R.anim.slide_left),
        MOVE_RIGHT(R.anim.slide_right),
        ONE_TOUCH(0);

        private int idAnim;

        HandGesture(int idAnim) {
            this.idAnim = idAnim;
        }

        public int getAnimationResource() {
            return idAnim;
        }
    }

    public static int getAnimationResource(int animationType) {
        return HandGesture.values()[animationType].getAnimationResource();
    }

    public int getAnimationResource(HandGesture handGesture) {
        return handGesture.getAnimationResource();
    }
}
