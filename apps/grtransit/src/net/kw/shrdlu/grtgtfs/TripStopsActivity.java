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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class TripStopsActivity extends MenuListActivity {
	private static final String TAG = "TripStopsActivity";

	private String mTrip_id, mRoute_id = null, mHeadsign, mStop_id;
	private Cursor mCsr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setContentView(R.layout.timeslayout);
		super.onCreate(savedInstanceState);

		final String pkgstr = mContext.getApplicationContext().getPackageName();
		final Intent intent = getIntent();
		mTrip_id = intent.getStringExtra(pkgstr + ".trip_id");
		mStop_id = intent.getStringExtra(pkgstr + ".stop_id");
		mRoute_id = intent.getStringExtra(pkgstr + ".route_id");
		mHeadsign = intent.getStringExtra(pkgstr + ".headsign");

		// register to get long clicks on bus stop list
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemLongClick(parent, view, position, id);
				return true; // we consumed the click
			}
		});

		final ListView lv = getListView();
		final TextView tv = new TextView(mContext);
		tv.setText(R.string.longpress_adds_stop);
		lv.addFooterView(tv);

		new ProcessBusStops().execute();
	}

	/* Do the processing to load the ArrayAdapter for display. */
	private class ProcessBusStops extends AsyncTask<Void, Integer, Integer> {
		// static final String TAG = "ProcessBusStops";

		@Override
		protected void onPreExecute() {
			// Log.v(TAG, "onPreExecute()");
			mProgress.setVisibility(View.VISIBLE);
		}

		// Update the progress bar.
		@Override
		protected void onProgressUpdate(Integer... parms) {
			mProgress.setProgress(parms[0]);
		}

		@Override
		protected Integer doInBackground(Void... foo) {
			// Log.v(TAG, "doInBackground()");

			final String qry = "select distinct stop_times.stop_id as _id, stop_name as descr, departure_time from stop_times"
					+ " join stops on stops.stop_id = stop_times.stop_id where trip_id = ? order by departure_time";
			final String[] selectargs = new String[] { mTrip_id };
			mCsr = DatabaseHelper.ReadableDB().rawQuery(qry, selectargs);
			startManagingCursor(mCsr);

			// Find where the current stop is, to position list
			final int maxcount = mCsr.getCount();
			int savedpos = -1;
			int progresscount = 0;
			boolean more = mCsr.moveToPosition(0);
			while (more) {
				if (mCsr.getString(0).equals(mStop_id)) {
					savedpos = progresscount;
					break;
				}
				more = mCsr.moveToNext();
				publishProgress(((int) ((++progresscount / (float) maxcount) * 100)));
			}

			return savedpos;
		}

		@Override
		protected void onPostExecute(Integer savedpos) {
			// Log.v(TAG, "onPostExecute()");

			mProgress.setVisibility(View.INVISIBLE);

			// TODO should be route_short_name?
			mTitle.setText("Stops on route " + mRoute_id + ": " + mHeadsign);

			final ListCursorAdapter adapter = new ListCursorAdapter(mContext, R.layout.timestopdesc, mCsr);
			mContext.setListAdapter(adapter);

			getListView().setSelectionFromTop(savedpos, 50); // position stop just below top
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Log.v(TAG, "clicked position " + position);

		final Cursor csr = (Cursor) l.getItemAtPosition(position, Cursor.class);
		if (csr == null) {
			return;
		}
		final String stop_id = csr.getString(0);
		final String stop_name = csr.getString(1);

		final Intent routes = new Intent(mContext, RouteselectActivity.class);
		final String pkgstr = mContext.getApplicationContext().getPackageName();
		routes.putExtra(pkgstr + ".stop_id", stop_id);
		routes.putExtra(pkgstr + ".stop_name", stop_name);
		mContext.startActivity(routes);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_showmap: {
			// Perform action on click
			final String pkgstr = mContext.getApplicationContext().getPackageName();
			final Intent busroutes = new Intent(mContext, RouteActivity.class);
			busroutes.putExtra(pkgstr + ".route_id", mRoute_id);
			busroutes.putExtra(pkgstr + ".headsign", mHeadsign);
			busroutes.putExtra(pkgstr + ".stop_id", mStop_id);
			startActivity(busroutes);
			return true;
		}
		default: {
			return TitlebarClick.onOptionsItemSelected(mContext, item);
		}
		}
	}


	// Called from the listener above for a long click
	public void onListItemLongClick(AdapterView<?> parent, View v, int position, long id) {
		// Log.v(TAG, "long clicked position " + position);

		final Cursor csr = (Cursor) parent.getItemAtPosition(position, Cursor.class);
		if (csr == null) {
			return;
		}
		final String stop_id = csr.getString(0);
		final String stop_name = csr.getString(1);

		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				switch (id) {
				case DialogInterface.BUTTON_POSITIVE:
					GRTApplication.mPreferences.AddBusstopFavourite(stop_id, stop_name);
					// mContext.startActivity(new Intent(mContext, FavstopsActivity.class));
					break;
					// case DialogInterface.BUTTON_NEGATIVE:
					// // nothing
					// break;
				}
				dialog.cancel();
			}
		};

		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Stop " + stop_id + ", " + stop_name);
		builder.setMessage(R.string.favs_add_to_list).setPositiveButton(R.string.yes, listener)
		.setNegativeButton(R.string.no, listener).create().show();
	}
}
