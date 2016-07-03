/**
 * Stub/model code simplified/modified from the Android library
 */

package android.location;

import android.os.Bundle;

public interface LocationListener {

    void onLocationChanged(Location location);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);
}
