package android.os;

import java.io.File;

/**
 * Created by burcuozkan on 05/05/16.
 */
public class Environment {


    public static File getDataDirectory() {
        return new File("");
    }

    public static File getExternalStorageDirectory() {
        return new File("");
    }

    public static String getExternalStorageState() {
        return "";
    }

    public static File getExternalStoragePublicDirectory(String s) {
        return new File("");
    }

    public static final String MEDIA_UNKNOWN = "unknown";
    public static final String MEDIA_REMOVED = "removed";
    public static final String MEDIA_UNMOUNTED = "unmounted";
    public static final String MEDIA_CHECKING = "checking";
    public static final String MEDIA_NOFS = "nofs";
    public static final String MEDIA_MOUNTED = "mounted";
    public static final String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
    public static final String MEDIA_SHARED = "shared";
    public static final String MEDIA_BAD_REMOVAL = "bad_removal";
    public static final String MEDIA_UNMOUNTABLE = "unmountable";

    public static final String DIRECTORY_NOTIFICATIONS = "Notifications";
    public static final String DIRECTORY_PICTURES = "Pictures";
}
