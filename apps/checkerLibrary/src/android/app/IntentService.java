/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public abstract class IntentService extends Service {
    private ServiceHandler mServiceHandler = new ServiceHandler();

    private final class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            onHandleIntent((Intent) msg.obj);
        }
    }

    public IntentService(String name) {
    }

    public void setIntentRedelivery(boolean enabled) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
    }

    protected abstract void onHandleIntent(Intent intent);
}
