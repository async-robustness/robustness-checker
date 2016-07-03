/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v7.app;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class ActionBar {

    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_TABS = 2;

    public static final int DISPLAY_SHOW_TITLE = 0x8;
    public static final int DISPLAY_SHOW_CUSTOM = 0x10;


    private View customView = new View();

    public ActionBar getSupportActionBar() {
        return this;
    }

    public boolean isShowing() {
        return false;
    }

    public void show() {
    }

    public void hide() {
    }

    public void setTitle(String s) {
    }

    public void setHomeButtonEnabled(boolean b) {
    }

    public void setDisplayShowHomeEnabled(boolean b) {
    }

    public void setDisplayShowCustomEnabled(boolean b) {
    }

    public void setDisplayShowTitleEnabled(boolean b) {
    }

    public void setDisplayHomeAsUpEnabled(boolean b) {
    }

    public Tab setIcon(int resId) {
        return new Tab();
    }

    public void setCustomView(View view) {
    }

    public void setCustomView(int resId) {
    }

    public void setLogo(int resId) {
    }

    public void setBackgroundDrawable(Object drawable) {
    }

    public void setDisplayOptions(int options) {
    }

    public void setNavigationMode(int mode) {
    }

    public View getCustomView() {
        return customView;
    }

    public void addTab(Tab b) {
    }

    public Tab newTab() {
        return new Tab();
    }

    public interface TabListener {
        public void onTabSelected(Tab tab, FragmentTransaction ft);
        public void onTabUnselected(Tab tab, FragmentTransaction ft);
        public void onTabReselected(Tab tab, FragmentTransaction ft);
    }

    public static class Tab {
        private TabListener mTabListener;

        public Tab setIcon(int resId) {
            return new Tab();
        }

        public int getPosition() {
            return 0;
        }

        public Tab setTabListener(TabListener listener) {
            mTabListener = listener;
            return this;
        }
    }
}
