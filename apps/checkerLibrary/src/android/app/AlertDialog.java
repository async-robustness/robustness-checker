/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Message;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;

public class AlertDialog extends Dialog implements DialogInterface {

    public static final int THEME_TRADITIONAL = 1;
    public static final int THEME_HOLO_DARK = 2;
    public static final int THEME_HOLO_LIGHT = 3;
    public static final int THEME_DEVICE_DEFAULT_DARK = 4;
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;

    private static OnClickListener mPositiveButtonListener;
    private static OnClickListener mNegativeButtonListener;
    private static OnClickListener mNeutralButtonListener;
    private static OnMultiChoiceClickListener mOnCheckboxClickListener;
    private static AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private static OnClickListener mOnClickListener;
    private static OnKeyListener mOnKeyListener;
    private static OnCancelListener mOnCancelListener;
    private static OnDismissListener mOnDismissListener;

    private static Context mContext;

    public AlertDialog() {
    }

    public void setTitle(CharSequence title) {
    }

    public void setCustomTitle(View customTitleView) {
    }

    public void setMessage(CharSequence message) {
    }

    public void setView(View view) {
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
                        int viewSpacingBottom) {
    }

    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
    }

    public void setButton(CharSequence text, Message msg) {
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
    }

    public LayoutInflater getLayoutInflater() {
        return mContext.getLayoutInflater();
    }

    public View findViewById(int resId, Class viewClass) {
        View v;
        try {
            v = (View) viewClass.newInstance();
            return v;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        v = new View();
        return v;
    }

    public Button getButton(int resId) {
        return new Button();
    }

    public static class Builder {

        public AlertDialog mAlertDialog;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder(Context context, int theme) {
            mContext = context;
        }

        /*public Context getContext() {
            return new Context();
        }*/

        public Builder setTitle(int titleId) {
            return this;
        }

        public Builder setTitle(CharSequence title) {
            return this;
        }

        public Builder setCustomTitle(View customTitleView) {
            return this;
        }

        public Builder setMessage(int messageId) {
            return this;
        }

        public Builder setMessage(Spanned s) {
            return this;
        }

        public Builder setMessage(CharSequence message) {
            return this;
        }

        public Builder setIcon(int iconId) {
            return this;
        }

        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            mAlertDialog.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            mAlertDialog.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            mAlertDialog.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            mAlertDialog.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, final OnClickListener listener) {
            mAlertDialog.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            mAlertDialog.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mAlertDialog.mOnCancelListener = onCancelListener;
            return this;
        }


        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mAlertDialog.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            mAlertDialog.mOnKeyListener = onKeyListener;
            return this;
        }


        public Builder setItems(int itemsId, final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(final Cursor cursor, final OnClickListener listener,
                                 String labelColumn) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            mAlertDialog.mOnCheckboxClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            mAlertDialog.mOnCheckboxClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn,
                                           final OnMultiChoiceClickListener listener) {
            mAlertDialog.mOnCheckboxClickListener = listener;
            return this;
        }

        public Builder setSingleChoiceItems(int itemsId, int checkedItem,
                                            final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }


        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn,
                                            final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, final OnClickListener listener) {
            mAlertDialog.mOnClickListener = listener;
            return this;
        }

        public Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            mAlertDialog.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(View view) {
            return this;
        }

        public AlertDialog create() {
            return mAlertDialog = new AlertDialog();
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            return dialog;
        }
    }

    public void callOnClickListener(DialogInterface dialog, int position) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(dialog, position);
        }
    }

}
