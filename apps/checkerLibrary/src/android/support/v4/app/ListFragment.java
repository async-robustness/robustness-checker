/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import static android.widget.AdapterView.*;

public class ListFragment extends Fragment {

    final private OnItemClickListener mOnClickListener
            = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            //onListItemClick((ListView) parent, v, position, id);
        }
    };

    ListAdapter mAdapter;

    ListView mList = new ListView();

    public ListView getListView() {
        return mList;
    }

    public void setListAdapter(ListAdapter la) {
        mAdapter = la;
        mList.setAdapter(la);
    }

    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate view
        return new View();
    }

}