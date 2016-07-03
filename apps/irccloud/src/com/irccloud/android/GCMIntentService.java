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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

// The asynchronous invocations by Timer need to be made synchronous to check for robustness
public class GCMIntentService extends IntentService {

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
	}

	public static void scheduleRegisterTimer(int delay) {
		final int retrydelay = delay;

		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if(!IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).contains("session_key")) {
					return;
				}
				boolean success = false;
                if(getRegistrationId(IRCCloudApplication.getInstance().getApplicationContext()).length() == 0) {
                    //try {
                    //    String oldRegId = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).getString("gcm_reg_id", "");
                    //    String regId = "";
                    //    int appVersion = getAppVersion();
                    //    Log.i("IRCCloud", "Saving regId on app version " + appVersion);
                    //    SharedPreferences.Editor editor = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).edit();
                    //    editor.putString("gcm_reg_id", regId);
                    //    editor.putInt("gcm_app_version", appVersion);
                    //   editor.remove("gcm_registered");
                    //    editor.commit();
                    //    if(oldRegId.length() > 0) {
                    //        Log.i("IRCCloud", "Unregistering old ID");
                    //        scheduleUnregisterTimer(1000, oldRegId);
                    //    }
                    //} catch (IOException ex) {
                    //    Log.w("IRCCloud", "Failed to register device ID, will retry in " + ((retrydelay*2)/1000) + " seconds");
                    //    scheduleRegisterTimer(retrydelay * 2);
                    //    return;
                    //}
                }
                if(IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).contains("gcm_registered")) {
                    Log.d("IRCCloud", "GCM ID already sent to IRCCloud");
                    return;
                }
                Log.i("IRCCloud", "Sending GCM ID to IRCCloud");
				//try {
				//	JSONObject result = NetworkConnection.getInstance().registerGCM(getRegistrationId(IRCCloudApplication.getInstance().getApplicationContext()), IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).getString("session_key", ""));
				//	if(result.has("success"))
				//		success = result.getBoolean("success");
				//} catch (Exception e) {
				//	e.printStackTrace();
				//}
				if(success) {
					SharedPreferences.Editor editor = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).edit();
					editor.putBoolean("gcm_registered", true);
					editor.commit();
                    Log.d("IRCCloud", "Device successfully registered");
				} else {
					Log.w("IRCCloud", "Failed to register device ID, will retry in " + ((retrydelay*2)/1000) + " seconds");
					scheduleRegisterTimer(retrydelay * 2);
				}
			}
			
		}, delay);*/
	}
	
	public static void scheduleUnregisterTimer(int delay, final String regId) {
		final int retrydelay = delay;
		
		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				boolean success = false;
				//try {
                //    String session = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).getString(regId, "");
                //    if (session.length() == 0)
                //        session = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).getString("session_key", "");
				//	//JSONObject result = NetworkConnection.getInstance().unregisterGCM(regId, session);
                //    //if(result.has("success"))
                //    //	success = result.getBoolean("success");
               // } catch (SkipException e) {
                //    throw e; // not to block SkipExceptions
                //} catch (Exception e) {
				//	// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				if(success) {
                    //try {
                    //    GoogleCloudMessaging.getInstance(IRCCloudApplication.getInstance().getApplicationContext()).unregister();
                    //} catch (IOException e) {
                    //    Log.w("IRCCloud", "Failed to unregister device ID, will retry in " + ((retrydelay*2)/1000) + " seconds");
                    //    scheduleUnregisterTimer(retrydelay * 2, regId);
                    //    return;
                    //}
                    SharedPreferences.Editor editor = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).edit();
					editor.remove("gcm_registered");
                    editor.remove("gcm_reg_id");
                    editor.remove("gcm_app_version");
					editor.remove(regId);
					editor.commit();
				} else {
					Log.w("IRCCloud", "Failed to unregister device ID, will retry in " + ((retrydelay*2)/1000) + " seconds");
					scheduleUnregisterTimer(retrydelay * 2, regId);
				}
			}
		}, delay);*/
	}

    private static int getAppVersion() {
        try {
            Context context = IRCCloudApplication.getInstance().getApplicationContext();
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("prefs", 0);
        String registrationId = prefs.getString("gcm_reg_id", "");
        if(registrationId.length() == 0) {
            Log.i("IRCCloud", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt("gcm_app_version", Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i("IRCCloud", "App version changed.");
            return "";
        }
        return registrationId;
    }
}
