/**
 * Stub/model code simplified/modified from the Android library
 */

package android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.HashMap;
import java.util.Map;

public class View {

    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SYSTEM_UI_FLAG_VISIBLE = 0;
    public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 0x00000001;

    private int mID;
    private Object mTag;
    private OnClickListener mOnClickListener;
    private OnKeyListener mOnKeyListener;
    private OnTouchListener mOnTouchListener;
    protected OnFocusChangeListener mOnFocusChangeListener;
    protected OnLongClickListener mOnLongClickListener;

    public View() {
    }

    public View(int resId) {
    }

    public View(Context context) {
    }

    public int getTop() {
        return 0;
    }

    public int getId() {
        return mID;
    }

    public void setId(int mID) {
        this.mID = mID;
    }

    public void setTag(final Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean b) {
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams();
    }

    public ViewGroup.LayoutParams getLayoutParams(Class clazz) {
        try {
            ViewGroup.LayoutParams p = (ViewGroup.LayoutParams) clazz.newInstance();
            return p;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new ViewGroup.LayoutParams();
    }

    public void setSystemUiVisibility(int visibility) {
    }

    public void setFocusable(boolean focusable) {
    }

    public void requestFocus() {
    }

    public void refreshDrawableState() {
    }

    public void setBackgroundColor(int color) {
    }

    public void setBackgroundResource(int resid) {
    }

    public void setBackgroundDrawable(Drawable drawable) {
    }

    public void setMinimumHeight(int minHeight) {
    }

    public void setMinimumWidth(int minWidth) {
    }

    public void setAlpha(float alpha) {
    }

    public int getPaddingLeft() {
        return 0;
    }

    public int getPaddingRight() {
        return 0;
    }

    public int getPaddingTop() {
        return 0;
    }

    public int getPaddingBottom() {
        return 0;
    }

    public void setClickable(boolean clickable) {
    }

    public void setPadding(int left, int top, int right, int bottom) {
    }

    public int getHeight() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public void invalidate() {
    }

    public void setLayoutParams(ViewGroup.LayoutParams params) {
    }

    public void setSelected(boolean selected) {
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public boolean callOnClick() {
        if (mOnClickListener == null) {
            return false;
        }

        mOnClickListener.onClick(new View());
        return true;
    }

    public interface OnClickListener {
        void onClick(View v);
    }

    public boolean callOnKey(int keyCode, KeyEvent event) {
        if (mOnKeyListener == null) {
            return false;
        }

        mOnKeyListener.onKey(new View(), keyCode, event);
        return true;
    }

    public void setOnKeyListener(OnKeyListener l) {
        mOnKeyListener = l;
    }

    public interface OnKeyListener {
        boolean onKey(View v, int keyCode, KeyEvent event);
    }

    public boolean callOnTouch(MotionEvent event) {
        if (mOnTouchListener == null) {
            return false;
        }

        mOnTouchListener.onTouch(new View(), event);
        return true;
    }

    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event);
    }

    public boolean callOnLongClick() {
        if (mOnLongClickListener == null) {
            return false;
        }

        mOnLongClickListener.onLongClick(new View());
        return true;
    }

    public interface OnLongClickListener {
        boolean onLongClick(View v);
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        mOnLongClickListener = l;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    public interface OnFocusChangeListener {
        void onFocusChange(View v, boolean hasFocus);
    }

    public CharSequence getContentDescription() {
        return "";
    }

    public View getChildAt(int i) {
        return new View(); //TODO take Class param and cast?
    }

    public static final int VISIBLE = 0x00000000;

    public static final int INVISIBLE = 0x00000004;

    public static final int GONE = 0x00000008;

    //private int visibility;

    public int getVisibility() {
        return 0; //visibility;
    }

    public void setVisibility(int visibility) {
        //this.visibility = visibility;
    }

    public View parent;

    //ViewParent is not maintained
    public View getParent() {
        if (parent == null) {
            parent = new View();
        }
        return parent;
    }

    public View getParent(Class parentClass) {
        if (parent == null) {
            try {
                parent = (View) parentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return parent;
    }


    private void setParent(View v) {
    }

    public Drawable getBackground() {
        return new Drawable();
    }

    public void setContentDescription(CharSequence contentDescription) {
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * NOTE:
     * Runnables are executed synchronously by default
     * To simulate their async execution, the programmer should surround the call in his app with proper try/catch blocks
     * (See AsyncTask doInBackground and onPostExecute to see how to simulate an async proc)
     */
    public void post(Runnable r) {
        r.run();
    }

    public Map viewsById = new HashMap<Integer, View>();

    public View findViewById(int resId, Class viewClass) {
        View v = (View) viewsById.get(resId);
        if (v == null) {
            try {
                v = (View) viewClass.newInstance();
                viewsById.put(resId, v);
                v.setParent(this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

  /*public View findViewById(int resId) {
    View v = (View) viewsById.get(resId);
    if(v == null) {
      v = new View();
      //v.setParent(rootView);
    }
    return v;
  }*/

}