/**
 * Stub/model code simplified/modified from the Android library
 */

package android.net;

import android.os.Parcelable;

public class Uri implements Parcelable {

    public static Uri parse(String uriString) {
        return new Uri();
    }

    public String getScheme() {
        return "";
    }

    public String getHost() {
        return "";
    }

    public String getPath() {
        return "";
    }

    public int getPort() {
        return 0;
    }

    public static String encode(String s) {
        return s;
    }

    public static String encode(String s, String allow) {
        return s;
    }
}
