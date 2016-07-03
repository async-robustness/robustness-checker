/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.Context;

public class ProgressDialog extends AlertDialog {
    public static final int STYLE_SPINNER = 0;
    public static final int STYLE_HORIZONTAL = 1;

    public ProgressDialog() {
    }

    public ProgressDialog(Context context) {
    }

    public ProgressDialog(Context context, int theme) {
    }


    public static ProgressDialog show(Context context, CharSequence title,
                                      CharSequence message) {
        return new ProgressDialog();
    }

    public static ProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate) {
        return new ProgressDialog();
    }

    public static ProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate, boolean cancelable) {
        return new ProgressDialog();
    }

    public static ProgressDialog show(Context context, CharSequence title,
                                      CharSequence message, boolean indeterminate,
                                      boolean cancelable, OnCancelListener cancelListener) {
        return new ProgressDialog();
    }

    public void setProgressStyle(int style) {
    }

    public void setProgress(int value) {

    }

    public void incrementProgressBy(int diff) {
    }

    public void setMax(int max) {
    }
}
