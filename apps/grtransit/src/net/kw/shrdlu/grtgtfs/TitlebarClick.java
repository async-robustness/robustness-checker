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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/*
 *  Called when a button is clicked on the title bar.
 */
public class TitlebarClick {
	public static void onTitlebarClick(Activity context, View v) {
		switch (v.getId()) {
		case R.id.titlelogo: {
			final Intent home = new Intent(context, FavstopsActivity.class);
			home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(home);
			context.finish(); // This clears the stack and returns to the home screen.
			return;
		}
		case R.id.button_search: {
			context.startActivity(new Intent(context, SearchActivity.class));
			return;
		}
		case R.id.button_overflow: {
			context.openOptionsMenu();
			return;
		}
		}
	}

	public static boolean onOptionsItemSelected(Activity context, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			final Intent home = new Intent(context, FavstopsActivity.class);
			home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(home);
			context.finish(); // This clears the stack and returns to the home screen.
			return true;
		}
		case R.id.menu_search: {
			context.startActivity(new Intent(context, SearchActivity.class));
			return true;
		}
		case R.id.menu_refresh: {
			context.finish();
			context.startActivity(context.getIntent());
			return true;
		}
		case R.id.menu_alerts: {
			context.startActivity(new Intent(context, RiderAlertsActivity.class));
			return true;
		}
		case R.id.menu_showmap: {
			context.startActivity(new Intent(context, StopsActivity.class));
			return true;
		}
		case R.id.menu_preferences: {
			final Intent prefs = new Intent(context, PrefsActivity.class);
			context.startActivity(prefs);
			return true;
		}
		case R.id.menu_closeststops: {
			final Intent stops = new Intent(context, ClosestStopsActivity.class);
			context.startActivity(stops);
			return true;
		}
		case R.id.menu_about: {

			// Show the about screen
			String versionName = "unknown";
			int versionCode = 0;
			final int dbVersion = DatabaseHelper.GetDBVersion();

			try {
				final String pn = context.getPackageName();
				final PackageManager pm = context.getPackageManager();
				final PackageInfo pi = pm.getPackageInfo(pn, 0);
				versionCode = pi.versionCode;
				versionName = pi.versionName;
			} catch (final NameNotFoundException e) {
				e.printStackTrace();
			}

			if (GRTApplication.isDebugBuild) {
				versionName += "dbg";
			}

			final View messageView = context.getLayoutInflater().inflate(R.layout.about, null, false);
			final String credits = context.getString(R.string.app_credits, versionName, versionCode, dbVersion);
			final TextView cv = (TextView) messageView.findViewById(R.id.about_credits, TextView.class);
			cv.setText(credits);

			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.drawable.grticon);
			builder.setTitle(R.string.app_name);
			builder.setView(messageView);

			// Only do this on newer code -- reqd if using maps v2.
			// try {
			// // Maps API requires us to provide attribution.
			// TextView legal = (TextView) messageView.findViewById(R.id.about_legalese);
			// if (legal != null && GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
			// String text = new String(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(context));
			// legal.setText(text);
			// }
			// } catch (Exception e) {
			// Log.w(TAG, "Problems contacting Google Play services");
			// }

			builder.create();
			builder.show();
			return true;
		}
		}
		return false;
	}
}
