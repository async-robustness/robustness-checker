/**
 * Stub/model code simplified/modified from the Android library
 */

package android.preference;

public class SwitchPreference extends Preference {
    private  boolean mChecked = false;

    public void setChecked(boolean b) {
        mChecked = b;
    }

    public boolean isChecked() {
        return mChecked;
    }
}
