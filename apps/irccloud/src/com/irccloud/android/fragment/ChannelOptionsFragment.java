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

package com.irccloud.android.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.irccloud.android.NetworkConnection;
import com.irccloud.android.R;

public class ChannelOptionsFragment extends DialogFragment {
	CheckBox members;
	CheckBox unread;
	CheckBox joinpart;
    CheckBox collapse;
	int cid;
	int bid;
	
	public ChannelOptionsFragment() {
		cid = -1;
		bid = -1;
	}
	
	public ChannelOptionsFragment(int cid, int bid) {
		this.cid = cid;
		this.bid = bid;
	}
	
	public Object updatePref(Object prefs, CheckBox control, String key) {
		return prefs;
	}
	
    class SaveClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// removed json code
			dismiss();
		}
    }

    @Override
    public void onResume() {
    	super.onResume();
    	//try {
	    	if(NetworkConnection.getInstance().getUserInfo() != null) {
		    	/*JSONObject prefs = NetworkConnection.getInstance().getUserInfo().prefs;
		    	if(prefs != null) {
		    		if(prefs.has("channel-hideJoinPart")) {
				    	JSONObject hiddenMap = prefs.getJSONObject("channel-hideJoinPart");
						if(hiddenMap.has(String.valueOf(bid)) && hiddenMap.getBoolean(String.valueOf(bid)))
							joinpart.setChecked(false);
						else
							joinpart.setChecked(true);
		    		} else {
						joinpart.setChecked(true);
		    		}
		    		if(prefs.has("channel-disableTrackUnread")) {
				    	JSONObject unreadMap = prefs.getJSONObject("channel-disableTrackUnread");
						if(unreadMap.has(String.valueOf(bid)) && unreadMap.getBoolean(String.valueOf(bid)))
							unread.setChecked(false);
						else
							unread.setChecked(true);
		    		} else {
		    			unread.setChecked(true);
		    		}
		    		if(prefs.has("channel-hiddenMembers")) {
				    	JSONObject membersMap = prefs.getJSONObject("channel-hiddenMembers");
						if(membersMap.has(String.valueOf(bid)) && membersMap.getBoolean(String.valueOf(bid)))
							members.setChecked(false);
						else
							members.setChecked(true);
		    		} else {
		    			members.setChecked(true);
		    		}
                    if(prefs.has("channel-expandJoinPart")) {
                        JSONObject expandMap = prefs.getJSONObject("channel-expandJoinPart");
                        if(expandMap.has(String.valueOf(bid)) && expandMap.getBoolean(String.valueOf(bid)))
                            collapse.setChecked(false);
                        else
                            collapse.setChecked(true);
                    } else {
                        collapse.setChecked(true);
                    }
		    	} else {
					joinpart.setChecked(true);
	    			unread.setChecked(true);
	    			members.setChecked(true);
                    collapse.setChecked(true);
		    	}*/
	    	} else {
				joinpart.setChecked(true);
    			unread.setChecked(true);
    			members.setChecked(true);
                collapse.setChecked(true);
	    	}
		//} catch (JSONException e) {
		//	e.printStackTrace();
		//}
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	if(savedInstanceState != null && savedInstanceState.containsKey("cid") && cid == -1) {
    		cid = savedInstanceState.getInt("cid");
    		bid = savedInstanceState.getInt("bid");
    	}
		Context ctx = getActivity();
		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View v = inflater.inflate(R.layout.dialog_channel_options,null);
    	members = (CheckBox)v.findViewById(R.id.members, CheckBox.class);
    	unread = (CheckBox)v.findViewById(R.id.unread, CheckBox.class);
    	joinpart = (CheckBox)v.findViewById(R.id.joinpart, CheckBox.class);
        collapse = (CheckBox)v.findViewById(R.id.collapse, CheckBox.class);
    	
    	return new AlertDialog.Builder(ctx)
                .setTitle("Display Options")
                .setView(v)
                .setPositiveButton("Save", new SaveClickListener())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
                })
                .create();
    }
    
    @Override
    public void onSaveInstanceState(Bundle state) {
    	super.onSaveInstanceState(state);
    	state.putInt("cid", cid);
    	state.putInt("bid", bid);
    }
}
