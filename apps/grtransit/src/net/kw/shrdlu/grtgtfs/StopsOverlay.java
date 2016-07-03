/*
 * Copyright 2011 Giles Malet.
 *
 * This file is part of GRTransit.
 * 
 * GRTransit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as ed by
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

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class StopsOverlay {
	private static final String TAG = "GrtItemizedOverlay";

	private Context mContext = null;
	private String mStopid;

	private class stopDetail {
		public final String num, name;

		public stopDetail(String n, String a) {
			num = n;
			name = a;
		}
	}

	private ArrayList<stopDetail> mStops = new ArrayList<stopDetail>(3000);
	private static ArrayList<stopDetail> mCachedStops = null;

	// Used to limit which stops are displayed
	public StopsOverlay(Context context) {
		mContext = context;
	}

	// This is time consuming, and should not be called on the GUI thread
	public void LoadDB(String whereclause, String[] selectargs, NotificationCallback task) {
		// Log.d(TAG, "starting LoadDB");

		final String table = "stops";
		final String[] columns = { "stop_lat", "stop_lon", "stop_id", "stop_name" };

		if (whereclause == null && mCachedStops != null) {
			// Log.d(TAG, "using cached values");
			mStops = mCachedStops;

		} else {

			// Log.d(TAG, "no cached values");

			Cursor csr;
			try {
				csr = DatabaseHelper.ReadableDB().query(true, table, columns, whereclause, selectargs, null, null, null, null);
			} catch (final SQLException e) {
				Log.e(TAG, "DB query failed: " + e.getMessage());
				return;
			}
			final int maxcount = csr.getCount();
			int progresscount = 0;


			boolean more = csr.moveToPosition(0);
			while (more) {
				more = csr.moveToNext();
			}
			csr.close();

		}

		if (whereclause == null && mCachedStops == null) {
			// Log.d(TAG, "priming cache");
			mCachedStops = mStops;
		}

		// Log.d(TAG, "exiting LoadDB");
	}

	/*private int findClosestStop(MapView view, int screenX, int screenY) {
		final int DIST_THRESHOLD = 512;
		int closestpt = -1;

		for (int i = 0; i < mStops.size(); i++) {
			final stopDetail stop = mStops.get(i);
		}

		return closestpt;
	}

	// This must be called on the GIU thread
	private MapView mView; // TODO this is messy

	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		mView = mapView; // TODO this is messy
		return false;
	}*/


	// This is called when a bus stop is clicked on in the map.
	protected boolean onScreenTap(int index, boolean longpress) {
		// Log.d(TAG, "OnTap(" + index + ")");
		final stopDetail stop = mStops.get(index);
		mStopid = stop.num;
		final String stopname = stop.name;

		if (longpress == true) {

			final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					switch (id) {
					case DialogInterface.BUTTON_POSITIVE:
						GRTApplication.mPreferences.AddBusstopFavourite(mStopid, stopname);
						break;
						// case DialogInterface.BUTTON_NEGATIVE:
						// // nothing
						// break;
					}
					dialog.cancel();
				}
			};

			final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Stop " + mStopid + ", " + stopname).setMessage(R.string.favs_add_to_list)
			.setPositiveButton(R.string.yes, listener).setNegativeButton(R.string.no, listener).create().show();
		} else {
			// Show route select activity
			final Intent routeselect = new Intent(mContext, RouteselectActivity.class);
			final String pkgstr = mContext.getApplicationContext().getPackageName();
			routeselect.putExtra(pkgstr + ".stop_id", mStopid);
			routeselect.putExtra(pkgstr + ".stop_name", stopname);
			mContext.startActivity(routeselect);
		}
		return true;
	}


	public int size() {
		return 0;
	}

	/* Override this so we can add text labels to each pin.
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean) */
	private class cachedStop {
		public final String num, name;

		public cachedStop(String n, String a) {
			num = n;
			name = a;
		}
	}

}
