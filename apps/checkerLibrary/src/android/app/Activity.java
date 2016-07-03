/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.*;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;

public class Activity extends Context {

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    public static final int RESULT_FIRST_USER = 1;


    private Intent mIntent;

    protected void onCreate() {
    }

    protected void onCreate(Bundle bundle) {
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onResume(Bundle bundle) {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDestroy() {
    }

    public Activity() {
        mIntent = new Intent();
    }

    public Activity(Intent i) {
        mIntent = i;
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        return false;
    }

    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
    }

    public boolean onContextItemSelected(final MenuItem item) {
        return false;
    }

    public String getLocalClassName() {
        return "";
    }

    public WindowManager getWindowManager() {
        return super.getWindowManager();
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    }

    public void setTitle(int resId) {
    }

    public void setTitle(String text) {
    }

    public final void setResult(int resultCode) {
    }

    public final void setResult(int resultCode, Intent intent) {
    }

    public LayoutInflater getLayoutInflater(Object o) {
        return new LayoutInflater(this);
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(this);
    }

    public void startManagingCursor(Cursor c) {
    }

    public void stopManagingCursor(Cursor c) {
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent i) {
        mIntent = i;
    }


    protected void onNewIntent(Intent i) {
    }

    public void setTitle(CharSequence title) {
    }

    private ActionBar mActionBar;

    public ActionBar getActionBar() {
        if (mActionBar == null) {
            mActionBar = new ActionBar();
        }
        return mActionBar;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        return false;
    }

    public void openOptionsMenu() {
    }

    public final void setProgressBarIndeterminateVisibility(boolean visible) {
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    protected void onSaveInstanceState(Bundle saved) {
    }

    protected void onRestoreInstanceState(Bundle saved) {
    }

    public void setRequestedOrientation(int requestedOrientation) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public Object getLastNonConfigurationInstance() {
        return null;
    }


    public Object onRetainNonConfigurationInstance() {
        return null;
    }

    public void registerForContextMenu(View view) {
    }

    public void unRegisterForContextMenu(View view) {
    }

    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        return false;
    }

    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public final void setProgress(int progress) {
    }

    public final void setProgressBarVisibility(boolean visible) {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public SharedPreferences getPreferences(int mode) {
        return super.getSharedPreferences("", mode);
    }

    public void finish() {
    }

    public boolean onTouchEvent(final MotionEvent e) {
        return false;
    }

    private android.support.v4.app.FragmentManager mSupportFragments = new android.support.v4.app.FragmentManager();

    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return mSupportFragments;
    }

    private FragmentManager mFragments = new FragmentManager();

    public FragmentManager getFragmentManager() {
        return mFragments;
    }


    public void onBackPressed() {
        onPause();
        onStop();
    }

    public final void runOnUiThread(Runnable action) {
        /**
         * NOTE:
         * Runnables are executed synchronously by default
         * To simulate their async execution, the programmer should surround the call in his app with proper try/catch blocks
         * (See AsyncTask doInBackground and onPostExecute to see how to simulate an async proc)
         */
        action.run();
    }

    public void setContentView(int resId) {
    }

    public final boolean requestWindowFeature(int featureId) {
        return false;
    }

    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }

    // each activity has its own rootview
    protected View rootView = new View();

    public View getRootView() {
        return rootView;
    }

    public View findViewById(int resId, Class viewClass) {
        return getRootView().findViewById(resId, viewClass);
    }

    // removed to add the class parameter to the calls in the app
    /*public View findViewById(int resId) {
        return getRootView().findViewById(resId);
    }*/

    public Window getWindow() {
        return new Window();
    }

}