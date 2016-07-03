/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class FragmentManager {

    public Map fragmentsById = new HashMap<Integer, android.support.v4.app.Fragment>();

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
                f.onActivityCreated(b);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public Fragment findFragmentById(int resId) {
        Fragment f = (Fragment) fragmentsById.get(resId);
        Bundle b = new Bundle();
        if (f == null) {
            f = new Fragment();
            fragmentsById.put(resId, f);
            f.onCreate(b);
            f.onCreateView(new LayoutInflater(), new ViewGroup(), b);
            f.onViewCreated(new View(), b);
            f.onActivityCreated(b);
        }
        return f;
    }

    public FragmentTransaction beginTransaction() {
        return new FragmentTransaction();
    }

    public static int fragmentId = -1;  // not to overwrite existing Fragments in the Map

    public Fragment findFragmentByTag(String tag) {
        for (Object f : fragmentsById.values()) {
            if (((Fragment) f).getTag().equalsIgnoreCase(tag)) {
                return (Fragment) f;
            }
        }

        Fragment f = new Fragment();
        f.setTag(tag);
        fragmentsById.put(fragmentId--, f);
        Bundle b = new Bundle();
        f.onCreate(b);
        f.onCreateView(new LayoutInflater(), new ViewGroup(), b);
        f.onViewCreated(new View(), b);
        f.onActivityCreated(b);
        return f;
    }

    public Fragment findFragmentByTag(String tag, Class fragmentClass) {
        for (Object f : fragmentsById.values()) {
            if (((Fragment) f).getTag().equalsIgnoreCase(tag)) {
                return (Fragment) f;
            }
        }

        Fragment f;
        Bundle b = new Bundle();

        try {
            f = (Fragment) fragmentClass.newInstance();
            f.setTag(tag);
            fragmentsById.put(fragmentId--, f);
            f.onCreate(b);
            f.onCreateView(new LayoutInflater(), new ViewGroup(), b);
            f.onViewCreated(new View(), b);
            f.onActivityCreated(b);
            return f;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        f = new Fragment();
        fragmentsById.put(fragmentId--, f);
        f.setTag(tag);
        f.onCreate(b);
        f.onCreateView(new LayoutInflater(), new ViewGroup(), b);
        f.onViewCreated(new View(), b);
        f.onActivityCreated(b);
        return f;
    }
}
