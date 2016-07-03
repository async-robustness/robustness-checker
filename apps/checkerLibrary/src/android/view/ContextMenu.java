package android.view;

import android.graphics.drawable.Drawable;


public class ContextMenu extends Menu {

    public class ContextMenuInfo {

    }

    public ContextMenu setHeaderTitle(int titleRes) {
        return this;
    }

    public ContextMenu setHeaderTitle(CharSequence title) {
        return this;
    }
    
    public ContextMenu setHeaderIcon(int iconRes) {
        return this;
    }

    public ContextMenu setHeaderIcon(Drawable icon) {
        return this;
    }
    
    public ContextMenu setHeaderView(View view) {
        return this;
    }

}
