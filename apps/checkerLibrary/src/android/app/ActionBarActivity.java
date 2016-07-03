/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.support.v4.app.FragmentActivity;

public class ActionBarActivity extends FragmentActivity {

    private static final ActionBar mActionBar = new ActionBar();

    public ActionBar getSupportActionBar() {
        return mActionBar;
    }
}
