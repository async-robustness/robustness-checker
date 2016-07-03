/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

import android.content.Context;

public class LayoutInflater {

    private Context mContext;

    public LayoutInflater() {
    }

    public LayoutInflater(Context c) {
        mContext = c;
    }

    public Context getContext() {
        return mContext;
    }

    public View inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    // added to support casting
    public View inflate(int resource, ViewGroup root, Class viewClass) {
        try {
            View v = (View) viewClass.newInstance();
            return v;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new View();
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return new View();
    }

    public static LayoutInflater from(Context c) {
        return new LayoutInflater(c);
    }
}

