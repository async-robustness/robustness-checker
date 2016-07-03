/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

public class KeyEvent extends InputEvent {
    public static final int KEYCODE_UNKNOWN = 0;
    public static final int KEYCODE_SOFT_LEFT = 1;
    public static final int KEYCODE_SOFT_RIGHT = 2;
    public static final int KEYCODE_HOME = 3;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_CALL = 5;
    public static final int KEYCODE_ENDCALL = 6;
    public static final int KEYCODE_TAB = 61;
    public static final int KEYCODE_ENTER = 66;

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;

    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    public static final int KEYCODE_SEARCH = 84;

    private int mAction;
    private int mKeyCode;

    public KeyEvent(int action) {
        mAction = action;
    }

    public final int getRepeatCount() {
        return 0;
    }

    public final int getAction() {
        return mAction;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

}
