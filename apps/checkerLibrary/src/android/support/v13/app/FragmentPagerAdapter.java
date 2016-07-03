/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v13.app;

import android.app.Fragment;
import android.app.FragmentManager;

public abstract class FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;

    public FragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    public abstract Fragment getItem(int position);

    public abstract int getCount();

    public CharSequence getPageTitle(int position) {
        return null;
    }

}
