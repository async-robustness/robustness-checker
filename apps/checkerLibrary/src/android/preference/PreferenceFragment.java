/**
 * Stub/model code simplified/modified from the Android library
 */

package android.preference;

import android.app.Fragment;

public class PreferenceFragment extends Fragment {

    public void addPreferencesFromResource(int preferencesResId) {
    }

    public Preference findPreference(CharSequence key, Class preferenceClass) {
        Preference p;

        try{
            p = (Preference) preferenceClass.newInstance();
            return p;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new Preference();
    }
}
