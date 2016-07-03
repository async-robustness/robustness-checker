/**
 * Stub/model code simplified/modified from the Android library
 */

package android.net;

public class NetworkInfo {

    public State getState() {
        return State.DISCONNECTED;
    }

    public enum State {
        CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
    }

    public boolean isAvailable() {
        return false;
    }

    public boolean isConnected() {
        return false;
    }

    public int getType() {
        return -1; // type none
    }

    public String getTypeName() {
        return "";
    }
}
