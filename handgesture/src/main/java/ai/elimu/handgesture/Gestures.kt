package ai.elimu.handgesture

/**
 */
object Gestures {
    var MOVE_UP: HandGesture = HandGesture.MOVE_UP
    var MOVE_DOWN: HandGesture = HandGesture.MOVE_DOWN
    var MOVE_LEFT: HandGesture = HandGesture.MOVE_LEFT
    var MOVE_RIGHT: HandGesture = HandGesture.MOVE_RIGHT
    @JvmField
    var SINGLE_TAP: HandGesture = HandGesture.SINGLE_TAP
    @JvmField
    var DOUBLE_TAP: HandGesture = HandGesture.DOUBLE_TAP
    @JvmField
    var PRESS_AND_HOLD: HandGesture = HandGesture.PRESS_AND_HOLD
    @JvmField
    var TRANSLATION: HandGesture = HandGesture.TRANSLATION

    @JvmStatic
    fun getAnimationResource(animationType: Int): Int {
        return HandGesture.entries[animationType].animationResource
    }

    @JvmStatic
    fun getHandGesture(animationType: Int): HandGesture? {
        return HandGesture.entries[animationType]
    }

    enum class HandGesture {
        MOVE_UP(R.anim.slide_up),
        MOVE_DOWN(R.anim.slide_down),
        MOVE_LEFT(R.anim.slide_left),
        MOVE_RIGHT(R.anim.slide_right),
        SINGLE_TAP(0),
        DOUBLE_TAP(0),
        PRESS_AND_HOLD(0),
        TRANSLATION(true);

        var animationResource: Int = 0
            private set
        var isCustomTranslation: Boolean = false
            private set

        constructor(idAnim: Int) {
            this.animationResource = idAnim
        }

        constructor(translation: Boolean) {
            this.isCustomTranslation = translation
        }

        val isTranslation: Boolean
            get() = (this.animationResource > 0) || this.isCustomTranslation
    }
}
