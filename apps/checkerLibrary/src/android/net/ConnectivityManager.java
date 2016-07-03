/**
 * Stub/model code simplified/modified from the Android library
 */

package android.net;

public class ConnectivityManager {

    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private NetworkInfo networkInfo = new NetworkInfo();

    public NetworkInfo getActiveNetworkInfo() {
        return networkInfo;
    }
}
