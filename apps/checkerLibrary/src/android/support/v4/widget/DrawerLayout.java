/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.widget;

import android.view.View;
import android.view.ViewGroup;

public class DrawerLayout extends ViewGroup {

    public static final int LOCK_MODE_UNLOCKED = 0;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;

    private DrawerListener mListener;

    public interface DrawerListener {

        public void onDrawerSlide(View drawerView, float slideOffset);

        public void onDrawerOpened(View drawerView);

        public void onDrawerClosed(View drawerView);

        public void onDrawerStateChanged(int newState);
    }

    public void openDrawer(View drawerView) {
    }

    public void openDrawer(int gravity) {
    }

    public void closeDrawers() {
    }

    public void closeDrawer(View drawerView) {
    }

    public void closeDrawer(int gravity) {
    }

    public void setDrawerListener(DrawerListener listener) {
        mListener = listener;
    }

    public DrawerListener getDrawerListener() {
        return mListener;
    }

    public boolean isDrawerOpen(View drawer) {
        return false;
    }

    public boolean isDrawerOpen(int drawerGravity) {
        return false;
    }

    public void setDrawerLockMode(int lockMode) {
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
    }

    public void setDrawerLockMode(int lockMode, View drawerView) {
    }
}