/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

import android.graphics.drawable.Drawable;

public class Window {

    public static final int FEATURE_OPTIONS_PANEL = 0;
    public static final int FEATURE_NO_TITLE = 1;
    public static final int FEATURE_PROGRESS = 2;
    public static final int FEATURE_LEFT_ICON = 3;
    public static final int FEATURE_RIGHT_ICON = 4;
    public static final int FEATURE_INDETERMINATE_PROGRESS = 5;
    public static final int FEATURE_CONTEXT_MENU = 6;
    public static final int FEATURE_CUSTOM_TITLE = 7;
    public static final int FEATURE_ACTION_BAR = 8;

    public void setSoftInputMode(int mode) {
    }

    public void clearFlags(int flags) {
    }

    public void addFlags(int flags) {
    }

    public View getDecorView() {
        return new View();
    }

    public void setBackgroundDrawable(Drawable d) {
    }

    public void setFeatureDrawableResource(int featureId, int resId) {
    }

    public boolean requestFeature(int featureId) {
        return false;
    }
}
