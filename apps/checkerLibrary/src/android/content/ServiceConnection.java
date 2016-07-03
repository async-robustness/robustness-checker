/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.os.IBinder;

public interface ServiceConnection {

    public void onServiceConnected(ComponentName name, IBinder service);

    public void onServiceDisconnected(ComponentName name);
}
