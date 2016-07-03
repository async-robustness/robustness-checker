/**
 * Stub/model code simplified/modified from the Android library
 */

package android.net.wifi;

public class WifiManager {

    public class WifiLock {
        public void acquire() {
        }

        public void release() {
        }

        public boolean isHeld() {
            return false;
        }
    }

    public WifiLock createWifiLock(int lockType, String tag) {
        return new WifiLock();
    }

    public WifiLock createWifiLock(String tag) {
        return new WifiLock();
    }

}


