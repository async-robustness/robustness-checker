/*
 * Copyright (c) 2013 IRCCloud, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.irccloud.android;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageView;

// An EditText that lets you use actions ("Done", "Go", etc.) on multi-line edits.
// From: http://stackoverflow.com/a/12570003/1406639
public class ActionEditText extends EditText
{
	private DrawerLayout mDrawerLayout = null;
	private ImageView upView = null;

    public ActionEditText()
    {

    }

    public ActionEditText(Context context)
    {
        super(context);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        return new InputConnection();
    }
    
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(mDrawerLayout != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
        	mDrawerLayout.closeDrawers();
        	upView.setVisibility(View.VISIBLE);
        }
        return super.onKeyPreIme(keyCode, event);
    }
    
    public void setDrawerLayout(DrawerLayout view, ImageView upView) {
    	mDrawerLayout = view;
    	this.upView = upView;
    }
}