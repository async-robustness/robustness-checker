/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

import android.content.Context;


public class ViewGroup extends View {

    public ViewGroup() {
    }

    public ViewGroup(Context context) {
    }

    public static class LayoutParams {
        public static final int FILL_PARENT = -1;
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;

        public LayoutParams() {
        }

        public LayoutParams(int width, int height) {
        }
    }

    public static class MarginLayoutParams extends ViewGroup.LayoutParams {
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;

        public MarginLayoutParams() {
        }

        public MarginLayoutParams(int width, int height) {
        }

        public void setMargins(int left, int top, int right, int bottom) {

        }
    }

    public void removeView(View view) {
    }

    public void addView(View child) {
    }

    public void addView(View child, int index) {
    }

    public void addView(View child, LayoutParams params) {
    }

    public void addView(View child, int index, LayoutParams params) {
    }
}
