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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.irccloud.android.NetworkConnection;
import com.irccloud.android.R;
import com.irccloud.android.data.ServersDataSource;

public class NickservFragment extends DialogFragment {
    ServersDataSource.Server server;
    EditText pass;
    TextView nick;
	
    class SaveClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(pass.getText() != null && pass.getText().length() > 0) {
				NetworkConnection.getInstance().set_nspass(server.cid, pass.getText().toString());
				dismiss();
			}
		}
    }

    class InstructionsClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			NetworkConnection.getInstance().ns_help_register(server.cid);
			dismiss();
		}
    }

    public void setCid(int cid) {
    	server = ServersDataSource.getInstance().getServer(cid);
    	if(nick != null && server != null) {
	    	nick.setText("Password for " + server.nick);
	    	if(server.nickserv_pass != null)
	    		pass.setText(server.nickserv_pass);
    	}
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context ctx = getActivity();

		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	View v = inflater.inflate(R.layout.dialog_nickserv,null);
    	nick = (TextView)v.findViewById(R.id.nickname, TextView.class);
    	pass = (EditText)v.findViewById(R.id.password, EditText.class);
    	if(server != null) {
	    	nick.setText("Password for " + server.nick);
	    	if(server.nickserv_pass != null)
	    		pass.setText(server.nickserv_pass);
    	}
    	
    	String title = "Identify your nickname on ";
    	if(server.name != null && server.name.length() > 0)
    		title += server.name;
    	else
    		title += server.hostname;
    	
    	AlertDialog d = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setView(v)
                .setPositiveButton("Save", new SaveClickListener())
                .setNeutralButton("Instructions", new InstructionsClickListener())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
                })
                .create();
	    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	    return d;
    }
}
