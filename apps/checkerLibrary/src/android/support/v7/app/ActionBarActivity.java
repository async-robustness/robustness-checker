/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v7.app;

import android.support.v4.app.FragmentActivity;

public class ActionBarActivity extends FragmentActivity {

    private ActionBar mActionBar = new ActionBar();

    public ActionBar getSupportActionBar() {
        return mActionBar;
    }
}
