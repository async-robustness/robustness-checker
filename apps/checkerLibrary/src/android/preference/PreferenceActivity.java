/**
 * Stub/model code simplified/modified from the Android library
 */

package android.preference;

import android.app.ListActivity;

public class PreferenceActivity extends ListActivity {

    PreferenceManager mPreferenceManager = new PreferenceManager();

    public void addPreferencesFromResource(int preferencesResId) {
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public Preference findPreference(CharSequence key) {

        if (mPreferenceManager == null) {
            return null;
        }

        return new Preference();
    }

    public Preference findPreference(CharSequence key, Class clazz) {

        if (mPreferenceManager == null) {
            return null;
        }

        try{
            Preference p = (Preference) clazz.newInstance();
            return p;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
