/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

public class WindowManager {

    private static Display display = new Display();

    public Display getDefaultDisplay() {
        return display;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public static final int SOFT_INPUT_MASK_STATE = 0x0f;
        public static final int SOFT_INPUT_STATE_UNSPECIFIED = 0;
        public static final int SOFT_INPUT_STATE_UNCHANGED = 1;
        public static final int SOFT_INPUT_STATE_HIDDEN = 2;
        public static final int SOFT_INPUT_STATE_ALWAYS_HIDDEN = 3;
        public static final int SOFT_INPUT_STATE_VISIBLE = 4;
        public static final int SOFT_INPUT_STATE_ALWAYS_VISIBLE = 5;
        public static final int SOFT_INPUT_ADJUST_RESIZE = 0x10;

        public static final int FLAG_KEEP_SCREEN_ON = 0x00000080;
    }

    public static class BadTokenException extends RuntimeException {
        public BadTokenException() {
        }

        public BadTokenException(String name) {
            super(name);
        }
    }

}
