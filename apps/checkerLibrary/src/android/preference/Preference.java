/**
 * Stub/model code simplified/modified from the Android library
 */

package android.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Preference {

    private OnPreferenceChangeListener mOnChangeListener;
    private OnPreferenceClickListener mOnClickListener;

    public Preference() {
    }

    public Preference(Context c) {
    }

    public Preference(Context c, Object o) {
    }

    public void setIcon(Drawable icon) {
    }

    public interface OnPreferenceChangeListener {
        boolean onPreferenceChange(Preference preference, Object newValue);
    }

    public interface OnPreferenceClickListener {
        boolean onPreferenceClick(Preference preference);
    }

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        mOnChangeListener = onPreferenceChangeListener;
    }

    public OnPreferenceChangeListener getOnPreferenceChangeListener() {
        return mOnChangeListener;
    }

    public void callOnPreferenceChangeListener(Preference p, Object o) {
        if (mOnChangeListener != null) {
            mOnChangeListener.onPreferenceChange(p, o);
        }
    }

    public void callOnPreferenceClickListener(Preference p) {
        if (mOnClickListener != null) {
            mOnClickListener.onPreferenceClick(p);
        }
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        mOnClickListener = onPreferenceClickListener;
    }

    public OnPreferenceClickListener getOnPreferenceClickListener() {
        return mOnClickListener;
    }

    public CharSequence getSummary() {
        return "";
    }

    public void setSummary(CharSequence summary) {
    }

    public void setSummary(int resId) {
    }

    public void setEnabled(boolean enabled) {
    }

    public String getKey() {
        return "";
    }

    protected void notifyChanged() {
    }
}
