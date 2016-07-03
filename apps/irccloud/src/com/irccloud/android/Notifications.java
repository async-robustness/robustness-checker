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

import java.util.*;

import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import checker.SkipException;

public class Notifications {
	public class Notification {
		public int cid;
		public int bid;
		public long eid;
		public String nick;
		public String message;
		public String network;
		public String chan;
		public String buffer_type;
		public String message_type;
		public boolean shown = false;
		
		public String toString() {
			return "{cid: " + cid + ", bid: " + bid + ", eid: " + eid + ", nick: " + nick + ", message: " + message + ", network: " + network + " shown: " + shown + "}";
		}
	}
	
	public class comparator implements Comparator<Notification> {
		public int compare(Notification n1, Notification n2) {
			if(n1.cid != n2.cid)
				return Integer.valueOf(n1.cid).compareTo(n2.cid);
			else if(n1.bid != n2.bid)
				return Integer.valueOf(n1.bid).compareTo(n2.bid);
			else
				return Long.valueOf(n1.eid).compareTo(n2.eid);
		}
	}
	
	private ArrayList<Notification> mNotifications = null;
	private SparseArray<String> mNetworks = null;
	private SparseArray<Long> mLastSeenEIDs = null;
	private SparseArray<HashSet<Long>> mDismissedEIDs = null;
	
	private static Notifications instance;
	private int excludeBid = -1;
	private Timer mNotificationTimer = null;

	public static Notifications getInstance() {
		if(instance == null)
			instance = new Notifications();
		return instance;
	}
	
	public Notifications() {
		load();
	}

	private void load() {
		mNotifications = new ArrayList<Notification>();
		mNetworks = new SparseArray<String>();
		mLastSeenEIDs = new SparseArray<Long>();
		mDismissedEIDs = new SparseArray<HashSet<Long>>();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext());

		if(prefs.contains("notifications_json")) {
			/*try {
				JSONArray array = new JSONArray(prefs.getString("networks_json", "{}"));
				for(int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					mNetworks.put(o.getInt("cid"), o.getString("network"));
				}
				
				array = new JSONArray(prefs.getString("lastseeneids_json", "{}"));
				for(int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					mLastSeenEIDs.put(o.getInt("bid"), o.getLong("eid"));
				}
				
				array = new JSONArray(prefs.getString("dismissedeids_json", "{}"));
				for(int i = 0; i < array.length(); i++) {
					JSONObject o = array.getJSONObject(i);
					int bid = o.getInt("bid");
					mDismissedEIDs.put(bid, new HashSet<Long>());
					
					JSONArray eids = o.getJSONArray("eids");
					for(int j = 0; j < eids.length(); j++) {
						mDismissedEIDs.get(bid).add(eids.getLong(j));
					}
				}
				
				synchronized(mNotifications) {
					array = new JSONArray(prefs.getString("notifications_json", "{}"));
					for(int i = 0; i < array.length(); i++) {
						JSONObject o = array.getJSONObject(i);
						Notification n = new Notification();
						n.bid = o.getInt("bid");
						n.cid = o.getInt("cid");
						n.eid = o.getLong("eid");
						n.nick = o.getString("nick");
						n.message = o.getString("message");
						n.chan = o.getString("chan");
						n.buffer_type = o.getString("buffer_type");
						n.message_type = o.getString("message_type");
						n.network = mNetworks.get(n.cid);
						if(o.has("shown"))
							n.shown = o.getBoolean("shown");
						mNotifications.add(n);
					}
					Collections.sort(mNotifications, new comparator());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	private Timer mSaveTimer = null;
	
	private void save() {
		if(mSaveTimer != null)
			mSaveTimer.cancel();
		//mSaveTimer = new Timer();  // remove calls to Timer
		/*mSaveTimer.schedule(
				new TimerTask() {

					@Override
					public void run() {
						SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext()).edit();
				try {
					JSONArray array = new JSONArray();
					synchronized(mNotifications) {
						for(Notification n : mNotifications) {
							JSONObject o = new JSONObject();
							o.put("cid", n.cid);
							o.put("bid", n.bid);
							o.put("eid", n.eid);
							o.put("nick", n.nick);
							o.put("message", n.message);
							o.put("chan", n.chan);
							o.put("buffer_type", n.buffer_type);
							o.put("message_type", n.message_type);
							o.put("shown", n.shown);
							array.put(o);
						}
						editor.putString("notifications_json", array.toString());
					}
					
					array = new JSONArray();
					for(int i = 0; i < mNetworks.size(); i++) {
						int cid = mNetworks.keyAt(i);
						String network = mNetworks.get(cid);
						JSONObject o = new JSONObject();
						o.put("cid", cid);
						o.put("network", network);
						array.put(o);
					}
					editor.putString("networks_json", array.toString());

					array = new JSONArray();
					for(int i = 0; i < mLastSeenEIDs.size(); i++) {
						int bid = mLastSeenEIDs.keyAt(i);
						long eid = mLastSeenEIDs.get(bid);
						JSONObject o = new JSONObject();
						o.put("bid", bid);
						o.put("eid", eid);
						array.put(o);
					}
					editor.putString("lastseeneids_json", array.toString());

					array = new JSONArray();
					for(int i = 0; i < mDismissedEIDs.size(); i++) {
						JSONArray a = new JSONArray();
						int bid = mDismissedEIDs.keyAt(i);
						HashSet<Long> eids = mDismissedEIDs.get(bid);
						for(long eid : eids) {
							a.put(eid);
						}
						JSONObject o = new JSONObject();
						o.put("bid", bid);
						o.put("eids", a);
						array.put(o);
					}
					editor.putString("dismissedeids_json", array.toString());

					if(Build.VERSION.SDK_INT >= 9)
						editor.apply();
					else
						editor.commit();
                } catch (ConcurrentModificationException e) {
                } catch (OutOfMemoryError e) {
                    editor.remove("notifications_json");
                    editor.remove("networks_json");
                    editor.remove("lastseeneids_json");
                    editor.remove("dismissedeids_json");
                    editor.commit();
                } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						mSaveTimer = null;
					}

			}, 60000);*/
	}
	
	public void clearDismissed() {
		mDismissedEIDs.clear();
		save();
	}
	
	public void clear() {
		try {
	        NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			
			synchronized(mNotifications) {
				if(mNotifications.size() > 0) {
			        for(Notification n : mNotifications) {
		        		nm.cancel((int)(n.eid/1000));
		        		nm.cancel(n.bid);
			        }
				}
				mNotifications.clear();
			}
			mNetworks.clear();
			mLastSeenEIDs.clear();
			save();
            IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));

		} catch (SkipException e) {
			throw e; // not to block SkipExceptions
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public long getLastSeenEid(int bid) {
		if(mLastSeenEIDs.get(bid) != null)
			return mLastSeenEIDs.get(bid);
		else
			return -1;
	}
	
	public synchronized void updateLastSeenEid(int bid, long eid) {
		mLastSeenEIDs.put(bid, eid);
		save();
	}

	public synchronized boolean isDismissed(int bid, long eid) {
		if(mDismissedEIDs.get(bid) != null) {
			for(Long e : mDismissedEIDs.get(bid)) {
				if(e == eid)
					return true;
			}
		}
		return false;
	}
	
	public synchronized void dismiss(int bid, long eid) {
		if(mDismissedEIDs.get(bid) == null)
			mDismissedEIDs.put(bid, new HashSet<Long>());
		
		mDismissedEIDs.get(bid).add(eid);
		Notification n = getNotification(eid);
		synchronized(mNotifications) {
			if(n != null)
				mNotifications.remove(n);
		}
		save();
        IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
	}
	
	public synchronized void addNetwork(int cid, String network) {
		mNetworks.put(cid, network);
		save();
	}

	public synchronized void deleteNetwork(int cid) {
		mNetworks.remove(cid);
		save();
	}
	
	public synchronized void addNotification(int cid, int bid, long eid, String from, String message, String chan, String buffer_type, String message_type) {
		if(isDismissed(bid, eid)) {
			return;
		}
		long last_eid = getLastSeenEid(bid);
		if(eid <= last_eid) {
			return;
		}
		
		String network = getNetwork(cid);
		if(network == null)
			addNetwork(cid, "Unknown Network");
		Notification n = new Notification();
		n.bid = bid;
		n.cid = cid;
		n.eid = eid;
		n.nick = from;
		n.message = TextUtils.htmlEncode(message);
		n.chan = chan;
		n.buffer_type = buffer_type;
		n.message_type = message_type;
		n.network = network;
		
		synchronized(mNotifications) {
			mNotifications.add(n);
			Collections.sort(mNotifications, new comparator());
		}
		save();
	}

	public void deleteNotification(int cid, int bid, long eid) {
		synchronized(mNotifications) {
			for(Notification n : mNotifications) {
				if(n.cid == cid && n.bid == bid && n.eid == eid) {
					mNotifications.remove(n);
					save();
					return;
				}
			}
		}
	}
	
	public void deleteOldNotifications(int bid, long last_seen_eid) {
        boolean changed = false;
		if(mNotificationTimer != null) {
			mNotificationTimer.cancel();
			mNotificationTimer = null;
		}
		
		NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		ArrayList<Notification> notifications = getOtherNotifications();
		
		if(notifications.size() > 0) {
	        for(Notification n : notifications) {
	        	if(n.bid == bid && n.eid <= last_seen_eid) {
	        		nm.cancel((int)(n.eid/1000));
                    changed = true;
	        	}
	        }
		}
		
		synchronized(mNotifications) {
			for(int i = 0; i < mNotifications.size(); i++) {
				Notification n = mNotifications.get(i);
				if(n.bid == bid && n.eid <= last_seen_eid) {
					mNotifications.remove(n);
					i--;
					nm.cancel(bid);
                    changed = true;
					continue;
				}
			}
		}
		if(mDismissedEIDs.get(bid) != null) {
			HashSet<Long> eids = mDismissedEIDs.get(bid);
			Long[] eidsArray = eids.toArray(new Long[eids.size()]);
			for(int i = 0; i < eidsArray.length; i++) {
				if(eidsArray[i] <= last_seen_eid) {
					eids.remove(eidsArray[i]);
				}
			}
		}
        if(changed) {
            IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
        }
	}
	
	public void deleteNotificationsForBid(int bid) {
        NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		ArrayList<Notification> notifications = getOtherNotifications();
		
		if(notifications.size() > 0) {
	        for(Notification n : notifications) {
	        	if(n.bid == bid)
	        		nm.cancel((int)(n.eid/1000));
	        }
		}
		nm.cancel(bid);

		synchronized(mNotifications) {
			for(int i = 0; i < mNotifications.size(); i++) {
				Notification n = mNotifications.get(i);
				if(n.bid == bid) {
					mNotifications.remove(n);
					i--;
					continue;
				}
			}
		}
		mDismissedEIDs.remove(bid);
		mLastSeenEIDs.remove(bid);
        IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
	}
	
	private boolean isMessage(String type) {
		return !(type.equalsIgnoreCase("channel_invite") || type.equalsIgnoreCase("callerid"));
	}

    public int count() {
        return mNotifications.size();
    }

	public ArrayList<Notification> getMessageNotifications() {
		ArrayList<Notification> notifications = new ArrayList<Notification>();

		synchronized(mNotifications) {
			for(int i = 0; i < mNotifications.size(); i++) {
				Notification n = mNotifications.get(i);
				if(n.bid != excludeBid && isMessage(n.message_type)) {
					if(n.network == null)
						n.network = getNetwork(n.cid);
					notifications.add(n);
				}
			}
		}
		return notifications;
	}
	
	public ArrayList<Notification> getOtherNotifications() {
		ArrayList<Notification> notifications = new ArrayList<Notification>();

		synchronized(mNotifications) {
			for(int i = 0; i < mNotifications.size(); i++) {
				Notification n = mNotifications.get(i);
				if(n.bid != excludeBid && !isMessage(n.message_type)) {
					if(n.network == null)
						n.network = getNetwork(n.cid);
					notifications.add(n);
				}
			}
		}
		return notifications;
	}
	
	public String getNetwork(int cid) {
		return mNetworks.get(cid);
	}
	
	public Notification getNotification(long eid) {
		synchronized(mNotifications) {
			for(int i = 0; i < mNotifications.size(); i++) {
				Notification n = mNotifications.get(i);
				if(n.bid != excludeBid && n.eid == eid && isMessage(n.message_type)) {
					if(n.network == null)
						n.network = getNetwork(n.cid);
					return n;
				}
			}
		}
		return null;
	}
	
	public synchronized void excludeBid(int bid) {
		excludeBid = -1;
        NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		ArrayList<Notification> notifications = getOtherNotifications();
		
		if(notifications.size() > 0) {
	        for(Notification n : notifications) {
	        	if(n.bid == bid)
	        		nm.cancel((int)(n.eid/1000));
	        }
		}
		nm.cancel(bid);
		excludeBid = bid;
	}
	
	private String mTicker = null;
	
	public synchronized void showNotifications(String ticker) {
		if(ticker != null)
			mTicker = ticker;
		
		ArrayList<Notification> notifications = getMessageNotifications();
		for(Notification n : notifications) {
			if(isDismissed(n.bid, n.eid)) {
				deleteNotification(n.cid, n.bid, n.eid);
			}
		}

		if(mNotificationTimer != null)
			mNotificationTimer.cancel();

        /*try {
            // mNotificationTimer = new Timer(); // remove calls to Timer
            mNotificationTimer.schedule(
					new TimerTask() {
						@Override
						public void run() {
							showMessageNotifications(mTicker);
							showOtherNotifications();
							mTicker = null;
							mNotificationTimer = null;
							// commented out DashClock
							//IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
						}
					 }, 5000);
		} catch (SkipException e) {
			throw e; // not to block SkipExceptions
		} catch (Exception e) {

        }*/

		// made synchronous
		new Runnable() {
			@Override
			public void run() {
				showMessageNotifications(mTicker);
				showOtherNotifications();
				mTicker = null;
				mNotificationTimer = null;
				// commented out DashClock
				//IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
			}
		}.run();
	}
	
	private void showOtherNotifications() {
		String title = "";
		String text = "";
		String ticker = null;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext());
        NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		ArrayList<Notification> notifications = getOtherNotifications();
		
		int notify_type = Integer.parseInt(prefs.getString("notify_type", "1"));
		boolean notify = false;
		if(notify_type == 1 || (notify_type == 2 && NetworkConnection.getInstance().isVisible()))
			notify = true;
		
		if(notifications.size() > 0 && notify) {
	        for(Notification n : notifications) {
	        	if(!n.shown) {
					if(n.message_type.equals("callerid")) {
						title = "Callerid: " + n.nick + " (" + n.network + ")";
						text = n.nick + " " + n.message;
						ticker = n.nick + " " + n.message;
					} else {
						title = n.nick + " (" + n.network + ")";
						text = n.message;
						ticker = n.message;
					}
			        nm.notify((int)(n.eid/1000), buildNotification(ticker, n.bid, new long[] {n.eid}, title, text, Html.fromHtml(text), 1));
			        n.shown = true;
	        	}
	        }
		}
	}
	
	private android.app.Notification buildNotification(String ticker, int bid, long[] eids, String title, String text, Spanned big_text, int count) {
		return new android.app.Notification();
	}

    private void notifyPebble(String title, String body) {
        /*JSONObject jsonData = new JSONObject();
        try {
            final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");
            jsonData.put("title", title);
            jsonData.put("body", body);
            final String notificationData = new JSONArray().put(jsonData).toString();

            i.putExtra("messageType", "PEBBLE_ALERT");
            i.putExtra("sender", "IRCCloud");
            i.putExtra("notificationData", notificationData);
            IRCCloudApplication.getInstance().getApplicationContext().sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

	private void showMessageNotifications(String ticker) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext());
		String title = "";
		String text = "";
        NotificationManager nm = (NotificationManager)IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		ArrayList<Notification> notifications = getMessageNotifications();

		int notify_type = Integer.parseInt(prefs.getString("notify_type", "1"));
		boolean notify = false;
		if(notify_type == 1 || (notify_type == 2 && NetworkConnection.getInstance().isVisible()))
			notify = true;
		
		if(notifications.size() > 0 && notify) {
			int lastbid = notifications.get(0).bid;
			int count = 0;
			long[] eids = new long[notifications.size()];
			Notification last = null;
			count = 0;
    		title = notifications.get(0).chan + " (" + notifications.get(0).network + ")";
    		boolean show = false;
	        for(Notification n : notifications) {
	        	if(n.bid != lastbid) {
	        		if(show) {
						if(count == 1) {
							if(last.nick != null && last.nick.length() > 0) {
								title = last.nick;
								if(!last.buffer_type.equals("conversation") && !last.message_type.equals("wallops") && last.chan.length() > 0)
									title += " in " + last.chan;
								title += " (" + last.network + ")";
							} else {
								title = last.network;
							}
				        	if(last.message_type.equals("buffer_me_msg"))
				        		text = "— " + last.message;
				        	else
				        		text = last.message;
							nm.notify(lastbid, buildNotification(ticker, lastbid, eids, title, Html.fromHtml(text).toString(), Html.fromHtml(text), count));
						} else {
							nm.notify(lastbid, buildNotification(ticker, lastbid, eids, title, count + " unread highlight" + ((count == 1)?".":"s."), Html.fromHtml(text), count));
						}
	        		}
	        		lastbid = n.bid;
	        		if(n.chan != null && n.chan.length() > 0)
	        			title = n.chan + " (" + n.network + ")";
	        		else
	        			title = n.network;
	        		text = "";
					count = 0;
					eids = new long[notifications.size()];
					show = false;
	        	}
	        	if(count < 4) {
	        		if(text.length() > 0)
	        			text += "<br/>";
		        	if(n.buffer_type.equals("conversation") && n.message_type.equals("buffer_me_msg"))
		        		text += "— " + n.message;
		        	else if(n.buffer_type.equals("conversation"))
		        		text += n.message;
		        	else if(n.message_type.equals("buffer_me_msg"))
			    		text += "<b>— " + n.nick + "</b> " + n.message;
		        	else
			    		text += "<b>" + n.nick + "</b> " + n.message;
	        	}
	        	if(!n.shown) {
	        		n.shown = true;
	        		show = true;

                    if(prefs.getBoolean("notify_pebble", false)) {
                        String pebbleTitle = n.network + ":\n";
                        String pebbleBody = "";
                        if(n.buffer_type.equals("channel") && n.chan != null && n.chan.length() > 0)
                            pebbleTitle = n.chan + ":\n";

                        if(n.message_type.equals("buffer_me_msg"))
                            pebbleBody = "— " + n.message;
                        else
                            pebbleBody = n.message;

                        if(n.nick != null && n.nick.length() > 0)
                            notifyPebble(n.nick, pebbleTitle + Html.fromHtml(pebbleBody).toString());
                        else
                            notifyPebble(n.network, pebbleTitle + Html.fromHtml(pebbleBody).toString());
                    }
                }
	        	eids[count++] = n.eid;
	        	last = n;
	        }
	        if(show) {
				if(count == 1) {
					if(last.nick != null && last.nick.length() > 0) {
						title = last.nick;
						if(!last.buffer_type.equals("conversation") && !last.message_type.equals("wallops") && last.chan.length() > 0)
							title += " in " + last.chan;
						title += " (" + last.network + ")";
					} else {
						title = last.network;
					}
		        	if(last.message_type.equals("buffer_me_msg"))
		        		text = "— " + last.message;
		        	else
		        		text = last.message;
					nm.notify(lastbid, buildNotification(ticker, lastbid, eids, title, Html.fromHtml(text).toString(), Html.fromHtml(text), count));
				} else {
					nm.notify(lastbid, buildNotification(ticker, lastbid, eids, title, count + " unread highlight" + ((count == 1)?".":"s."), Html.fromHtml(text), count));
				}
	        }
		}
	}
}