/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class Toast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    public Toast() {
    }

    public Toast(Context context) {
    }

    public void show() {
    }

    public void cancel() {
    }

    public void setView(View view) {
    }

    public View getView() {
        return new View();
    }

    public void setDuration(int duration) {
    }

    public int getDuration() {
        return 0;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
    }

    public float getHorizontalMargin() {
        return 0;
    }

    public float getVerticalMargin() {
        return 0;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
    }

    public int getGravity() {
        return 0;
    }

    public int getXOffset() {
        return 0;
    }

    public int getYOffset() {
        return 0;
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        return new Toast();
    }

    public static Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return new Toast();
    }

    public void setText(int resId) {
    }

    public void setText(CharSequence s) {
    }
}
