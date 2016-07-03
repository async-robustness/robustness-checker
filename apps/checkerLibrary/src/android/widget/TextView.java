/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

public class TextView extends View {

    private boolean mEnabled;
    //keep in Editable format
    //TODO support more formats
    private CharSequence mText = new SpannableStringBuilder("");

    // the String text is kept only for display purposes
    public enum BufferType {
        NORMAL, SPANNABLE, EDITABLE,
    }

    public TextView() {
    }

    public TextView(Context c) {
    }

    public TextView(int textId) {
    }

    public TextView(String text) {
        //mText = text;
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(text);
        mText = t;
    }

    public CharSequence getText() {
        return mText;
    }

    public final void setText(CharSequence text) {
        // mText = text;
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(text);
        mText = t;
    }

    public final void setTextKeepState(CharSequence text) {
        //mText = text;
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(text);
        mText = t;
    }

    public void setText(CharSequence text, BufferType type) {
        //mText = text;
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(text);
        mText = t;
    }

    private void setText(CharSequence text, BufferType type,
                         boolean notifyBefore, int oldlen) {
        //mText = text;
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(text);
        mText = t;
    }

    public final void setText(char[] text, int start, int len) {
        // mText = new String(text);
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(new String(text));
        mText = t;
    }

    public final void setText(int resid) {
        //mText = Integer.toString(resid);
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(new String(Integer.toString(resid)));
        mText = t;
    }

    public final void setHint(CharSequence hint) {
    }

    public final void setText(int resid, BufferType type) {
        // mText = Integer.toString(resid);
        // keep in Editable form
        //TODO support more formats
        Editable t = new SpannableStringBuilder(new String(Integer.toString(resid)));
        mText = t;
    }

    public void setMinWidth(int minpixels) {
    }

    public void setTypeface(Object tf) {
    }

    public void setPaintFlags(int flags) {
    }

    public int getPaintFlags() {
        return 0;
    }

    public void setTextColor(int color) {
    }

    public void setSelection(int i) {
    }

    public void setSelected(boolean b) {
    }

    public void setGravity(int gravity) {
    }

    public void setTextSize(int size) {
    }

    public void setSingleLine() {
    }

    public void setHorizontallyScrolling(boolean b) {
    }

    public void setLineSpacing(float add, float mult) {
    }

    public void setInputType(int type) {
    }

    public int getSelectionStart() {
        return 0;
    }

    public final void append(CharSequence text) {
        setText("appended");
        //append(text, 0, text.length());
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    private ArrayList<TextWatcher> mListeners;

    public void addTextChangedListener(TextWatcher watcher) {
        if (mListeners == null) {
            mListeners = new ArrayList<TextWatcher>();
        }

        mListeners.add(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        if (mListeners != null) {
            int i = mListeners.indexOf(watcher);

            if (i >= 0) {
                mListeners.remove(i);
            }
        }
    }

    public void callOnTextChangedListener(int listenerIndex, CharSequence s, int start, int before, int count) {
        if (mListeners.get(listenerIndex) == null)
            return;

        mListeners.get(listenerIndex).onTextChanged(s, start, before, count);
    }

    OnEditorActionListener onEditorActionListener;

    public void setOnEditorActionListener(OnEditorActionListener l) {
        onEditorActionListener = l;
    }

    public interface OnEditorActionListener {
        boolean onEditorAction(TextView v, int actionId, KeyEvent event);
    }

    public void callOnEditorActionListener(TextView v, int actionId, KeyEvent event) {
        if (onEditorActionListener == null)
            return;

        onEditorActionListener.onEditorAction(v, actionId, event);
    }

}

