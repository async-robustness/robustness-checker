/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

public class DragSortListView extends ListView {

    private DropListener mListener;

    public void setDropListener(DropListener l) {
        mListener = l;
    }

    public interface DropListener {
        void drop(int from, int to);
    }

}
