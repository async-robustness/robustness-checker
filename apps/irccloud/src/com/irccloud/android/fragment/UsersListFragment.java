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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.support.v4.app.ListFragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import checker.SkipException;
import com.irccloud.android.data.ChannelsDataSource;
import com.irccloud.android.NetworkConnection;
import com.irccloud.android.R;
import com.irccloud.android.data.ServersDataSource;
import com.irccloud.android.data.UsersDataSource;

public class UsersListFragment extends ListFragment {
	private static final int TYPE_HEADING = 0;
	private static final int TYPE_USER = 1;
	
	private NetworkConnection conn;
	private UserListAdapter adapter;
	private OnUserSelectedListener mListener;
	private int cid = -1;
	private int bid = -1;
	private String channel;
	private RefreshTask refreshTask = null;
	private Timer tapTimer = null;
    private Timer refreshTimer = null;
	
	private class UserListAdapter extends BaseAdapter {
		ArrayList<UserListEntry> data;
		private ListFragment ctx;
		
		private class ViewHolder {
			int type;
			TextView label;
			TextView count;
		}
	
		private class UserListEntry {
			int type;
			String text;
			String count;
			int color;
			int bg_color;
			int static_bg_color;
			boolean away;
			boolean last;
		}

		public UserListAdapter(ListFragment context) {
			ctx = context;
			data = new ArrayList<UserListEntry>();
		}
		
		public void setItems(ArrayList<UserListEntry> items) {
			data = items;
		}
		
		public UserListEntry buildItem(int type, String text, String count, int color, int bg_color, int static_bg_color, boolean away, boolean last) {
			UserListEntry e = new UserListEntry();
			e.type = type;
			e.text = text;
			e.count = count;
			e.color = color;
			e.bg_color = bg_color;
			e.static_bg_color = static_bg_color;
			e.away = away;
			e.last = last;
			return e;
		}
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			UserListEntry e = data.get(position);
			View row = convertView;
			ViewHolder holder;

			if (row == null) {
				LayoutInflater inflater = ctx.getLayoutInflater(null);
                row = inflater.inflate(R.layout.row_user, null);

				holder = new ViewHolder();
				holder.label = (TextView) row.findViewById(R.id.label, TextView.class);
				holder.count = (TextView) row.findViewById(R.id.count, TextView.class);
				holder.type = e.type;

				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			row.setOnLongClickListener(new OnItemLongClickListener(position));
			row.setOnClickListener(new OnItemClickListener(position));
			holder.label.setText(e.text);
			if(e.type == TYPE_USER && e.away) {
				holder.label.setTextColor(getResources().getColorStateList(R.color.row_user_away));
			} else {
				holder.label.setTextColor(getResources().getColorStateList(e.color));
			}

			if(e.last || e.type == TYPE_HEADING)
				row.setBackgroundResource(e.static_bg_color);
			else
				row.setBackgroundResource(e.bg_color);
			
			if(e.type == TYPE_HEADING) {
				if(e.count != null) {
					holder.count.setVisibility(View.VISIBLE);
					holder.count.setText(e.count);
					holder.count.setTextColor(getResources().getColorStateList(e.color));
				} else {
					holder.count.setVisibility(View.GONE);
					holder.count.setText("");
				}
				row.setFocusable(false);
				row.setEnabled(false);
                row.setMinimumHeight(0);
			} else {
                holder.count.setVisibility(View.GONE);
                holder.count.setText("");
				row.setFocusable(true);
				row.setEnabled(true);
                row.setMinimumHeight(0);
			}
			
			return row;
		}
	}

	private void addUsersFromList(ArrayList<UserListAdapter.UserListEntry> entries, ArrayList<UsersDataSource.User> users, String heading, String symbol, int heading_color, int bg_color, int heading_bg_color) {
		if(users.size() > 0) {
			entries.add(adapter.buildItem(TYPE_HEADING, heading, users.size() > 0?symbol + String.valueOf(users.size()):null, heading_color, heading_bg_color, heading_bg_color, false, false));
			for(int i = 0; i < users.size(); i++) {
				UsersDataSource.User user = users.get(i);
				entries.add(adapter.buildItem(TYPE_USER, user.nick, null, R.color.row_user, bg_color, heading_bg_color, user.away > 0, i == users.size() - 1));
			}
		}
	}
	
	private void refresh(ArrayList<UsersDataSource.User> users) {
		if(users == null) {
			if(adapter != null) {
				adapter.data.clear();
				adapter.notifyDataSetInvalidated();
			}
			return;
		}
		
		ArrayList<UserListAdapter.UserListEntry> entries = new ArrayList<UserListAdapter.UserListEntry>();
		ArrayList<UsersDataSource.User> owners = new ArrayList<UsersDataSource.User>();
		ArrayList<UsersDataSource.User> admins = new ArrayList<UsersDataSource.User>();
		ArrayList<UsersDataSource.User> ops = new ArrayList<UsersDataSource.User>();
		ArrayList<UsersDataSource.User> halfops = new ArrayList<UsersDataSource.User>();
		ArrayList<UsersDataSource.User> voiced = new ArrayList<UsersDataSource.User>();
		ArrayList<UsersDataSource.User> members = new ArrayList<UsersDataSource.User>();
		boolean showSymbol = false;
		//try {
			if(conn.getUserInfo() != null && conn.getUserInfo().prefs != null)
			showSymbol = false; //conn.getUserInfo().prefs.getBoolean("mode-showsymbol");
		//} catch (JSONException e) {
		//}

        /*JsonObject PREFIX = null;
        ServersDataSource.Server s = ServersDataSource.getInstance().getServer(cid);
        if(s != null)
            PREFIX = s.PREFIX;

        if(PREFIX == null) {
            PREFIX = new JsonObject();
            PREFIX.addProperty(s!=null?s.MODE_OWNER:"q", "~");
            PREFIX.addProperty(s!=null?s.MODE_ADMIN:"a", "&");
            PREFIX.addProperty(s!=null?s.MODE_OP:"o", "@");
            PREFIX.addProperty(s!=null?s.MODE_HALFOP:"h", "%");
            PREFIX.addProperty(s!=null?s.MODE_VOICED:"v", "+");
        }*/


        if(adapter == null) {
			adapter = new UserListAdapter(UsersListFragment.this);
		}

		/*for(int i = 0; i < users.size(); i++) {
			UsersDataSource.User user = users.get(i);
			if(user.mode.contains(s!=null?s.MODE_OWNER:"q")) {
				owners.add(user);
			} else if(user.mode.contains(s!=null?s.MODE_ADMIN:"a")) {
				admins.add(user);
			} else if(user.mode.contains(s!=null?s.MODE_OP:"o")) {
				ops.add(user);
			} else if(user.mode.contains(s!=null?s.MODE_HALFOP:"h")) {
				halfops.add(user);
			} else if(user.mode.contains(s!=null?s.MODE_VOICED:"v")) {
				voiced.add(user);
			} else {
				members.add(user);
			}
		}*/
		
		if(owners.size() > 0) {
			addUsersFromList(entries, owners, "OWNER", "", R.color.heading_owner, R.drawable.row_owners_bg, R.drawable.owner_bg);
		}
		
		if(admins.size() > 0) {
			addUsersFromList(entries, admins, "ADMINS", "", R.color.heading_admin, R.drawable.row_admins_bg, R.drawable.admin_bg);
		}
		
		if(ops.size() > 0) {
			addUsersFromList(entries, ops, "OPS", "", R.color.heading_operators, R.drawable.row_operator_bg, R.drawable.operator_bg);
		}
		
		if(halfops.size() > 0) {
			addUsersFromList(entries, halfops, "HALF OPS", "", R.color.heading_halfop, R.drawable.row_halfops_bg, R.drawable.halfop_bg);
		}
		
		if(voiced.size() > 0) {
			addUsersFromList(entries, voiced, "VOICED", "", R.color.heading_voiced, R.drawable.row_voiced_bg, R.drawable.voiced_bg);
		}
		
		if(members.size() > 0) {
			addUsersFromList(entries, members, "MEMBERS", "", R.color.heading_members, R.drawable.row_members_bg, R.drawable.background_blue);
		}

		adapter.setItems(entries);
		
		if(getListAdapter() == null && entries.size() > 0)
			setListAdapter(adapter);
		else
			adapter.notifyDataSetChanged();
	}
	
	private class RefreshTask extends AsyncTask<Void, Void, Void> {
		ArrayList<UsersDataSource.User> users = null;
		
		@Override
		protected Void doInBackground(Void... params) {
	    	if(ChannelsDataSource.getInstance().getChannelForBuffer(bid) != null)
	    		users = UsersDataSource.getInstance().getUsersForBuffer(cid, bid);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(!isCancelled()) {
				refresh(users);
				refreshTask = null;
			}
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(savedInstanceState != null && savedInstanceState.containsKey("cid")) {
        	cid = savedInstanceState.getInt("cid");
        	bid = savedInstanceState.getInt("bid");
        	channel = savedInstanceState.getString("channel");
        }
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		return inflater.inflate(R.layout.userslist, null);
	}

	public void onResume() {
    	super.onResume();
    	conn = NetworkConnection.getInstance();
    	conn.addHandler(mHandler);
    	ArrayList<UsersDataSource.User> users = UsersDataSource.getInstance().getUsersForBuffer(cid, bid);
    	refresh(users);
    }
    
    @Override
    public void setArguments(Bundle args) {
    	cid = args.getInt("cid", 0);
    	bid = args.getInt("bid", 0);
    	channel = args.getString("name");
    	
    	mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
		    	ArrayList<UsersDataSource.User> users = null;
		    	if(ChannelsDataSource.getInstance().getChannelForBuffer(bid) != null)
		    		users = UsersDataSource.getInstance().getUsersForBuffer(cid, bid);
		    	refresh(users);
		    	try {
			    	if(getListView() != null)
			    		getListView().setSelection(0);
				} catch (SkipException e) {
					throw e; // not to block SkipExceptions
				} catch (Exception e) { //Sometimes the list view isn't available yet
		    	}
			}
    		
    	}, 100);
    }
	
    public void onPause() {
    	super.onPause();
    	if(conn != null)
    		conn.removeHandler(mHandler);
        if(refreshTimer != null)
            refreshTimer.cancel();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUserSelectedListener");
        }
        if(cid == -1) {
	        cid = activity.getIntent().getIntExtra("cid", 0);
	        channel = activity.getIntent().getStringExtra("name");
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle state) {
    	super.onSaveInstanceState(state);
    	state.putInt("cid", cid);
    	state.putInt("bid", bid);
    	state.putString("channel", channel);
    }
    
    private class OnItemLongClickListener implements OnLongClickListener {
        private int pos;
        OnItemLongClickListener(int position){
            pos = position;
        }
        
		@Override
		public boolean onLongClick(View v) {
            if(pos < adapter.getCount()) {
                UserListAdapter.UserListEntry e = (UserListAdapter.UserListEntry)adapter.getItem(pos);
                if(e.type == TYPE_USER) {
                    mListener.onUserSelected(cid, channel, e.text);
                    return true;
                }
            }
	    	return false;
		}
    	
    }
    
    private class OnItemClickListener implements OnClickListener {
        private int pos;
        OnItemClickListener(int position){
            pos = position;
        }
        
        @Override
        public void onClick(View arg0) {
	    	if(pos < 0)
	    		return;

	    	if(adapter != null) {
	    		if(tapTimer != null) {
	    			tapTimer.cancel();
	    			tapTimer = null;
                    try {
                        UserListAdapter.UserListEntry e = (UserListAdapter.UserListEntry)adapter.getItem(pos);
                        if(e.type == TYPE_USER)
                            mListener.onUserDoubleClicked(e.text);
					} catch (SkipException e) {
						throw e; // not to block SkipExceptions
					} catch (Exception e) {

                    }
	    		} else {
	    			// tapTimer = new Timer(); // remove calls to Timer
	    			//tapTimer.schedule(
							new TimerTask() {
								int position = pos;

								@Override
								public void run() {
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											try {
												UserListAdapter.UserListEntry e = (UserListAdapter.UserListEntry) adapter.getItem(position);
												if (e.type == TYPE_USER) {
													mListener.onUserSelected(cid, channel, e.text);

													if (!getActivity().getSharedPreferences("prefs", 0).getBoolean("longPressTip", false)) {
														Toast.makeText(getActivity(), "Long-press a message to quickly interact with the sender", Toast.LENGTH_LONG).show();
														SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", 0).edit();
														editor.putBoolean("longPressTip", true);
														editor.commit();
													}
												}
											} catch (SkipException e) {
												throw e; // not to block SkipExceptions
											} catch (Exception e) {

											}
										}
									});
									tapTimer = null;
								}

								//}, 300);
							}.run(); // made synchronous
	    		}
	    	}
    	}
    }

    
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetworkConnection.EVENT_CONNECTIVITY:
				if(adapter != null)
					adapter.notifyDataSetChanged();
				break;
			case NetworkConnection.EVENT_USERINFO:
			case NetworkConnection.EVENT_CHANNELINIT:
			case NetworkConnection.EVENT_JOIN:
			case NetworkConnection.EVENT_PART:
			case NetworkConnection.EVENT_QUIT:
			case NetworkConnection.EVENT_NICKCHANGE:
			case NetworkConnection.EVENT_MEMBERUPDATES:
			case NetworkConnection.EVENT_USERCHANNELMODE:
			case NetworkConnection.EVENT_KICK:
			case NetworkConnection.EVENT_BACKLOG_END:
                if(refreshTimer == null) {
                    /* refreshTimer = new Timer(); // remove calls to Timer
                    /refreshTimer.schedule(
							new TimerTask() {
								@Override
								public void run() {
									if (refreshTask != null)
										refreshTask.cancel(true);
									refreshTask = new RefreshTask();
									refreshTask.execute((Void) null);
									refreshTimer = null;
								}
						}, 250);
						*/
                }
				break;
			default:
				break;
			}
		}
	};
	
	public interface OnUserSelectedListener {
		public void onUserSelected(int cid, String channel, String name);
		public void onUserDoubleClicked(String name);
	}
}
