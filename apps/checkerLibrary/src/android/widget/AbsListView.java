package android.widget;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class AbsListView extends AdapterView<ListAdapter> {

    protected ListAdapter mAdapter = null;

    // ViewGroup and the Views of the ListView:
    protected ViewGroup mViewGroup = new ViewGroup();
    protected List<View> mViews = new ArrayList<View>();

    // getView() is called for each item in the list you pass to the adapter
    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;

        if(mAdapter != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View v = new View();
                mViews.add(v);
                mAdapter.getView(i, v, mViewGroup);
            }
        }
    }

    public View getChildAt(int pos) {
        if(mViews.size() <= pos) {
            return null;
        }
        return mViews.get(pos);
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    private OnScrollListener mOnScrollListener;
    public interface OnScrollListener {

        public static int SCROLL_STATE_IDLE = 0;
        public static int SCROLL_STATE_TOUCH_SCROLL = 1;
        public static int SCROLL_STATE_FLING = 2;
        public void onScrollStateChanged(AbsListView view, int scrollState);
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount);
    }

    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    public boolean callOnScrollListener(int first, int count, int totalCount) {
        if(mOnScrollListener == null)
            return false;

        mOnScrollListener.onScroll(new ListView(), first, count, totalCount);
        return true;
    }

    public int getFirstVisiblePosition() {
        return 0;
    }

    public int getLastVisiblePosition() {
        return (mAdapter == null || mAdapter.isEmpty()) ? 0 : mAdapter.getCount() >= 5 ? 4 : mAdapter.getCount();
    }

    public void requestFocus() {

    }

    public void setCacheColorHint(int color) {
    }

    public void smoothScrollToPosition(int position) {
    }
}
