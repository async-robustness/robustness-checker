/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

public class CursorAdapter extends BaseAdapter {

    private Cursor cursor;

    public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
    }

    public Cursor getCursor() {
        return cursor;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return null;
            }
        };
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new View();
    }

}
