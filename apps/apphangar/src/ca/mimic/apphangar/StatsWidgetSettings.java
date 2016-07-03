/*
 * Copyright © 2014 Jeff Corcoran
 *
 * This file is part of Hangar.
 *
 * Hangar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hangar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hangar.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ca.mimic.apphangar;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import ca.mimic.apphangar.Settings.PrefsGet;

public class StatsWidgetSettings extends PreferenceActivity {

    final static int WIDGET_3X3_DEFAULT_APPSNO = 6;
    final static int WIDGET_3X3_DEFAULT_APPSNO_LS = 3;

    static PrefsGet prefs;
    static Context mContext;
    static Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new PrefsGet(getSharedPreferences("StatsWidget", Context.MODE_PRIVATE));

        mContext = getApplicationContext();
        extras = getIntent().getExtras();

        setTitle(R.string.stats_widget_name);
        setResult(RESULT_CANCELED);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new StatsWidgetFragment().newInstance()).commit();

        ListView v = getListView();
        v.setFooterDividersEnabled(false);

    }
    public static class StatsWidgetFragment extends PreferenceFragment {

        CheckBoxPreference divider_preference;
        CheckBoxPreference rounded_corners_preference;
        SwitchPreference apps_by_widget_size_preference;
        UpdatingListPreference appnos_preference;
        UpdatingListPreference appnos_ls_preference;
        UpdatingListPreference alignment_preference;

        public static StatsWidgetFragment newInstance() {
            return new StatsWidgetFragment();
        }

        public StatsWidgetFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);

            LinearLayout footer = (LinearLayout) inflater.inflate(R.layout.widget_footer, v, false);
            footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            Button createButton = (Button) footer.findViewById(R.id.placementButton, Button.class);
            if (extras == null) {
                createButton.setText(R.string.reconfigure_button_name);
            } else {
                createButton.setText(R.string.button_name);
            }
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mAppWidgetId;
                    savePrefs();
                    if (extras != null) {
                        mAppWidgetId = extras.getInt(
                                AppWidgetManager.EXTRA_APPWIDGET_ID,
                                AppWidgetManager.INVALID_APPWIDGET_ID);
                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        getActivity().setResult(RESULT_OK, resultValue);
                        Toast.makeText(mContext, R.string.reconfigure_reminder, Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    } else {
                        Tools.updateWidget(mContext);
                        getActivity().finish();
                    }
                }
            });

            v.addView(footer);
            return v;
        }
        public void savePrefs() {
            final SharedPreferences.Editor mEditor = prefs.editorGet();
            mEditor.putBoolean(Settings.DIVIDER_PREFERENCE, divider_preference.isChecked());
            mEditor.putBoolean(Settings.ROUNDED_CORNERS_PREFERENCE, rounded_corners_preference.isChecked());
            mEditor.putBoolean(Settings.APPS_BY_WIDGET_SIZE_PREFERENCE, apps_by_widget_size_preference.isChecked());
            mEditor.putString(Settings.STATS_WIDGET_APPSNO_PREFERENCE, appnos_preference.getValue());
            mEditor.putString(Settings.STATS_WIDGET_APPSNO_LS_PREFERENCE, appnos_ls_preference.getValue());
            mEditor.putInt(Settings.BACKGROUND_COLOR_PREFERENCE, 0);
            mEditor.putString(Settings.ALIGNMENT_PREFERENCE, alignment_preference.getValue());
            mEditor.apply();
        }
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.stats_widget_settings);
            int defaultAppsNo = WIDGET_3X3_DEFAULT_APPSNO;
            int defaultAppsNoLs = WIDGET_3X3_DEFAULT_APPSNO_LS;

            SharedPreferences mPrefs = prefs.prefsGet();

            divider_preference = (CheckBoxPreference)findPreference(Settings.DIVIDER_PREFERENCE, CheckBoxPreference.class);
            divider_preference.setChecked(mPrefs.getBoolean(Settings.DIVIDER_PREFERENCE, Settings.DIVIDER_DEFAULT));
            divider_preference.setOnPreferenceChangeListener(changeListener);

            rounded_corners_preference = (CheckBoxPreference)findPreference(Settings.ROUNDED_CORNERS_PREFERENCE, CheckBoxPreference.class);
            rounded_corners_preference.setChecked(mPrefs.getBoolean(Settings.ROUNDED_CORNERS_PREFERENCE, Settings.ROUNDED_CORNERS_DEFAULT));
            rounded_corners_preference.setOnPreferenceChangeListener(changeListener);

            apps_by_widget_size_preference = (SwitchPreference)findPreference(Settings.APPS_BY_WIDGET_SIZE_PREFERENCE, SwitchPreference.class);
            apps_by_widget_size_preference.setChecked(mPrefs.getBoolean(Settings.APPS_BY_WIDGET_SIZE_PREFERENCE, Settings.APPS_BY_WIDGET_SIZE_DEFAULT));
            apps_by_widget_size_preference.setOnPreferenceChangeListener(changeListener);

            appnos_preference = (UpdatingListPreference)findPreference(Settings.STATS_WIDGET_APPSNO_PREFERENCE, UpdatingListPreference.class);
            appnos_preference.setValue(mPrefs.getString(extras == null ? Settings.STATS_WIDGET_APPSNO_PREFERENCE : Integer.toString(defaultAppsNo),
                    Integer.toString(defaultAppsNo)));
            appnos_preference.setOnPreferenceChangeListener(changeListener);
            if (apps_by_widget_size_preference.isChecked()) {
                appnos_preference.setSummary(R.string.automatic_appsno);
            }

            appnos_ls_preference = (UpdatingListPreference)findPreference(Settings.STATS_WIDGET_APPSNO_LS_PREFERENCE, UpdatingListPreference.class);
            appnos_ls_preference.setValue(mPrefs.getString(extras == null ? Settings.STATS_WIDGET_APPSNO_LS_PREFERENCE : Integer.toString(defaultAppsNoLs),
                    Integer.toString(defaultAppsNoLs)));
            appnos_ls_preference.setOnPreferenceChangeListener(changeListener);
            if (apps_by_widget_size_preference.isChecked()) {
                appnos_ls_preference.setSummary(R.string.automatic_appsno);
            }

            alignment_preference = (UpdatingListPreference)findPreference(Settings.ALIGNMENT_PREFERENCE, UpdatingListPreference.class);
            alignment_preference.setValue(mPrefs.getString(Settings.ALIGNMENT_PREFERENCE,
                    Integer.toString(Settings.ALIGNMENT_DEFAULT)));
            alignment_preference.setOnPreferenceChangeListener(changeListener);
        }
        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, Object newValue) {
                if (preference.getKey().equals(Settings.BACKGROUND_COLOR_PREFERENCE)) {
                    preference.setSummary(0);
                } else if (preference.getKey().equals(Settings.APPS_BY_WIDGET_SIZE_PREFERENCE)) {
                    if (apps_by_widget_size_preference.isChecked()) {
                        appnos_preference.setSummary(R.string.automatic_appsno);
                        appnos_ls_preference.setSummary(R.string.automatic_appsno);
                    } else {
                        appnos_preference.goDefault(String.format(getResources().getString(R.string.summary_stats_widget_appsno_preference), appnos_preference.getValue()));
                        appnos_ls_preference.goDefault(String.format(getResources().getString(R.string.summary_stats_widget_appsno_ls_preference), appnos_ls_preference.getValue()));
                    }
                } else if (preference.getKey().equals(Settings.STATS_WIDGET_APPSNO_PREFERENCE)) {
                    appnos_preference.goDefault(String.format(getResources().getString(R.string.summary_stats_widget_appsno_preference), (String) newValue));
                } else if (preference.getKey().equals(Settings.STATS_WIDGET_APPSNO_LS_PREFERENCE)) {
                    appnos_ls_preference.goDefault(String.format(getResources().getString(R.string.summary_stats_widget_appsno_ls_preference), (String) newValue));
                }
                return true;
            }
        };
    }
}
