/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

public class CheckBox extends CompoundButton {

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public CheckBox() {
    }

    public CheckBox(int resId) {
    }

}
