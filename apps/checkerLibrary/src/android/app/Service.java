/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public class Service extends Context {

    public Service() {
    }

    public final Application getApplication() {
        return super.getApplication();
    }

    public void onCreate() {
    }

    public void onStart(Intent intent, int startId) {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final int START_CONTINUATION_MASK = 0xf;
    

    public static final int START_STICKY_COMPATIBILITY = 0;

    public static final int START_STICKY = 1;

    public static final int START_NOT_STICKY = 2;

    public static final int START_REDELIVER_INTENT = 3;

    public static final int START_TASK_REMOVED_COMPLETE = 1000;

    public static final int START_FLAG_REDELIVERY = 0x0001;

    public static final int START_FLAG_RETRY = 0x0002;

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    public void onDestroy() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }
    
    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    //public abstract IBinder onBind(Intent intent);

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void onRebind(Intent intent) {
    }

    public void onTaskRemoved(Intent rootIntent) {
    }

    public final void stopSelf() {
        stopSelf(-1);
    }

    public final void stopSelf(int startId) {
    }

    public final boolean stopSelfResult(int startId) {
        return false;
    }

    public final void setForeground(boolean isForeground) {
    }

    public final void startForeground(int id, Notification notification) {
    }

    public final void stopForeground(boolean removeNotification) {
    }

    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
    }

}
