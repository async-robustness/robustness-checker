/*
 * Copyright (c) 2014 IRCCloud, Ltd.
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
import android.view.View;
import android.widget.ListView;

/**
 * Created by sam on 1/28/14.
 */
public class ListViewHax extends ListView {
    private int bottomPos = -1;
    private int bottomOffset = 0;

    public ListViewHax(Context context)
    {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View v = getChildAt(getLastVisiblePosition() - getFirstVisiblePosition());
        if(v != null) {
            bottomPos = getLastVisiblePosition();
            bottomOffset = getHeight() - v.getTop();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(oldw > 0 && oldh > 0) {
            if(bottomPos != -1 && bottomOffset > 0)
                setSelectionFromTop(bottomPos, h - bottomOffset);
            else
                setSelection(getCount() - 1);
        }
    }
}
