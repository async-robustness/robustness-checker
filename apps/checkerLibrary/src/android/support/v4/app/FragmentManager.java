/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.util.HashMap;
import java.util.Map;

public class FragmentManager {

    public Map fragmentsById = new HashMap<Integer, Fragment>();

    public Fragment findFragmentById(int resId, Class fragmentClass) {
        Fragment f = (Fragment) fragmentsById.get(resId);
        Bundle b = new Bundle();
        if (f == null) {
            try {
                f = (Fragment) fragmentClass.newInstance();
                fragmentsById.put(resId, f);
                f.onCreate(b);
                //Fragment can be added inside layout file and inflated
                f.onCreateView(new LayoutInflater(), new ViewGroup(), b);
                f.onViewCreated(new View(), b);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return f;
    }

    public FragmentTransaction beginTransaction() {
        return new FragmentTransaction();
    }

    public Fragment findFragmentByTag(String tag) {
        return null;
    }
}
