/*
 * Copyright 2011 Giles Malet.
 *
 * This file is part of GRTransit.
 * 
 * GRTransit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GRTransit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GRTransit.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.kw.shrdlu.grtgtfs;

import android.content.Context;
import android.os.Build;


public class GRTApplication extends android.app.Application {
	public static final String TAG = "GRTApplication";

	public static Preferences mPreferences = null;
	public static DatabaseHelper dbHelper = null;
	public static boolean isDebugBuild = false;

	private Context mContext;

	// Define the debug signature hash (Android default debug cert). Code from sigs[i].hashCode()
	protected final static int DEBUG_SIGNATURE_HASH = -1270195494;

	// Used by tracker.
	private static final int TRACKER_VISITOR_SCOPE = 1;
	// private static final int TRACKER_SESSION_SCOPE = 2;
	// private static final int TRACKER_PAGE_SCOPE = 3;
	private static final int TRACKER_UUID = 1;
	private static final int TRACKER_VERSION = 2;

	// private static final int TRACKER_CV_SCREEN_ORIENTATION_SLOT = 3;

	@Override
	public void onCreate() {
		super.onCreate();
		// Log.d(TAG, "onCreate");

		mContext = this; // also returned by getApplicationContext();

		isDebugBuild = CheckDebugBuild();

		// Do this before instantiating Globals, as that may do something we'd like
		// to see by having StrictMode on already.
		if (isDebugBuild && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD /* 9 */) {
			APIReflectionWrapper.API9.setStrictMode();
		}

		mPreferences = new Preferences(mContext);
		dbHelper = new DatabaseHelper(mContext);

		// Google Analytics tracker code removed
	}

	// Checks if this apk was built using the debug certificate
	// See http://stackoverflow.com/questions/3029819/android-automatically-choose-debug-release-maps-api-key/3828864#3828864
	private static boolean checkedBuild = false;

	private boolean CheckDebugBuild() {
		// android.content.pm.signature code is removed
		return false;
	}
}
