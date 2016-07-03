/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

public class MotionEvent extends InputEvent {

    private int mAction;

    public MotionEvent(int action) {
        mAction = action;
    }

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_CANCEL = 3;
    public static final int ACTION_OUTSIDE = 4;

    public final int getAction() {
        return mAction;
    }
}
