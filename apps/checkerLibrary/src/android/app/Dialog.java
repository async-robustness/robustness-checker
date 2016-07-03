/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Dialog implements DialogInterface {
    private static final String TAG = "Dialog";
    private Activity mOwnerActivity;

    private OnKeyListener mOnKeyListener;

    public Dialog() {

    }

    public Dialog(Context context) {

    }

    public Dialog(Context context, int theme) {

    }

    public boolean isShowing() {
        return false;
    }


    public void show() {
    }

    public void hide() {

    }

    public void cancel() {

    }

    public void dismiss() {
    }

    void dismissDialog() {
    }

    public Window getWindow() {
        return new Window();
    }

    public final void setOwnerActivity(Activity activity) {
    }

    public void setTitle(CharSequence title) {
    }

    public void setTitle(int titleId) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {

        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }
    

    public void onBackPressed() {

    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }


    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }


    public void setOnCancelListener(final OnCancelListener listener) {
    }

    public void setOnDismissListener(final OnDismissListener listener) {
    }

    public void setOnShowListener(OnShowListener listener) {
    }

    public void setOnKeyListener(final OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    public void setCancelable(boolean flag) {
    }

    public void setIndeterminate(boolean b) {

    }

}
