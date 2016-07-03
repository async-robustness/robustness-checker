package com.vlille.checker.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.vlille.checker.R;
import com.vlille.checker.db.StationEntityManager;
import com.vlille.checker.model.Station;
import com.vlille.checker.ui.HomeActivity;
import com.vlille.checker.ui.delegate.StationUpdateDelegate;
//import com.vlille.checker.ui.osm.MapState;
//import com.vlille.checker.ui.osm.MapView;


import java.util.List;
 
/**
 * A fragment to localize and bookmark stations from a map, using OpenStreetMap.
 */
public class MapFragment extends Fragment implements StationUpdateDelegate {

/*	private static final String TAG = MapFragment.class.getSimpleName();

    private StationEntityManager stationEntityManager;

	private MapState state = new MapState();
	private List<Station> stations;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.d(TAG, "onCreate");
    }

	
	@Override
    public View onCreateView(Bundle savedInstanceState,
                             LayoutInflater inflater, ViewGroup container) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreateView");

		final View view = new View(R.layout.maps, container, false);

		return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.stations = stationEntityManager.findAll();
        addLocationEnablerClickListener(getView());
    }



    private void addLocationEnablerClickListener(final View view) {
        final ImageButton locationEnabler = new ImageButton(R.id.maps_location_enable);
        locationEnabler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}*/

	@Override
	public void update(Station station) {
		//stationEntityManager.update(station);
	}

}
