package com.vlille.checker.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.vlille.checker.R;
import com.vlille.checker.model.Station;
import com.vlille.checker.utils.StationUtils;
import com.vlille.checker.utils.ToastUtils;

import rjava.util.ArrayList;
import java.util.List;

/**
 * A fragment to bookmark stations.
 */
public class AllStationsFragment extends StationsListFragment {

    private static final String TAG = AllStationsFragment.class.getName();

    /**
     * The original overall stations list, used for the filter by name.
     */
	private List<Station> originalStations;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSearchFieldListeners();
	}

    @Override
    public void onPause() {
        super.onPause();
        hideInputMethodManager();
    }

    @Override
    void loadStations() {
        originalStations = stationEntityManager.findAll();
        setStations(new ArrayList<Station>(originalStations));
    }

    @Override
	public void initListAdapter() {
        /*
         * setListAdapter(null) is a a hack to avoid java.lang.IllegalStateException: Cannot add header view to list -- setListAdapter has already been called.
	     * @see <a href="http://stackoverflow.com/questions/5704478/best-place-to-addheaderview-in-listfragment">Add heaver view in list fragment</a>
         */
        setListAdapter(null);

        final ListView listView = getListView();

        setListAdapter();
	}

	private void initSearchFieldListeners() {
		initSearchTextListener();
		initClearTextListener();
	}

	private void initSearchTextListener() {
		final EditText searchField = getSearchField();
		searchField.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKeyListener " + event);
                if (hasPressedOk(keyCode, event)) {
                    hideInputMethodManager();

                    final String keyword = searchField.getText().toString();
                    filterStationsByKeyword(keyword);
                }

                return false;
            }

            private boolean hasPressedOk(int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });
	}

    /**
     * Hides the input text field when searched for some station.
     */
	private void hideInputMethodManager() {
		/*InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getSearchField().getWindowToken(), 0);*/
	}

	private void initClearTextListener() {
		final ImageButton clearButton = new ImageButton(R.id.list_search_field_clear);
		clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clear search editText");
                EditText editText = getSearchField();
                String oldText = editText.getText().toString();
                editText.setText(null);

                // Only reload stations if there was something in the input.
                if (!TextUtils.isEmpty(oldText)) {
                    filterStationsByKeyword(null);
                }
            }
        });
	}

	private EditText getSearchField() {
		final EditText searchField = new EditText(R.id.list_search_field);

		return searchField;
	}

	private void filterStationsByKeyword(final String keyword) {
		Log.d(TAG, "Text searched: " + keyword);

		final List<Station> filteredStations = StationUtils.filter(originalStations, keyword);
		if (filteredStations.isEmpty()) {
			ToastUtils.show(getActivity(), R.string.search_no_result);
		} else {
            setStations(filteredStations);
		}

        setListAdapter();
        updateVisibleItemsAsRunnable();
    }

    @Override
    boolean isReadOnly() {
        return true;
    }

}
