/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListActivity extends Activity {
    protected ListAdapter mAdapter;
    protected ListView mList = new ListView();

    private boolean mFinishedStart = false;

    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setListAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mList.setAdapter(adapter);
    }

    public void setSelection(int position) {
        mList.setSelection(position);
    }

    public int getSelectedItemPosition() {
        //return mList.getSelectedItemPosition();
        return 0;
    }

    public long getSelectedItemId() {
        //return mList.getSelectedItemId();
        return 0;
    }

    public ListView getListView() {
        return mList;
    }

    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };
}
