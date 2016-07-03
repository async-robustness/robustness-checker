package com.vlille.checker.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlille.checker.R;
import com.vlille.checker.model.Station;
import com.vlille.checker.ui.delegate.StationUpdateDelegate;

import com.vlille.checker.utils.MapsIntentChooser;
import com.vlille.checker.utils.TextPlural;
import com.vlille.checker.utils.ViewUtils;


import java.util.List;
/**
 * A generic adapter for a stations ListView.
 */
public class StationsAdapter extends ArrayAdapter<Station> {

	private  static final String TAG = StationsAdapter.class.getSimpleName();

	private Activity activity;
    private StationUpdateDelegate stationUpdateDelegate;
    private List<Station> stations;
    private Resources resources;
    private boolean readOnly = false;

	public StationsAdapter(Context context, int resource, List<Station> stations) {
		super(context, resource, stations);

		this.activity = (Activity) context;
		this.stations = stations;
		this.resources = context.getResources();
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {

        handleStationDetails(view, position);

		return view;
	}

	/**
	 * Handle stations details.
	 */
	private void handleStationDetails(View view, final int position) {
        final Station station = stations.get(position);

        handleStarCheckbox(view, position, station);
        handleStationsTextInfos(view, station);
        handleToMapButton(view, station);
        handleToNavigationpButton(view, station);
	}

    private void handleToNavigationpButton(View view, final Station station) {
        View anonView = new View();
        anonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsIntentChooser.chooseIntent(activity, station);
            }
        });

    }

    private void handleStarCheckbox(View view, final int position, final Station station) {
        final CheckBox checkbox = (CheckBox) view.findViewById(R.id.detail_starred, CheckBox.class);
        checkbox.setChecked(station.isStarred());

        final ArrayAdapter<Station> arrayAdapter = this;
        checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                station.setStarred(checkbox.isChecked());
                if (stationUpdateDelegate != null) {
                    stationUpdateDelegate.update(station);
                }

                Log.i("Checkbox B", "Clicked position:" + position + " station: " + station.name);
                //System.out.println("Clicked star at checkbox  position:" + position + " station: " + station.name);

                if (position < stations.size()) {
                    stations.remove(position);

                    //System.out.println("Removed  position:" + position + " station: " + station.name + "  checkbox: " + checkbox.hashCode());
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void handleStationsTextInfos(View view, Station station) {
        TextView name = new TextView(R.id.station_name);
        name.setText(station.getName());

        String timeUnitSecond = TextPlural.toPlural(
                station.getLastUpdate(),
                resources.getString(R.string.timeunit_second));
        TextView lastUpdate = new TextView(R.id.station_lastupdate);
        lastUpdate.setText(resources.getString(R.string.update_ago, station.getLastUpdate(), timeUnitSecond));

        TextView address = new TextView(R.id.station_adress);
        address.setText(station.getAdressToUpperCase());

        TextView nbBikes = new TextView(R.id.details_bikes);
        nbBikes.setText(station.getBikesAsString());

        TextView nbAttachs = new TextView(R.id.details_attachs);
        nbAttachs.setText(station.getAttachsAsString());

        ImageView ccPaymentAllowed = new ImageView(R.id.details_cb);
        ViewUtils.switchView(ccPaymentAllowed, station.isCbPaiement());
    }

    private void handleToMapButton(View view, final Station station) {
        /*ImageButton buttonToMap = (ImageButton) view.findViewById(R.id.station_action_tomap);
        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GeoPoint geoPoint = station.getGeoPoint();

                // Select the map tab and resets the tabListener to focus on selected station geoPoint.
                ActionBarActivity actionBarActivity = (ActionBarActivity) activity;
                MapTabListener mapTabListener = new MapTabListener(actionBarActivity, geoPoint);

                ActionBar.Tab mapTab = actionBarActivity.getSupportActionBar().getTabAt(2);
                mapTab.setTabListener(mapTabListener);
                mapTab.select();
            }
        });*/
    }

    private int getColor(int number) {
		return 0; //resources.getColor(ColorSelector.getColor(number));
	}

	@Override
	public void notifyDataSetChanged() {
		Log.d(TAG, "Dataset has changed!");

		super.notifyDataSetChanged();
	}

    public void setStationUpdateDelegate(StationUpdateDelegate stationUpdateDelegate) {
        this.stationUpdateDelegate = stationUpdateDelegate;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
