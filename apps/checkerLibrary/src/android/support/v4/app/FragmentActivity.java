/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.app;

import android.app.Activity;

public class FragmentActivity extends Activity {

    private FragmentManager mFragmentManager = new FragmentManager();

    public FragmentManager getSupportFragmentManager() {
        return mFragmentManager;
    }

    public void supportInvalidateOptionsMenu() {
    }

}
