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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MenuMapActivity extends Activity {
	private static final String TAG = "MenuMapActivity";

	protected Activity mContext;
	protected View mDetailArea;
	protected TextView mTitle;
	protected ProgressBar mProgress;
	protected StopsOverlay mStopsOverlay = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Make sure we get the correct API key to match the build key.
		if (GRTApplication.isDebugBuild == true) {
			setContentView(R.layout.mapview_debug);
		} else {
			setContentView(R.layout.mapview);
		}

		// Load animations used to show/hide progress bar
		mProgress = (ProgressBar) findViewById(R.id.progress, ProgressBar.class);
		mDetailArea = findViewById(R.id.mapview, View.class);

		mTitle = (TextView) findViewById(R.id.listtitle, TextView.class);
		mTitle.setText(R.string.loading_stops);

		mStopsOverlay = new StopsOverlay(mContext);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB /* 11 */) {
			APIReflectionWrapper.API11.prepActionBar(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// We want to track a pageView every time this activity gets the focus - but if the activity was
		// previously destroyed we could have lost our global data, so this is a bit of a hack to avoid a crash!
		/*if (GRTApplication.tracker == null) {
			Log.e(TAG, "null tracker!");
			startActivity(new Intent(this, FavstopsActivity.class));
		} else {
			GRTApplication.tracker.trackPageView("/" + this.getLocalClassName());
		}*/

		//B location code removed
	}

	@Override
	public void onPause() {
		super.onPause();
		// Log.d(TAG, "onPause()");
		//B location code removed
	}

	// Called when a button is clicked on the title bar
	public void onTitlebarClick(View v) {
		TitlebarClick.onTitlebarClick(this, v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.busstopsmenu, menu);

		// Twiddle menu options
		menu.removeItem(R.id.menu_about);
		menu.removeItem(R.id.menu_preferences);
		menu.findItem(R.id.menu_showmap).setTitle(R.string.mylocation);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB /* 11 */) {
			// Remove search from the menu, as we put it on the title bar.
			menu.removeItem(R.id.menu_search);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_showmap: {

			return true;
		}
		default: {
			return TitlebarClick.onOptionsItemSelected(mContext, item);
		}
		}
	}

}
