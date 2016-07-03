/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.Context;
import android.content.Intent;

public class PendingIntent {

    public static final int FLAG_CANCEL_CURRENT = 1 << 28;
    public static final int FLAG_UPDATE_CURRENT = 1 << 27;

    public static PendingIntent getService(Context context, int requestCode, Intent intent, int flags) {
        return new PendingIntent();
    }

    public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags) {
        return new PendingIntent();
    }

    public static PendingIntent getBroadcast(Context context, int requestCode, Intent intent, int flags) {
        return new PendingIntent();
    }

}
