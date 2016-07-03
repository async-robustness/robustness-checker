/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Intent;
import android.view.View;
import android.graphics.drawable.Drawable;

public class TabHost extends FrameLayout {

    private int currentTab = 0;
    private OnTabChangeListener mOnTabChangeListener;

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int tab) {
        currentTab = tab;
    }

    public void setOnTabChangedListener(OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    public interface OnTabChangeListener {
        void onTabChanged(String tabId);
    }

    public void addTab(TabSpec tabSpec) {
    }

    public void setup() {
    }

    public TabSpec newTabSpec(String tag) {
        return new TabSpec(tag);
    }

    public class TabSpec {

        private String mTag;

        private TabSpec(String tag) {
            mTag = tag;
        }

        public TabSpec setIndicator(CharSequence label) {
            return this;
        }

        public TabSpec setIndicator(CharSequence label, Drawable icon) {
            return this;
        }

        public TabSpec setContent(int viewId) {
            return this;
        }

        public TabSpec setContent(TabContentFactory contentFactory) {
            return this;
        }

        public TabSpec setContent(Intent intent) {
            return this;
        }
    }

    public interface TabContentFactory {
        View createTabContent(String tag);
    }
}
