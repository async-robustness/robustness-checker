/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.view.ViewGroup;

public class LinearLayout extends ViewGroup {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    // to support instantiation from Class
    public LinearLayout() {
    }

    public LinearLayout(Context context) {
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams() { // added for instantiation
        }

        public LayoutParams(Context c, Object attrs) {
        }

        public LayoutParams(int width, int height) {
        }

        public LayoutParams(int width, int height, float weight) {
        }

    }

    public void setOrientation(int orientation) {
    }

    public void setGravity(int gravity) {
    }
}
