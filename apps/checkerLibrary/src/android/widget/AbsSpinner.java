package android.widget;

/**
 * Created by burcuozkan on 10/06/16.
 */
public class AbsSpinner extends AdapterView<Adapter> {

   Adapter mAdapter;

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public int getSelectedItemPosition() {
        return 0;
    }
}
