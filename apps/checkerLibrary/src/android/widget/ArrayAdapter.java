/**
 * Stub/model code simplified/modified from the Android library
 * and JPA's model
 */

package android.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ArrayAdapter<T> implements ListAdapter, SpinnerAdapter {
    private List<T> mObjects;

    private boolean mNotifyOnChange = true;
    private Context mContext;

    private ArrayAdapter() {
    }

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<T> mOriginalValues;
    //private ArrayFilter mFilter;

    public ArrayAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<T>());
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<T>());
    }

    public ArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    public ArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        init(context, resource, textViewResourceId, objects);
    }

    public static ArrayAdapter<CharSequence> createFromResource(Context context, int textArrayResId, int textViewResId) {
        CharSequence cs[] = new CharSequence[1000];
        return new ArrayAdapter<CharSequence>(context, textViewResId, cs);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return new View();
    }

    public void setDropDownViewResource(int resource) {
    }

    public boolean isEmpty() {
        if (mObjects == null || mObjects.isEmpty())
            return true;
        return false;
    }

    public void add(T object) {
        if (mOriginalValues != null) {
            mOriginalValues.add(object);
        } else {
            mObjects.add(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        if (mOriginalValues != null) {
            mOriginalValues.addAll(collection);
        } else {
            mObjects.addAll(collection);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(T... items) {
        if (mOriginalValues != null) {
            Collections.addAll(mOriginalValues, items);
        } else {
            Collections.addAll(mObjects, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        if (mOriginalValues != null) {
            mOriginalValues.add(index, object);
        } else {
            mObjects.add(index, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(T object) {
        if (mOriginalValues != null) {
            mOriginalValues.remove(object);
        } else {
            mObjects.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        if (mOriginalValues != null) {
            mOriginalValues.clear();
        } else {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        if (mOriginalValues != null) {
            Collections.sort(mOriginalValues, comparator);
        } else {
            Collections.sort(mObjects, comparator);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, int resource, int textViewResourceId, List<T> objects) {
        mContext = context;
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public int getCount() {
        return mObjects.size();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return new View();
    }

    /*
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<T>(mObjects);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                list = new ArrayList<T>(mOriginalValues);
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values;
                values = new ArrayList<T>(mOriginalValues);

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
            //    notifyDataSetInvalidated();
            }
        }
    }
    */
}
