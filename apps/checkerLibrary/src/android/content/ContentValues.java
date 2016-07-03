/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.os.Parcelable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ContentValues implements Parcelable {
    public static final String TAG = "ContentValues";
    private HashMap<String, Object> mValues;

    public ContentValues() {
        // Choosing a default size of 8 based on analysis of typical
        // consumption by applications.
        mValues = new HashMap<String, Object>(8);
    }

    public ContentValues(int size) {
        mValues = new HashMap<String, Object>(size, 1.0f);
    }

    public ContentValues(ContentValues from) {
        mValues = new HashMap<String, Object>(from.mValues);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContentValues)) {
            return false;
        }
        return mValues.equals(((ContentValues) object).mValues);
    }

    @Override
    public int hashCode() {
        return mValues.hashCode();
    }

    public void put(String key, String value) {
        mValues.put(key, value);
    }

    public void putAll(ContentValues other) {
        mValues.putAll(other.mValues);
    }

    public void put(String key, Byte value) {
        mValues.put(key, value);
    }

    public void put(String key, Short value) {
        mValues.put(key, value);
    }

    public void put(String key, Integer value) {
        mValues.put(key, value);
    }

    public void put(String key, Long value) {
        mValues.put(key, value);
    }

    public void put(String key, Float value) {
        mValues.put(key, value);
    }

    public void put(String key, Double value) {
        mValues.put(key, value);
    }

    public void put(String key, Boolean value) {
        mValues.put(key, value);
    }

    public void put(String key, byte[] value) {
        mValues.put(key, value);
    }

    public void putNull(String key) {
        mValues.put(key, null);
    }

    public int size() {
        return mValues.size();
    }

    public void remove(String key) {
        mValues.remove(key);
    }

    public void clear() {
        mValues.clear();
    }

    public boolean containsKey(String key) {
        return mValues.containsKey(key);
    }

    public Object get(String key) {
        return mValues.get(key);
    }

    public String getAsString(String key) {
        Object value = mValues.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).longValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Long value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Long: " + value, e);
                return null;
            }
        }
    }

    public Integer getAsInteger(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).intValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Integer value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Integer: " + value, e);
                return null;
            }
        }
    }

    public Short getAsShort(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).shortValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Short.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Short value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Short: " + value, e);
                return null;
            }
        }
    }

    public Byte getAsByte(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).byteValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Byte value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Byte: " + value, e);
                return null;
            }
        }
    }

    public Double getAsDouble(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).doubleValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Double value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Double: " + value, e);
                return null;
            }
        }
    }

    public Float getAsFloat(String key) {
        Object value = mValues.get(key);
        try {
            return value != null ? ((Number) value).floatValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Cannot parse Float value for " + value + " at key " + key);
                    return null;
                }
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Float: " + value, e);
                return null;
            }
        }
    }

    public Boolean getAsBoolean(String key) {
        Object value = mValues.get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else {
                Log.e(TAG, "Cannot cast value for " + key + " to a Boolean: " + value, e);
                return null;
            }
        }
    }

    public byte[] getAsByteArray(String key) {
        Object value = mValues.get(key);
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else {
            return null;
        }
    }

    public Set<Map.Entry<String, Object>> valueSet() {
        return mValues.entrySet();
    }

    public Set<String> keySet() {
        return mValues.keySet();
    }

}
