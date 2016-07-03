/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment {

    private View mView = new View();

    public Fragment() {
    }

    // to be used for findFragmentByTag
    private String mTag = "";
    private int mPosition = -1;

    public Fragment(String tag) {
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    /*
    // to be used for findFragmentByPosition
    public Fragment(int pos) {

    }

    public void setPosition(int pos) {
        mPosition = pos;
    }

    public int getPosition() {
        return mPosition;
    }*/

    private Activity mActivity = new Activity();

    public Activity getActivity() {
        return mActivity;
    }

    // To enable Fragments sharing a same Activity
    public void setActivity(Activity a) {
        mActivity = a;
    }

    public void onAttach(Activity activity) {
    }

    public void onDetach() {
    }

    public LayoutInflater getLayoutInflater(Object o) {
        return mActivity.getLayoutInflater(o);
    }

    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    public Resources getResources() {
        return mActivity.getResources();
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    public View onCreateView(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public View getView() {
        return mView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    public void onResume() {
    }


    public void onSaveInstanceState(Bundle outState) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    public void onDestroyView() {
    }

    public void onDestroy() {
    }

    public void show(FragmentManager fm, String name) {
    }

    public void setHasOptionsMenu(boolean hasMenu) {
    }

    final public Bundle getArguments() {
        return new Bundle();
    }

    public Fragment instantiate(Activity activity, Fragment parent) {
        return new Fragment();
    }

    public static Fragment instantiate(Context context, String fname) {
        return new Fragment();
    }

    public void setArguments(Bundle args) {
    }
}
