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

package com.irccloud.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.*;
import android.preference.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import checker.SkipException;
import com.irccloud.android.BuildConfig;
import com.irccloud.android.DashClock;
import com.irccloud.android.IRCCloudJSONObject;
import com.irccloud.android.NetworkConnection;
import com.irccloud.android.Notifications;
import com.irccloud.android.R;
import com.irccloud.android.data.ServersDataSource;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PreferencesActivity extends PreferenceActivity {
	NetworkConnection conn;
	SaveSettingsTask saveSettingsTask = null;
	SavePreferencesTask savePreferencesTask = null;
	int save_prefs_reqid = -1;
	int save_settings_reqid = -1;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        if(Build.VERSION.SDK_INT >= 11) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		conn = NetworkConnection.getInstance();
		addPreferencesFromResource(R.xml.preferences_account);
		addPreferencesFromResource(R.xml.preferences_display);
		addPreferencesFromResource(R.xml.preferences_device);
		addPreferencesFromResource(R.xml.preferences_notifications);
        addPreferencesFromResource(R.xml.preferences_dashclock);
        findPreference("dashclock_showmsgs").setOnPreferenceChangeListener(dashclocktoggle);
        try {
            getPackageManager().getPackageInfo("com.getpebble.android", 0);
            addPreferencesFromResource(R.xml.preferences_pebble);
		} catch (SkipException e) {
			throw e; // not to block SkipExceptions
		} catch (Exception e) {
        }
        boolean foundSony=false;
        if(!foundSony) {
            try {
                getPackageManager().getPackageInfo("com.sonyericsson.extras.liveware", 0);
                addPreferencesFromResource(R.xml.preferences_sony);
                foundSony = true;
			} catch (SkipException e) {
				throw e; // not to block SkipExceptions
			} catch (Exception e) {
            }
        }
        if(!foundSony) {
            try {
                getPackageManager().getPackageInfo("com.sonyericsson.extras.smartwatch", 0);
                addPreferencesFromResource(R.xml.preferences_sony);
                foundSony = true;
			} catch (SkipException e) {
				throw e; // not to block SkipExceptions
			} catch (Exception e) {
            }
        }
        if(!foundSony) {
            try {
                getPackageManager().getPackageInfo("com.sonyericsson.extras.liveview", 0);
                addPreferencesFromResource(R.xml.preferences_sony);
                foundSony = true;
			} catch (SkipException e) {
				throw e; // not to block SkipExceptions
			} catch (Exception e) {
            }
        }
        if(foundSony)
            findPreference("notify_sony").setOnPreferenceChangeListener(sonytoggle);
		addPreferencesFromResource(R.xml.preferences_about);
		findPreference("name").setOnPreferenceChangeListener(settingstoggle);
		if(conn.getUserInfo() != null)
			findPreference("name").setSummary(conn.getUserInfo().name);
		else
			findPreference("name").setSummary(((EditTextPreference)findPreference("name", EditTextPreference.class)).getText());
		findPreference("email").setOnPreferenceChangeListener(settingstoggle);
		if(conn.getUserInfo() != null)
			findPreference("email").setSummary(conn.getUserInfo().email);
		else
			findPreference("email").setSummary(((EditTextPreference)findPreference("email", EditTextPreference.class)).getText());
		findPreference("highlights").setOnPreferenceChangeListener(settingstoggle);
		if(conn.getUserInfo() != null)
			findPreference("highlights").setSummary(conn.getUserInfo().highlights);
		else
			findPreference("highlights").setSummary(((EditTextPreference)findPreference("highlights", EditTextPreference.class)).getText());
		findPreference("autoaway").setOnPreferenceChangeListener(settingstoggle);
		findPreference("time-24hr").setOnPreferenceChangeListener(prefstoggle);
		findPreference("time-seconds").setOnPreferenceChangeListener(prefstoggle);
		findPreference("mode-showsymbol").setOnPreferenceChangeListener(prefstoggle);
        findPreference("nick-colors").setOnPreferenceChangeListener(prefstoggle);
		findPreference("faq").setOnPreferenceClickListener(urlClick);
		findPreference("feedback").setOnPreferenceClickListener(urlClick);
        findPreference("licenses").setOnPreferenceClickListener(licensesClick);
		//findPreference("subscriptions").setOnPreferenceClickListener(urlClick);
		//findPreference("changes").setOnPreferenceClickListener(urlClick);
		findPreference("notify_type").setOnPreferenceChangeListener(notificationstoggle);
		switch(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("notify_type", "1"))) {
			case 0:
				findPreference("notify_type").setSummary("Disabled");
				break;
			case 1:
				findPreference("notify_type").setSummary("Enabled");
				break;
			case 2:
				findPreference("notify_type").setSummary("Only while active");
				break;
		}
		if(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("notify_type", "1")) > 0) {
			findPreference("notify_vibrate").setEnabled(true);
			findPreference("notify_ringtone").setEnabled(true);
			findPreference("notify_lights").setEnabled(true);
		} else {
			findPreference("notify_vibrate").setEnabled(false);
			findPreference("notify_ringtone").setEnabled(false);
			findPreference("notify_lights").setEnabled(false);
		}

		try {
            if(BuildConfig.REVISION.length() > 0)
    			findPreference("version").setSummary(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "-" + BuildConfig.REVISION);
            else
                findPreference("version").setSummary(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
    	}
        return super.onOptionsItemSelected(item);
    }
	
    @Override
    public void onResume() {
    	super.onResume();
    	conn = NetworkConnection.getInstance();
    	conn.addHandler(mHandler);
    }

    @Override
    public void onPause() {
    	super.onPause();

    	if(conn != null) {
        	conn.removeHandler(mHandler);
    	}
	}

	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			IRCCloudJSONObject o;
			
			switch(msg.what) {
			case NetworkConnection.EVENT_SUCCESS:
				o = (IRCCloudJSONObject)msg.obj;
				if(o.has("_reqid")) {
					if(o.getInt("_reqid") == save_settings_reqid) {
						save_settings_reqid = -1;
					} else if(o.getInt("_reqid") == save_prefs_reqid) {
						save_prefs_reqid = -1;
					}
				}
				break;
			case NetworkConnection.EVENT_FAILURE_MSG:
				o = (IRCCloudJSONObject)msg.obj;
				if(o.has("_reqid")) {
					if(o.getInt("_reqid") == save_settings_reqid) {
						save_settings_reqid = -1;
						Log.e("IRCCloud", "Settings not updated: " + o.getString("message"));
					} else if(o.getInt("_reqid") == save_prefs_reqid) {
						save_prefs_reqid = -1;
						Log.e("IRCCloud", "Prefs not updated: " + o.getString("message"));
					}
					Toast.makeText(PreferencesActivity.this, "An error occured while saving settings.  Please try again.", Toast.LENGTH_SHORT).show();
				}
				break;
			case NetworkConnection.EVENT_USERINFO:
				NetworkConnection.UserInfo userInfo = conn.getUserInfo();
				if(userInfo != null) {
					/*JSONObject prefs = userInfo.prefs;
					((EditTextPreference)findPreference("name")).setText(userInfo.name);
					findPreference("name").setSummary(userInfo.name);
					((EditTextPreference)findPreference("email")).setText(userInfo.email);
					findPreference("email").setSummary(userInfo.email);
					((EditTextPreference)findPreference("highlights")).setText(userInfo.highlights);
					findPreference("highlights").setSummary(userInfo.highlights);
					((CheckBoxPreference)findPreference("autoaway")).setChecked(userInfo.auto_away);
					if(prefs != null) {
						try {
							((CheckBoxPreference)findPreference("time-24hr")).setChecked(prefs.has("time-24hr")?prefs.getBoolean("time-24hr"):false);
							((CheckBoxPreference)findPreference("time-seconds")).setChecked(prefs.has("time-seconds")?prefs.getBoolean("time-seconds"):false);
							((CheckBoxPreference)findPreference("mode-showsymbol")).setChecked(prefs.has("mode-showsymbol")?prefs.getBoolean("mode-showsymbol"):false);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}*/
				}
				break;
			}
		}
    };
    
	public Preference.OnPreferenceChangeListener settingstoggle = new Preference.OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if(preference.getKey().equals("name")) {
				conn.getUserInfo().name = (String)newValue;
				findPreference("name").setSummary((String)newValue);
			} else if(preference.getKey().equals("email")) {
				conn.getUserInfo().email = (String)newValue;
				findPreference("email").setSummary((String)newValue);
			} else if(preference.getKey().equals("highlights")) {
				conn.getUserInfo().highlights = (String)newValue;
				findPreference("highlights").setSummary((String)newValue);
			} else if(preference.getKey().equals("autoaway")) {
				conn.getUserInfo().auto_away = (Boolean)newValue;
			}
			if(saveSettingsTask != null)
				saveSettingsTask.cancel(true);
			saveSettingsTask = new SaveSettingsTask();
			saveSettingsTask.execute((Void)null);
			return true;
		}
	};

    Preference.OnPreferenceChangeListener dashclocktoggle = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            sendBroadcast(new Intent(DashClock.REFRESH_INTENT));
            return true;
        }
    };

    Preference.OnPreferenceChangeListener sonytoggle = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
			// removed call to com.sonyericsson.extras.liveware.extension.util.notification.NotificationUtil;
            return true;
        }
    };

    public Preference.OnPreferenceChangeListener prefstoggle = new Preference.OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			/*JSONObject prefs = conn.getUserInfo().prefs;
			try {
				if(prefs == null) {
					prefs = new JSONObject();
					conn.getUserInfo().prefs = prefs;
				}
	
                prefs.put(preference.getKey(), (Boolean)newValue);
			} catch (JSONException e) {
				prefs = null;
			}*/
			if(savePreferencesTask != null)
				savePreferencesTask.cancel(true);
			//if(prefs != null) {
				savePreferencesTask = new SavePreferencesTask();
				savePreferencesTask.execute((Void)null);
			//}
			return true;
		}
	};
	
	Preference.OnPreferenceChangeListener notificationstoggle = new Preference.OnPreferenceChangeListener() {
		@SuppressWarnings("deprecation")
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			switch(Integer.parseInt((String)newValue)) {
			case 0:
				findPreference("notify_type").setSummary("Disabled");
				break;
			case 1:
				findPreference("notify_type").setSummary("Enabled");
				break;
			case 2:
				findPreference("notify_type").setSummary("Only while active");
				break;
			}
			if(Integer.parseInt((String)newValue) > 0) {
				findPreference("notify_vibrate").setEnabled(true);
				findPreference("notify_ringtone").setEnabled(true);
				findPreference("notify_lights").setEnabled(true);
			} else {
				findPreference("notify_vibrate").setEnabled(false);
				findPreference("notify_ringtone").setEnabled(false);
				findPreference("notify_lights").setEnabled(false);
				Notifications.getInstance().clear();
			}
			return true;
		}
	};
	
	private class SavePreferencesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}*/
			if(!isCancelled() && conn.getUserInfo() != null && conn.getUserInfo().prefs != null)
				save_prefs_reqid = conn.set_prefs(conn.getUserInfo().prefs.toString());
			else
				save_prefs_reqid = -1;
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			savePreferencesTask = null;
		}
		
	};
	
	private class SaveSettingsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}*/
			if(!isCancelled()) {
				NetworkConnection.UserInfo userInfo = conn.getUserInfo();
				if(userInfo != null)
					save_settings_reqid = conn.set_user_settings(userInfo.email, userInfo.name, userInfo.highlights, userInfo.auto_away);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			saveSettingsTask = null;
		}
		
	};

    Preference.OnPreferenceClickListener licensesClick = new Preference.OnPreferenceClickListener() {

        public boolean onPreferenceClick(Preference preference) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
            View v = getLayoutInflater().inflate(R.layout.dialog_licenses, null);
            TextView tv = (TextView)v.findViewById(R.id.licenses, TextView.class);
            tv.setText("IRCCloud\n" +
                    "Copyright (C) 2013 IRCCloud, Ltd.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.\n" +
                    "\n" +
                    "You should have received a copy of the GNU General Public License\n" +
                    "along with this program.  If not, see &lt;http://www.gnu.org/licenses/&gt;.\n" +
                    "\n\n" +
                    "HybiParser.java: draft-ietf-hybi-thewebsocketprotocol-13 parser\n" +
                    "\n" +
                    "Based on code from the faye project.\n" +
                    "https://github.com/faye/faye-websocket-node\n" +
                    "Copyright (c) 2009-2012 James Coglan\n" +
                    "\n" +
                    "Ported from Javascript to Java by Eric Butler <eric@codebutler.com>\n" +
                    "\n" +
                    "(The MIT License)\n" +
                    "\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining\n" +
                    "a copy of this software and associated documentation files (the\n" +
                    "\"Software\"), to deal in the Software without restriction, including\n" +
                    "without limitation the rights to use, copy, modify, merge, publish,\n" +
                    "distribute, sublicense, and/or sell copies of the Software, and to\n" +
                    "permit persons to whom the Software is furnished to do so, subject to\n" +
                    "the following conditions:\n" +
                    "\n" +
                    "The above copyright notice and this permission notice shall be\n" +
                    "included in all copies or substantial portions of the Software.\n" +
                    "\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
                    "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n" +
                    "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
                    "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE\n" +
                    "LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION\n" +
                    "OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION\n" +
                    "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" +
                    "\n\n" +
                    "Sony LiveWare library\n" +
                    "Copyright (c) 2011, Sony Ericsson Mobile Communications AB\n" +
                    "Copyright (C) 2012-2013 Sony Mobile Communications AB\n" +
                    "\n" +
                    "All rights reserved.\n" +
                    "\n" +
                    "Redistribution and use in source and binary forms, with or without\n" +
                    "modification, are permitted provided that the following conditions are met:\n" +
                    "\n" +
                    "* Redistributions of source code must retain the above copyright notice, this\n" +
                    "  list of conditions and the following disclaimer.\n" +
                    "\n" +
                    "* Redistributions in binary form must reproduce the above copyright notice,\n" +
                    "  this list of conditions and the following disclaimer in the documentation\n" +
                    "  and/or other materials provided with the distribution.\n" +
                    "\n" +
                    "* Neither the name of the Sony Ericsson Mobile Communications AB nor the names\n" +
                    "  of its contributors may be used to endorse or promote products derived from\n" +
                    "  this software without specific prior written permission.\n" +
                    "\n" +
                    "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND\n" +
                    "ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n" +
                    "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
                    "DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE\n" +
                    "FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL\n" +
                    "DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                    "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER\n" +
                    "CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
                    "OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n" +
                    "OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                    "\n\n"+
                    "AsyncTaskEx\n" +
                    "Copyright (c) 2008-2009 CommonsWare, LLC\n" +
                    "Portions (c) 2009 Google, Inc.\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\"); you may\n" +
                    "not use this file except in compliance with the License. You may obtain\n" +
                    "a copy of the License at\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.\n" +
                    "A subclass of the Android ListView component that enables drag\n" +
                    "and drop re-ordering of list items.\n" +
                    "\n\n" +
                    "DragSortListView\n" +
                    "Copyright 2012 Carl Bauer\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.");
            builder.setView(v);
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog d = builder.create();
            d.setOwnerActivity(PreferencesActivity.this);
            d.show();
            return false;
        }
    };

	Preference.OnPreferenceClickListener urlClick = new Preference.OnPreferenceClickListener() {

		public boolean onPreferenceClick(Preference preference) {
			Intent i = null;
			if (preference.getKey().equals("faq"))
				i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irccloud.com/faq"));
			if (preference.getKey().equals("feedback")) {
				ServersDataSource.Server s = ServersDataSource.getInstance().getServer("irc.irccloud.com");
				if(s != null && s.ssl > 0)
					i = new Intent(Intent.ACTION_VIEW, Uri.parse("ircs://irc.irccloud.com/%23feedback"));
				else
					i = new Intent(Intent.ACTION_VIEW, Uri.parse("irc://irc.irccloud.com/%23feedback"));
			} if (preference.getKey().equals("subscriptions"))
				i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irccloud.com/#?/upgrade"));
			if (preference.getKey().equals("changes"))
				i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irccloud.com/android-changelog.txt"));

			if (i != null)
				startActivity(i);
			return false;
		}
	};
}
