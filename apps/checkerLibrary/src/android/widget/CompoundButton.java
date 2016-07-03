/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.view.View;

public class CompoundButton extends View {

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public boolean callOnCheckedChangeListener(boolean isChecked) {
        if (mOnCheckedChangeListener == null)
            return false;

        mOnCheckedChangeListener.onCheckedChanged(new CompoundButton(), isChecked);
        return true;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

}
