/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PopupMenu {
    private Context mContext;
    private Menu mMenu;

    public PopupMenu(Context c, View v) {
        mContext = c;
        mMenu = new Menu();
    }

    public Menu getMenu() {
        return mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(mContext);
    }

    private OnMenuItemClickListener mMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuItemClickListener = listener;
    }

    public void callOnMenuItemClickListener(MenuItem item) {
        mMenuItemClickListener.onMenuItemClick(item);
    }

    public interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem item);
    }

    public void show() {
    }
}
