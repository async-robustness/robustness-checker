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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import checker.SkipException;
import com.irccloud.android.BuildConfig;
import com.irccloud.android.IRCCloudJSONObject;
import com.irccloud.android.NetworkConnection;
import com.irccloud.android.R;
import com.irccloud.android.data.ServersDataSource;

public class MainActivity extends FragmentActivity {
	private View login = null;
	private AutoCompleteTextView email;
	private EditText password;
	private Button loginBtn;
	private NetworkConnection conn;
	private TextView errorMsg = null;
	private TextView connectingMsg = null;
	private ProgressBar progressBar = null;
	private Timer countdownTimer = null;
	private String error = null;
	private View connecting = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        connecting = findViewById(R.id.connecting, View.class);
		errorMsg = (TextView)findViewById(R.id.errorMsg, TextView.class);
		connectingMsg = (TextView)findViewById(R.id.connectingMsg, TextView.class);
		progressBar = (ProgressBar)findViewById(R.id.connectingProgress, ProgressBar.class);

        login = findViewById(R.id.login, View.class);
        email = (AutoCompleteTextView)findViewById(R.id.email, AutoCompleteTextView.class);
        ArrayList<String> accounts = new ArrayList<String>();
        AccountManager am = (AccountManager)getSystemService(Context.ACCOUNT_SERVICE);
        for(Account a : am.getAccounts()) {
        	if(a.name.contains("@") && !accounts.contains(a.name))
        		accounts.add(a.name);
        }
        if(accounts.size() > 0)
        	email.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accounts.toArray(new String[accounts.size()])));

        if(savedInstanceState != null && savedInstanceState.containsKey("email"))
        	email.setText(savedInstanceState.getString("email"));
        
        password = (EditText)findViewById(R.id.password, EditText.class);
        password.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					new LoginTask().execute((Void)null);
					return true;
				}
				return false;
			}
        });
        if(savedInstanceState != null && savedInstanceState.containsKey("password"))
        	password.setText(savedInstanceState.getString("password"));

        loginBtn = (Button)findViewById(R.id.loginBtn, Button.class);
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new LoginTask().execute((Void)null);
			}
        });
        loginBtn.setFocusable(true);
    	loginBtn.requestFocus();
        
        TextView version = (TextView)findViewById(R.id.version, TextView.class);
        try {
            if(BuildConfig.BRAND_NAME.length() > 0)
    			version.setText("Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "-" + BuildConfig.BRAND_NAME);
            else
                version.setText("Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			version.setVisibility(View.GONE);
		}
    }
    
    @Override
    public void onSaveInstanceState(Bundle state) {
    	super.onSaveInstanceState(state);
    	if(login != null && login.getVisibility() == View.VISIBLE) {
	    	if(email != null)
	    		state.putString("email", email.getText().toString());
	    	if(password != null)
	    		state.putString("password", password.getText().toString());
    	}
    }

    @Override
    public void onPause() {
    	super.onPause();
    	if(conn != null)
    		conn.removeHandler(mHandler);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	conn = NetworkConnection.getInstance();
    	if(conn.ready) {
            if(ServersDataSource.getInstance().count() > 0) {
                Intent i = new Intent(MainActivity.this, MessageActivity.class);
                if(getIntent() != null) {
                    if(getIntent().getData() != null)
                        i.setData(getIntent().getData());
                    if(getIntent().getExtras() != null)
                        i.putExtras(getIntent().getExtras());
                }
                startActivity(i);
            } else {
                startActivity(new Intent(MainActivity.this, EditConnectionActivity.class));
            }
    		finish();
    	} else {
    		conn.addHandler(mHandler);
    		if(getSharedPreferences("prefs", 0).contains("session_key")) {
    			connecting.setVisibility(View.VISIBLE);
    			login.setVisibility(View.GONE);
    			conn.connect(getSharedPreferences("prefs", 0).getString("session_key", ""));
    			updateReconnecting();
    		} else {
    			connecting.setVisibility(View.GONE);
    			login.setVisibility(View.VISIBLE);
                checkPlayServices();
    		}
    	}
    }
    
	private void updateReconnecting() {
		if(conn.getState() == NetworkConnection.STATE_CONNECTED) {
			connectingMsg.setText("Loading");
		} else if(conn.getState() == NetworkConnection.STATE_CONNECTING || conn.getReconnectTimestamp() > 0) {
			progressBar.setIndeterminate(true);
			if(conn.getState() == NetworkConnection.STATE_DISCONNECTED && conn.getReconnectTimestamp() > 0) {
	    		String plural = "";
	    		int seconds = (int)((conn.getReconnectTimestamp() - System.currentTimeMillis()) / 1000);
	    		if(seconds != 1)
	    			plural = "s";
	    		if(seconds < 1) {
	    			connectingMsg.setText("Connecting");
					errorMsg.setVisibility(View.GONE);
	    		} else if(seconds > 10 && error != null) {
	    			connectingMsg.setText("Reconnecting in " + seconds + " second" + plural);
					errorMsg.setText(error);
					errorMsg.setVisibility(View.VISIBLE);
	    		} else {
					connectingMsg.setText("Reconnecting in " + seconds + " second" + plural);
					errorMsg.setVisibility(View.GONE);
					error = null;
	    		}
	    		try {
					if(countdownTimer != null)
						countdownTimer.cancel();
	    		} catch (NullPointerException e) {
	    		}
				/*countdownTimer = new Timer(); // remove calls to Timer
				countdownTimer.schedule(
						new TimerTask() {
							public void run() {
								if (conn.getState() == NetworkConnection.STATE_DISCONNECTED) {
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											updateReconnecting();
										}
									});
								}
								countdownTimer = null;
							}
							}, 1000);*/
			} else {
				connectingMsg.setText("Connecting");
				error = null;
				errorMsg.setVisibility(View.GONE);
			}
    	} else {
			connectingMsg.setText("Offline");
			progressBar.setIndeterminate(false);
			progressBar.setProgress(0);
    	}
    }
    
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
            case NetworkConnection.EVENT_DEBUG:
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(msg.obj.toString());
                break;
			case NetworkConnection.EVENT_PROGRESS:
				float progress = (Float)msg.obj;
                if(progressBar.getProgress() < progress) {
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress((int)progress);
                }
				break;
            case NetworkConnection.EVENT_BACKLOG_START:
                progressBar.setProgress(0);
                break;
			case NetworkConnection.EVENT_BACKLOG_END:
				if(conn.ready) {
                    if(ServersDataSource.getInstance().count() > 0) {
                        Intent i = new Intent(MainActivity.this, MessageActivity.class);
                        if(getIntent() != null) {
                            if(getIntent().getData() != null)
                                i.setData(getIntent().getData());
                            if(getIntent().getExtras() != null)
                                i.putExtras(getIntent().getExtras());
                        }
                        startActivity(i);
                    } else {
                        startActivity(new Intent(MainActivity.this, EditConnectionActivity.class));
                    }
                    finish();
				}
				break;
			case NetworkConnection.EVENT_CONNECTIVITY:
				updateReconnecting();
				break;
			case NetworkConnection.EVENT_FAILURE_MSG:
				IRCCloudJSONObject o = (IRCCloudJSONObject)msg.obj;
				try {
					error = o.getString("message");
                    if(error.equals("auth")) {
                        conn.logout();
                        connecting.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                        return;
                    }

                    if(error.equals("set_shard")) {
                        conn.disconnect();
                        conn.ready = false;
                        SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
                        editor.putString("session_key", o.getString("cookie"));
                        conn.connect(o.getString("cookie"));
                        editor.commit();
                        return;
                    }

					if(error.equals("temp_unavailable"))
						error = "Your account is temporarily unavailable";
					updateReconnecting();
				} catch (SkipException e) {
					throw e; // not to block SkipExceptions
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	};
    
    private class LoginTask extends AsyncTask<Void, Void, Object> {
		@Override
		public void onPreExecute() {
			email.setEnabled(false);
			password.setEnabled(false);
			loginBtn.setEnabled(false);
			connectingMsg.setText("Signing in");
			progressBar.setIndeterminate(true);
			connecting.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Object doInBackground(Void... arg0) {
            if(email.getText() != null && email.getText().length() > 0 && password.getText() != null && password.getText().length() > 0)
    			return NetworkConnection.getInstance().login(email.getText().toString(), password.getText().toString());
            else
                return null;
		}

		@Override
		public void onPostExecute(Object result) {
			/*if(result != null && result.has("session")) {
				try {
					SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
					editor.putString("session_key", result.getString("session"));
					login.setVisibility(View.GONE);

					conn.addHandler(mHandler);
					conn.connect(result.getString("session"));
					editor.commit();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {*/
				email.setEnabled(true);
				password.setEnabled(true);
				loginBtn.setEnabled(true);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Login Failed");
				String message = "Unable to connect to IRCCloud.  Please try again later.";
				if(result != null) {
					/*try {
                        if(result.has("message")) {
                            message = result.getString("message");
                            if(message.equalsIgnoreCase("auth") || message.equalsIgnoreCase("email") || message.equalsIgnoreCase("password") || message.equalsIgnoreCase("legacy_account"))
                                message = "Incorrect username or password.  Please try again.";
                            else
                                message = "Error: " + message;
                        } else if(result.has("exception")) {
                            message += "\n\n" + result.getString("exception");
                        }
					} catch (JSONException e) {
						e.printStackTrace();
					}*/
				} else if(email.getText() == null || email.getText().length() == 0 || password.getText() == null || password.getText().length() == 0) {
                    message = "Please enter your username and password.";
                }
				builder.setMessage(message);
				builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.setOwnerActivity(MainActivity.this);
				try {
					dialog.show();
				} catch (WindowManager.BadTokenException e) {
				}
			//}
		}
    }

    private boolean checkPlayServices() {
        // removed Google Play Service code
        return true;
    }
}
