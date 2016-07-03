/**
 * Stub/model code simplified/modified from the Android library
 */

package android.location;


public class LocationManager {

    public static final String NETWORK_PROVIDER = "network";
    public static final String GPS_PROVIDER = "gps";

    public Location getLastLocation() {
        return new Location();
    }

    public Location getLastKnownLocation(String provider) {
        return new Location();
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance,
                                       LocationListener listener) {
    }

    public void removeUpdates(LocationListener listener) {
    }

}
