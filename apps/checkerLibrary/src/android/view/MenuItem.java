/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

public class MenuItem {

    private int itemId;
    private AdapterContextMenuInfo info;

    public MenuItem() {
        itemId = -1;
    }

    public MenuItem(int id) {
        itemId = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int id) {
        itemId = id;
    }

    public MenuItem setTitle(int resId) {
        return this;
    }

    public MenuItem setIcon(int resId) {
        return this;
    }

    public void setVisible(boolean b) {
    }

    public void setEnabled(boolean b) {
    }


    public AdapterContextMenuInfo getMenuInfo() {
        return info;
    }

    public interface AdapterContextMenuInfo {

    }
}
