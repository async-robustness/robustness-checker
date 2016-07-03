/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.view.View;

public class ListView extends AbsListView {

    private int selection = 0;

    public ListView() {
    }

    public ListView(int resId) {
    }

    public ListView(Context c) {
    }

    public void setSelection(int position) {
        selection = position;
    }

    public void setTextFilterEnabled(boolean b) {
    }

    public void setSelectionFromTop(int position, int y) {
    }

    public int getCount() {
        return 0;
    }

    public void setEmptyView(View v) {
    }

    public void invalidateViews() {
    }

    public int getHeaderViewsCount() {
        return 0;
    }

    public int getFooterViewsCount() {
        return 0;
    }

    public void setFooterDividersEnabled(boolean b) {
    }

    public void addHeaderView(View v) {
    }

    public void addFooterView(View v) {
    }

    public void removeFooterView(View v) {
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    }
}
