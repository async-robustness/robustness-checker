/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Intent implements Parcelable {

    private String mAction;
    private Uri mData;
    private String mType; //mime type
    private String mPackage; //filter only in this package
    private ComponentName mComponent; // explicit name of comp to start
    private int mFlags;
    private HashSet<String> mCategories;
    private Bundle mExtras;
    private Intent mSelector;

    public Activity getNewActivity() {
        return new Activity(this);
    }

    public Intent() {
    }

    public Intent(Intent o) {
        this.mAction = o.mAction;
        this.mData = o.mData;
        this.mType = o.mType;
        this.mPackage = o.mPackage;
        this.mComponent = o.mComponent;
        this.mFlags = o.mFlags;
        if (o.mCategories != null) {
            this.mCategories = new HashSet<String>(o.mCategories);
        }
        if (o.mExtras != null) {
            this.mExtras = new Bundle(o.mExtras);
        }

        if (o.mSelector != null) {
            this.mSelector = new Intent(o.mSelector);
        }
    }

    public Intent(String action) {
        setAction(action);
    }

    public Intent(String action, Uri uri) {
        setAction(action);
        mData = uri;
    }

    public Intent(String action, Uri uri,
                  Context packageContext, Class<?> cls) {
        setAction(action);
        mData = uri;
        mComponent = new ComponentName(packageContext, cls);
    }

    public Intent setAction(String action) {
        mAction = action != null ? action.intern() : null;
        return this;
    }

    private Intent(Intent o, boolean all) {
        this.mAction = o.mAction;
        this.mData = o.mData;
        this.mType = o.mType;
        this.mPackage = o.mPackage;
        this.mComponent = o.mComponent;
        if (o.mCategories != null) {
            this.mCategories = new HashSet<String>(o.mCategories);
        }
    }

    // added
    public Intent(Context packageContext, Activity act) {
    }

    public Intent(Context packageContext, Class<?> cls) {
        mComponent = new ComponentName(packageContext, cls);
    }

    public ComponentName getComponent() {
        return mComponent;
    }

    public Intent setData(Uri data) {
        mData = data;
        mType = null;
        return this;
    }

    public String getDataString() {
        return mData != null ? mData.toString() : null;
    }

    public Intent setClass(Context packageContext, Class<?> cls) {
        mComponent = new ComponentName(packageContext, cls);
        return this;
    }

    public void setPackage(String packageName) {
    }

    public Intent setComponent(ComponentName component) {
        mComponent = component;
        return this;
    }

    public Intent addCategory(String category) {
        if (mCategories == null) {
            mCategories = new HashSet<String>();
        }
        mCategories.add(category.intern());
        return this;
    }

    public void removeCategory(String category) {
        if (mCategories != null) {
            mCategories.remove(category);
            if (mCategories.size() == 0) {
                mCategories = null;
            }
        }
    }

    public boolean hasExtra(String name) {
        return mExtras != null && mExtras.containsKey(name);
    }

    public static Intent createChooser(Intent target, CharSequence title) {
        Intent intent = new Intent(ACTION_CHOOSER);
        intent.putExtra(EXTRA_INTENT, target);
        if (title != null) {
            intent.putExtra(EXTRA_TITLE, title);
        }

        // Migrate any clip data and flags from target.
        /*int permFlags = target.getFlags()
                & (FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        if (permFlags != 0) {
            ClipData targetClipData = target.getClipData();
            if (targetClipData == null && target.getData() != null) {
                ClipData.Item item = new ClipData.Item(target.getData());
                String[] mimeTypes;
                if (target.getType() != null) {
                    mimeTypes = new String[] { target.getType() };
                } else {
                    mimeTypes = new String[] { };
                }
                targetClipData = new ClipData(null, mimeTypes, item);
            }
            if (targetClipData != null) {
                intent.setClipData(targetClipData);
                intent.addFlags(permFlags);
            }
        }*/

        return intent;
    }

    /*private ClipData mClipData;

    public ClipData getClipData() {
        return mClipData;
    }

    public void setClipData(ClipData clip) {
        mClipData = clip;
    }*/

    public Intent addFlags(int flags) {
        mFlags |= flags;
        return this;
    }

    public String getType() {
        return mType;
    }

    public Intent setType(String type) {
        mData = null;
        mType = type;
        return this;
    }

    public String getAction() {
        return mAction;
    }

    public Uri getData() {
        return mData;
    }

    // get extra
    public Object getExtra(String name) {
        return getExtra(name, null);
    }

    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getBoolean(name, defaultValue);
    }

    public byte getByteExtra(String name, byte defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getByte(name, defaultValue);
    }

    public short getShortExtra(String name, short defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getShort(name, defaultValue);
    }

    public char getCharExtra(String name, char defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getChar(name, defaultValue);
    }

    public int getIntExtra(String name, int defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getInt(name, defaultValue);
    }

    public long getLongExtra(String name, long defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getLong(name, defaultValue);
    }

    public float getFloatExtra(String name, float defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getFloat(name, defaultValue);
    }

    public double getDoubleExtra(String name, double defaultValue) {
        return mExtras == null ? defaultValue :
                mExtras.getDouble(name, defaultValue);
    }

    public String getStringExtra(String name) {
        return mExtras == null ? null : mExtras.getString(name);
    }

    public CharSequence getCharSequenceExtra(String name) {
        return mExtras == null ? null : mExtras.getCharSequence(name);
    }

    public <T extends Parcelable> T getParcelableExtra(String name) {
        return mExtras == null ? null : mExtras.<T>getParcelable(name);
    }

    public Parcelable[] getParcelableArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getParcelableArray(name);
    }

    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.<T>getParcelableArrayList(name);
    }

    public Serializable getSerializableExtra(String name) {
        return mExtras == null ? null : mExtras.getSerializable(name);
    }

    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.getIntegerArrayList(name);
    }

    public ArrayList<String> getStringArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.getStringArrayList(name);
    }

    public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
        return mExtras == null ? null : mExtras.getCharSequenceArrayList(name);
    }

    public boolean[] getBooleanArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getBooleanArray(name);
    }

    public byte[] getByteArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getByteArray(name);
    }

    public short[] getShortArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getShortArray(name);
    }

    public char[] getCharArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getCharArray(name);
    }

    public int[] getIntArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getIntArray(name);
    }

    public long[] getLongArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getLongArray(name);
    }

    public float[] getFloatArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getFloatArray(name);
    }

    public double[] getDoubleArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getDoubleArray(name);
    }

    public String[] getStringArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getStringArray(name);
    }

    public CharSequence[] getCharSequenceArrayExtra(String name) {
        return mExtras == null ? null : mExtras.getCharSequenceArray(name);
    }

    public Bundle getBundleExtra(String name) {
        return mExtras == null ? null : mExtras.getBundle(name);
    }

    /*public IBinder getIBinderExtra(String name) {
        return mExtras == null ? null : mExtras.getIBinder(name);
    }*/

    public Object getExtra(String name, Object defaultValue) {
        Object result = defaultValue;
        if (mExtras != null) {
            Object result2 = mExtras.get(name);
            if (result2 != null) {
                result = result2;
            }
        }

        return result;
    }

    public Bundle getExtras() {
        return (mExtras != null)
                ? new Bundle(mExtras)
                : null;
    }

    public int getFlags() {
        return mFlags;
    }

    // put extra
    public Intent putExtra(String name, boolean value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBoolean(name, value);
        return this;
    }

    public Intent putExtra(String name, byte value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putByte(name, value);
        return this;
    }

    public Intent putExtra(String name, char value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putChar(name, value);
        return this;
    }

    public Intent putExtra(String name, short value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putShort(name, value);
        return this;
    }

    public Intent putExtra(String name, int value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putInt(name, value);
        return this;
    }

    public Intent putExtra(String name, long value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putLong(name, value);
        return this;
    }

    public Intent putExtra(String name, float value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putFloat(name, value);
        return this;
    }

    public Intent putExtra(String name, double value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putDouble(name, value);
        return this;
    }

    public Intent putExtra(String name, String value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putString(name, value);
        return this;
    }

    public Intent putExtra(String name, CharSequence value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharSequence(name, value);
        return this;
    }

    public Intent putExtra(String name, Parcelable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelable(name, value);
        return this;
    }

    public Intent putExtra(String name, Parcelable[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelableArray(name, value);
        return this;
    }

    public Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelableArrayList(name, value);
        return this;
    }

    public Intent putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putIntegerArrayList(name, value);
        return this;
    }

    public Intent putStringArrayListExtra(String name, ArrayList<String> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putStringArrayList(name, value);
        return this;
    }

    public Intent putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharSequenceArrayList(name, value);
        return this;
    }

    public Intent putExtra(String name, Serializable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putSerializable(name, value);
        return this;
    }

    public Intent putExtra(String name, boolean[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBooleanArray(name, value);
        return this;
    }

    public Intent putExtra(String name, byte[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putByteArray(name, value);
        return this;
    }

    public Intent putExtra(String name, short[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putShortArray(name, value);
        return this;
    }

    public Intent putExtra(String name, char[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharArray(name, value);
        return this;
    }

    public Intent putExtra(String name, int[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putIntArray(name, value);
        return this;
    }

    public Intent putExtra(String name, long[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putLongArray(name, value);
        return this;
    }

    public Intent putExtra(String name, float[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putFloatArray(name, value);
        return this;
    }

    public Intent putExtra(String name, double[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putDoubleArray(name, value);
        return this;
    }

    public Intent putExtra(String name, String[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putStringArray(name, value);
        return this;
    }

    public Intent putExtra(String name, CharSequence[] value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharSequenceArray(name, value);
        return this;
    }

    public Intent putExtra(String name, Bundle value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBundle(name, value);
        return this;
    }

    /*public Intent putExtra(String name, IBinder value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putIBinder(name, value);
        return this;
    }*/

    public Intent putExtras(Intent src) {
        if (src.mExtras != null) {
            if (mExtras == null) {
                mExtras = new Bundle(src.mExtras);
            } else {
                mExtras.putAll(src.mExtras);
            }
        }
        return this;
    }

    public Intent putExtras(Bundle extras) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putAll(extras);
        return this;
    }

    public Intent replaceExtras(Intent src) {
        mExtras = src.mExtras != null ? new Bundle(src.mExtras) : null;
        return this;
    }

    public Intent replaceExtras(Bundle extras) {
        mExtras = extras != null ? new Bundle(extras) : null;
        return this;
    }

    public void removeExtra(String name) {
        if (mExtras != null) {
            mExtras.remove(name);
            if (mExtras.size() == 0) {
                mExtras = null;
            }
        }
    }


    // Strings
    public static final String ACTION_MAIN = "android.intent.action.MAIN";
    public static final String ACTION_VIEW = "android.intent.action.VIEW";
    public static final String ACTION_DEFAULT = ACTION_VIEW;
    public static final String ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA";
    public static final String ACTION_EDIT = "android.intent.action.EDIT";
    public static final String ACTION_INSERT_OR_EDIT = "android.intent.action.INSERT_OR_EDIT";
    public static final String ACTION_PICK = "android.intent.action.PICK";
    public static final String ACTION_CREATE_SHORTCUT = "android.intent.action.CREATE_SHORTCUT";
    public static final String EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT";
    public static final String EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME";
    public static final String EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON";
    public static final String EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE";
    public static final String ACTION_CHOOSER = "android.intent.action.CHOOSER";
    public static final String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
    public static final String ACTION_DIAL = "android.intent.action.DIAL";
    public static final String ACTION_CALL = "android.intent.action.CALL";
    public static final String ACTION_CALL_EMERGENCY = "android.intent.action.CALL_EMERGENCY";
    public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
    public static final String ACTION_SENDTO = "android.intent.action.SENDTO";
    public static final String ACTION_SEND = "android.intent.action.SEND";
    public static final String ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
    public static final String ACTION_ANSWER = "android.intent.action.ANSWER";
    public static final String ACTION_INSERT = "android.intent.action.INSERT";
    public static final String ACTION_PASTE = "android.intent.action.PASTE";
    public static final String ACTION_DELETE = "android.intent.action.DELETE";
    public static final String ACTION_RUN = "android.intent.action.RUN";
    public static final String ACTION_SYNC = "android.intent.action.SYNC";
    public static final String ACTION_PICK_ACTIVITY = "android.intent.action.PICK_ACTIVITY";
    public static final String ACTION_SEARCH = "android.intent.action.SEARCH";
    public static final String ACTION_SYSTEM_TUTORIAL = "android.intent.action.SYSTEM_TUTORIAL";
    public static final String ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH";
    public static final String ACTION_ALL_APPS = "android.intent.action.ALL_APPS";
    public static final String ACTION_SET_WALLPAPER = "android.intent.action.SET_WALLPAPER";
    public static final String ACTION_BUG_REPORT = "android.intent.action.BUG_REPORT";
    public static final String ACTION_FACTORY_TEST = "android.intent.action.FACTORY_TEST";
    public static final String ACTION_CALL_BUTTON = "android.intent.action.CALL_BUTTON";
    public static final String ACTION_VOICE_COMMAND = "android.intent.action.VOICE_COMMAND";
    public static final String ACTION_SEARCH_LONG_PRESS = "android.intent.action.SEARCH_LONG_PRESS";
    public static final String ACTION_APP_ERROR = "android.intent.action.APP_ERROR";
    public static final String ACTION_POWER_USAGE_SUMMARY = "android.intent.action.POWER_USAGE_SUMMARY";
    public static final String ACTION_UPGRADE_SETUP = "android.intent.action.UPGRADE_SETUP";
    public static final String ACTION_MANAGE_NETWORK_USAGE = "android.intent.action.MANAGE_NETWORK_USAGE";
    public static final String ACTION_INSTALL_PACKAGE = "android.intent.action.INSTALL_PACKAGE";
    public static final String EXTRA_INSTALLER_PACKAGE_NAME = "android.intent.extra.INSTALLER_PACKAGE_NAME";
    public static final String EXTRA_NOT_UNKNOWN_SOURCE = "android.intent.extra.NOT_UNKNOWN_SOURCE";
    public static final String EXTRA_ALLOW_REPLACE = "android.intent.extra.ALLOW_REPLACE";
    public static final String EXTRA_RETURN_RESULT = "android.intent.extra.RETURN_RESULT";
    public static final String EXTRA_INSTALL_RESULT = "android.intent.extra.INSTALL_RESULT";
    public static final String ACTION_UNINSTALL_PACKAGE = "android.intent.action.UNINSTALL_PACKAGE";
    public static final String METADATA_SETUP_VERSION = "android.SETUP_VERSION";
    // Standard intent broadcast actions (see action variable).
    public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
    public static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
    public static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
    public static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
    public static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
    public static final String ACTION_CLEAR_DNS_CACHE = "android.intent.action.CLEAR_DNS_CACHE";
    public static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";
    public static final String ACTION_SYNC_STATE_CHANGED = "android.intent.action.SYNC_STATE_CHANGED";
    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";
    public static final String ACTION_PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL";
    public static final String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
    public static final String ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
    public static final String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";
    public static final String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    public static final String ACTION_PACKAGE_FULLY_REMOVED = "android.intent.action.PACKAGE_FULLY_REMOVED";
    public static final String ACTION_QUERY_PACKAGE_RESTART = "android.intent.action.QUERY_PACKAGE_RESTART";
    public static final String ACTION_PACKAGE_RESTARTED = "android.intent.action.PACKAGE_RESTARTED";
    public static final String ACTION_PACKAGE_DATA_CLEARED = "android.intent.action.PACKAGE_DATA_CLEARED";
    public static final String ACTION_UID_REMOVED = "android.intent.action.UID_REMOVED";
    public static final String ACTION_PACKAGE_FIRST_LAUNCH = "android.intent.action.PACKAGE_FIRST_LAUNCH";
    public static final String ACTION_PACKAGE_NEEDS_VERIFICATION = "android.intent.action.PACKAGE_NEEDS_VERIFICATION";
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
    public static final String ACTION_WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED";
    public static final String ACTION_CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
    public static final String ACTION_BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
    public static final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    public static final String ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";
    public static final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
    public static final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
    public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    public static final String ACTION_REQUEST_SHUTDOWN = "android.intent.action.ACTION_REQUEST_SHUTDOWN";
    public static final String ACTION_DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW";
    public static final String ACTION_DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK";
    public static final String ACTION_DEVICE_STORAGE_FULL = "android.intent.action.DEVICE_STORAGE_FULL";
    public static final String ACTION_MANAGE_PACKAGE_STORAGE = "android.intent.action.MANAGE_PACKAGE_STORAGE";
    @Deprecated
    public static final String ACTION_UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
    @Deprecated
    public static final String ACTION_UMS_DISCONNECTED = "android.intent.action.UMS_DISCONNECTED";
    public static final String ACTION_MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
    public static final String ACTION_MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
    public static final String ACTION_MEDIA_CHECKING = "android.intent.action.MEDIA_CHECKING";
    public static final String ACTION_MEDIA_NOFS = "android.intent.action.MEDIA_NOFS";
    public static final String ACTION_MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    public static final String ACTION_MEDIA_SHARED = "android.intent.action.MEDIA_SHARED";
    public static final String ACTION_MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
    public static final String ACTION_MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";
    public static final String ACTION_MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
    public static final String ACTION_MEDIA_SCANNER_STARTED = "android.intent.action.MEDIA_SCANNER_STARTED";
    public static final String ACTION_MEDIA_SCANNER_FINISHED = "android.intent.action.MEDIA_SCANNER_FINISHED";
    public static final String ACTION_MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
    public static final String ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON";
    public static final String ACTION_CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON";
    public static final String ACTION_GTALK_SERVICE_CONNECTED = "android.intent.action.GTALK_CONNECTED";
    public static final String ACTION_GTALK_SERVICE_DISCONNECTED = "android.intent.action.GTALK_DISCONNECTED";
    public static final String ACTION_INPUT_METHOD_CHANGED = "android.intent.action.INPUT_METHOD_CHANGED";
    public static final String ACTION_AIRPLANE_MODE_CHANGED = "android.intent.action.AIRPLANE_MODE";
    public static final String ACTION_PROVIDER_CHANGED = "android.intent.action.PROVIDER_CHANGED";
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final String ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String ACTION_REBOOT = "android.intent.action.REBOOT";
    public static final String ACTION_DOCK_EVENT = "android.intent.action.DOCK_EVENT";
    // Standard intent categories (see addCategory()).
    public static final String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
    public static final String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
    public static final String CATEGORY_ALTERNATIVE = "android.intent.category.ALTERNATIVE";
    public static final String CATEGORY_SELECTED_ALTERNATIVE = "android.intent.category.SELECTED_ALTERNATIVE";
    public static final String CATEGORY_TAB = "android.intent.category.TAB";
    public static final String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";
    public static final String CATEGORY_INFO = "android.intent.category.INFO";
    public static final String CATEGORY_HOME = "android.intent.category.HOME";
    public static final String CATEGORY_PREFERENCE = "android.intent.category.PREFERENCE";
    public static final String CATEGORY_DEVELOPMENT_PREFERENCE = "android.intent.category.DEVELOPMENT_PREFERENCE";
    public static final String CATEGORY_EMBED = "android.intent.category.EMBED";
    public static final String CATEGORY_APP_MARKET = "android.intent.category.APP_MARKET";
    public static final String CATEGORY_MONKEY = "android.intent.category.MONKEY";
    public static final String CATEGORY_TEST = "android.intent.category.TEST";
    public static final String CATEGORY_UNIT_TEST = "android.intent.category.UNIT_TEST";
    public static final String CATEGORY_SAMPLE_CODE = "android.intent.category.SAMPLE_CODE";
    public static final String CATEGORY_OPENABLE = "android.intent.category.OPENABLE";
    public static final String CATEGORY_FRAMEWORK_INSTRUMENTATION_TEST = "android.intent.category.FRAMEWORK_INSTRUMENTATION_TEST";
    public static final String CATEGORY_CAR_DOCK = "android.intent.category.CAR_DOCK";
    public static final String CATEGORY_DESK_DOCK = "android.intent.category.DESK_DOCK";
    public static final String CATEGORY_LE_DESK_DOCK = "android.intent.category.LE_DESK_DOCK";
    public static final String CATEGORY_HE_DESK_DOCK = "android.intent.category.HE_DESK_DOCK";
    public static final String CATEGORY_CAR_MODE = "android.intent.category.CAR_MODE";
    // Application launch intent categories (see addCategory()).
    public static final String CATEGORY_APP_BROWSER = "android.intent.category.APP_BROWSER";
    public static final String CATEGORY_APP_CALCULATOR = "android.intent.category.APP_CALCULATOR";
    public static final String CATEGORY_APP_CALENDAR = "android.intent.category.APP_CALENDAR";
    public static final String CATEGORY_APP_CONTACTS = "android.intent.category.APP_CONTACTS";
    public static final String CATEGORY_APP_EMAIL = "android.intent.category.APP_EMAIL";
    public static final String CATEGORY_APP_GALLERY = "android.intent.category.APP_GALLERY";
    public static final String CATEGORY_APP_MAPS = "android.intent.category.APP_MAPS";
    public static final String CATEGORY_APP_MESSAGING = "android.intent.category.APP_MESSAGING";
    public static final String CATEGORY_APP_MUSIC = "android.intent.category.APP_MUSIC";
    // Standard extra data keys.
    public static final String EXTRA_TEMPLATE = "android.intent.extra.TEMPLATE";
    public static final String EXTRA_TEXT = "android.intent.extra.TEXT";
    public static final String EXTRA_STREAM = "android.intent.extra.STREAM";
    public static final String EXTRA_EMAIL = "android.intent.extra.EMAIL";
    public static final String EXTRA_CC = "android.intent.extra.CC";
    public static final String EXTRA_BCC = "android.intent.extra.BCC";
    public static final String EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    public static final String EXTRA_INTENT = "android.intent.extra.INTENT";
    public static final String EXTRA_TITLE = "android.intent.extra.TITLE";
    public static final String EXTRA_INITIAL_INTENTS = "android.intent.extra.INITIAL_INTENTS";
    public static final String EXTRA_KEY_EVENT = "android.intent.extra.KEY_EVENT";
    public static final String EXTRA_DONT_KILL_APP = "android.intent.extra.DONT_KILL_APP";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    public static final String EXTRA_UID = "android.intent.extra.UID";
    public static final String EXTRA_DATA_REMOVED = "android.intent.extra.DATA_REMOVED";
    public static final String EXTRA_REPLACING = "android.intent.extra.REPLACING";
    public static final String EXTRA_ALARM_COUNT = "android.intent.extra.ALARM_COUNT";
    public static final String EXTRA_DOCK_STATE = "android.intent.extra.DOCK_STATE";
    public static final int EXTRA_DOCK_STATE_UNDOCKED = 0;
    public static final int EXTRA_DOCK_STATE_DESK = 1;
    public static final int EXTRA_DOCK_STATE_CAR = 2;
    public static final int EXTRA_DOCK_STATE_LE_DESK = 3;
    public static final int EXTRA_DOCK_STATE_HE_DESK = 4;
    public static final String METADATA_DOCK_HOME = "android.dock_home";
    public static final String EXTRA_BUG_REPORT = "android.intent.extra.BUG_REPORT";
    public static final String EXTRA_REMOTE_INTENT_TOKEN = "android.intent.extra.remote_intent_token";
    @Deprecated
    public static final String EXTRA_CHANGED_COMPONENT_NAME = "android.intent.extra.changed_component_name";
    public static final String EXTRA_CHANGED_COMPONENT_NAME_LIST = "android.intent.extra.changed_component_name_list";
    public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
    public static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";
    // Intent flags (see mFlags variable).
    public static final int FLAG_GRANT_READ_URI_PERMISSION = 0x00000001;
    public static final int FLAG_GRANT_WRITE_URI_PERMISSION = 0x00000002;
    public static final int FLAG_FROM_BACKGROUND = 0x00000004;
    public static final int FLAG_DEBUG_LOG_RESOLUTION = 0x00000008;
    public static final int FLAG_EXCLUDE_STOPPED_PACKAGES = 0x00000010;
    public static final int FLAG_INCLUDE_STOPPED_PACKAGES = 0x00000020;
    public static final int FLAG_ACTIVITY_NO_HISTORY = 0x40000000;
    public static final int FLAG_ACTIVITY_SINGLE_TOP = 0x20000000;
    public static final int FLAG_ACTIVITY_NEW_TASK = 0x10000000;
    public static final int FLAG_ACTIVITY_MULTIPLE_TASK = 0x08000000;
    public static final int FLAG_ACTIVITY_CLEAR_TOP = 0x04000000;
    public static final int FLAG_ACTIVITY_FORWARD_RESULT = 0x02000000;
    public static final int FLAG_ACTIVITY_PREVIOUS_IS_TOP = 0x01000000;
    public static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0x00800000;
    public static final int FLAG_ACTIVITY_BROUGHT_TO_FRONT = 0x00400000;
    public static final int FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = 0x00200000;
    public static final int FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY = 0x00100000;
    public static final int FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET = 0x00080000;
    public static final int FLAG_ACTIVITY_NO_USER_ACTION = 0x00040000;
    public static final int FLAG_ACTIVITY_REORDER_TO_FRONT = 0X00020000;
    public static final int FLAG_ACTIVITY_NO_ANIMATION = 0X00010000;
    public static final int FLAG_ACTIVITY_CLEAR_TASK = 0X00008000;
    public static final int FLAG_ACTIVITY_TASK_ON_HOME = 0X00004000;
    public static final int FLAG_RECEIVER_REGISTERED_ONLY = 0x40000000;
    public static final int FLAG_RECEIVER_REPLACE_PENDING = 0x20000000;
}
