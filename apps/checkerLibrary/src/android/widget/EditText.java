/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.text.Editable;

public class EditText extends TextView {
    private boolean isEnabled = false;

    public EditText() {
    }

    public EditText(Context c) {
    }

    public EditText(int resId) {
    }

    public Editable getText() {
        return (Editable) super.getText();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

}
