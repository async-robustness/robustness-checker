/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content.pm;

import android.os.Parcelable;

public class ApplicationInfo implements Parcelable {

    public static final int FLAG_SYSTEM = 1<<0;
    public static final int FLAG_DEBUGGABLE = 1<<1;
    public static final int FLAG_HAS_CODE = 1<<2;
    public static final int FLAG_PERSISTENT = 1<<3;
    public static final int FLAG_FACTORY_TEST = 1<<4;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 1<<5;
    public static final int FLAG_ALLOW_CLEAR_USER_DATA = 1<<6;
    public static final int FLAG_UPDATED_SYSTEM_APP = 1<<7;
    public static final int FLAG_TEST_ONLY = 1<<8;
    public static final int FLAG_SUPPORTS_SMALL_SCREENS = 1<<9;
    public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1<<10; 
    public static final int FLAG_SUPPORTS_LARGE_SCREENS = 1<<11;
    public static final int FLAG_RESIZEABLE_FOR_SCREENS = 1<<12;
    public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 1<<13;
    public static final int FLAG_VM_SAFE_MODE = 1<<14;
    public static final int FLAG_ALLOW_BACKUP = 1<<15;
    public static final int FLAG_KILL_AFTER_RESTORE = 1<<16;
    public static final int FLAG_RESTORE_ANY_VERSION = 1<<17;
    public static final int FLAG_EXTERNAL_STORAGE = 1<<18;
    public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 1<<19;
    public static final int FLAG_LARGE_HEAP = 1<<20;
    public static final int FLAG_STOPPED = 1<<21;
    public static final int FLAG_SUPPORTS_RTL = 1<<22;
    public static final int FLAG_INSTALLED = 1<<23;
    public static final int FLAG_IS_DATA_ONLY = 1<<24;

    public static final String dataDir = "";
    public static final String packageName = "";

    public static final int uid = 0;

    public ApplicationInfo() {
    }
    
    public CharSequence loadLabel(PackageManager pm) {
        return "";
    }

}
