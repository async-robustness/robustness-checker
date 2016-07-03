/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.view.KeyEvent;

public interface DialogInterface {

    public static final int BUTTON_POSITIVE = -1;
    public static final int BUTTON_NEGATIVE = -2;
    public static final int BUTTON_NEUTRAL = -3;

    public void cancel();

    public void dismiss();

    interface OnCancelListener {
        public void onCancel(DialogInterface dialog);
    }

    interface OnDismissListener {
        public void onDismiss(DialogInterface dialog);
    }

    interface OnShowListener {
        public void onShow(DialogInterface dialog);
    }

    interface OnClickListener {
        public void onClick(DialogInterface dialog, int which);
    }

    interface OnMultiChoiceClickListener {
        public void onClick(DialogInterface dialog, int which, boolean isChecked);
    }

    interface OnKeyListener {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);
    }
}
