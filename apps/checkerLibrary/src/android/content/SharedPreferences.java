/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import java.util.Set;

public class SharedPreferences {

    public interface OnSharedPreferenceChangeListener {
        void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key);
    }

    public String getString(String key, String defValue) {
        return defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return defValues;
    }

    public int getInt(String key, int defValue) {
        return defValue;
    }

    public long getLong(String key, long defValue) {
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    public boolean contains(String key) {
        return false;
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    public class Editor {
        public Editor putString(String key, String value) {
            return this;
        }

        public Editor putStringSet(String key, Set<String> values) {
            return this;
        }

        public Editor putInt(String key, int value) {
            return this;
        }

        public Editor putLong(String key, long value) {
            return this;
        }

        public Editor putFloat(String key, float value) {
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            return this;
        }

        public Editor remove(String key) {
            return this;
        }

        public Editor clear() {
            return this;
        }

        public boolean commit() {
            return false;
        }

        public void apply() {
        }
    }

    public Editor edit() {
        return new Editor();
    }
}
