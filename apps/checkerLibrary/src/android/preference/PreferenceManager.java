/**
 * Stub/model code simplified/modified from the Android library
 */

package android.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static SharedPreferences pref;

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        if(pref == null) {
            pref = new SharedPreferences();
        }
        return pref;
    }

    public void setSharedPreferencesName(String name) {
    }
}
