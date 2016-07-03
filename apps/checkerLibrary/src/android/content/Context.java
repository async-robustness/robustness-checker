/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.app.*;
import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.accounts.AccountManager;
import checker.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    public static final int MODE_PRIVATE = 0x0000;

    public static final int CONTEXT_INCLUDE_CODE = 0x00000001;
    public static final int CONTEXT_IGNORE_SECURITY = 0x00000002;
    public static final int MODE_MULTI_PROCESS = 0x0004;

    public static final String CONNECTIVITY_SERVICE = "connectivity";
    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final String NOTIFICATION_SERVICE = "notification";
    public static final String LOCATION_SERVICE = "location";
    public static final String WIFI_SERVICE = "wifi";
    public static final String POWER_SERVICE = "power";
    public static final String WINDOW_SERVICE = "window";
    public static final String ACTIVITY_SERVICE = "activity";
    public static final String ALARM_SERVICE = "alarm";
    public static final String USAGE_STATS_SERVICE_NAME = "usagestats"; //added
    public static final String ACCOUNT_SERVICE = "account";

    public static final int BIND_AUTO_CREATE = 0x0001;
    public static final int BIND_DEBUG_UNBIND = 0x0002;
    public static final int BIND_NOT_FOREGROUND = 0x0004;
    public static final int BIND_IMPORTANT = 0x0040;

    private static final ConnectivityManager connectivityManager = new ConnectivityManager();
    private static final LayoutInflater layoutInflater = new LayoutInflater();
    private static final NotificationManager notificationManager = new NotificationManager();
    private static final WifiManager wifiManager = new WifiManager();
    private static final PowerManager mPowerManager = new PowerManager();
    private static final PackageManager mPackageManager = new PackageManager();
    private static final AssetManager mAssetManager = new AssetManager();
    private static final ActivityManager mActivityManager = new ActivityManager();
    private static final AlarmManager mAlarmManager = new AlarmManager();
    private static final WindowManager mWindowManager = new WindowManager();
    private static final UsageStatsManager mUsageStatsManager = new UsageStatsManager();
    private static final AccountManager mAccountManager = new AccountManager();

    private String packageName;
    private static ApplicationInfo mApplicationInfo = new ApplicationInfo();
    private static SharedPreferences mSharedPreferences = new SharedPreferences();
    private static Resources mResources = Resources.getInstance();
    
    public static final ContentResolver contentResolver = new ContentResolver();

    private List<BroadcastReceiver> mBroadcastReceivers = new ArrayList<BroadcastReceiver>();

    public static Application app;

    public Context() {
    }

    public static Application initApplication(Class<? extends Application> clazz) {
        if(app == null) {
            try{
                app = (Application) clazz.newInstance();
                app.onCreate();
            } catch (SkipException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("The application cannot be created: " + clazz.toString());
            }
        }
        return app;
    }

    public Application getApplication() {
        return app;
    }

    public Context getApplicationContext() {
        return this;
    }

    public ApplicationInfo getApplicationInfo() {return mApplicationInfo; }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return mSharedPreferences;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String name) {
        packageName = name;
    }

    public PackageManager getPackageManager() {
        return mPackageManager;
    }

    public Resources getResources() {
        return mResources;
    }

    public AssetManager getAssets() { return mAssetManager; }

    public  ContentResolver getContentResolver() {
        return contentResolver;
    }

    public String getString(int id) {
        return "";
    }

    public String getString(int id, String s) {
        return "";
    }

    public String getString(int id, float size) {
        return "";
    }

    public File getFilesDir() {
        return new File("");
    }

    public File getExternalFilesDir(String type) {
        return null;
    }

    public final String getString(int resId, Object... formatArgs) {
        return "";
    }

    public File getCacheDir() {
        return new File("");
    }

    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return this;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public Object getSystemService(String name){

        if(name.equalsIgnoreCase(Context.CONNECTIVITY_SERVICE)) {
            return connectivityManager;
        }
        if(name.equalsIgnoreCase(Context.NOTIFICATION_SERVICE)) {
            return notificationManager;
        }
        if(name.equalsIgnoreCase(Context.LAYOUT_INFLATER_SERVICE)) {
            return layoutInflater;
        }
        if(name.equalsIgnoreCase(Context.WIFI_SERVICE)) {
            return wifiManager;
        }
        if(name.equalsIgnoreCase(Context.POWER_SERVICE)) {
            return mPowerManager;
        }
        if(name.equalsIgnoreCase(Context.ACTIVITY_SERVICE)) {
            return mActivityManager;
        }
        if(name.equalsIgnoreCase(Context.ALARM_SERVICE)) {
            return mAlarmManager;
        }
        if(name.equalsIgnoreCase(Context.USAGE_STATS_SERVICE_NAME)) {
            return mUsageStatsManager;
        }
        if(name.equalsIgnoreCase(Context.ACCOUNT_SERVICE)) {
            return mAccountManager;
        }
        return new Object();
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public File getDir(String name, int mode) {
        return new File("");
    }

    public void startActivity(Intent intent) {
        //TODO - the current implementation stubs and does not start a new activity
        /*
        onPause();
        onStop();
        Activity act = intent.getNewActivity();
        act.onCreate();
        act.onStart();
        act.onResume();*/
    }

    public void startActivityForResult(Intent intent, int value) {
        //TODO - the current implementation stubs and does not start a new activity
        // call onPause(); onStop();
        // call intended Activity onCreate(); onStart(); onResume();
    }

    // keep a map per app
    public static Map<Class, Service> services = new HashMap<Class, Service>();

    public void startService(Intent intent) {
        Class serviceClass = intent.getComponent().getComponentClass();
        Service s = services.get(serviceClass);

        if(s == null) {
            try {
                s = (Service) serviceClass.newInstance();
                services.put(serviceClass, s);
                //System.out.println("Creating in start service");
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }

        s.onCreate();
        // started with 0 flag and startId
        s.onStartCommand(intent, 0, 0);
    }

    public void stopService(Intent intent) {
        Class serviceClass = intent.getComponent().getComponentClass();
        Service s = services.get(serviceClass);

        if(s == null) {
            try {
                s = (Service) serviceClass.newInstance();
                services.put(serviceClass, s);
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }

        s.onDestroy();
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        // call connection.onServiceConnected(ComponentName className, IBinder service)
        // read the service type from service
        Class serviceClass = service.getComponent().getComponentClass();
        Service s = services.get(serviceClass);

        if(s == null) {
            try {
                s = (Service) serviceClass.newInstance();
                s.onCreate();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }

        // the second parameter is a Binder, implemented by the app itself
        // e.g. Aarddict returns a LocalBinder that implements itself
        // conn.onServiceConnected(service.getComponent(), s.onBind(service)); // this is async on the main thread

        try {
            Checker.setProcMode(ProcMode.ASYNCMain);
            Checker.beforeAsyncProc(); // might skip the whole async proc
            conn.onServiceConnected(service.getComponent(), s.onBind(service)); // this is async on the main thread
        } catch (SkipException e) {
            // Do not block the caller (catch the exception and continue after AsyncTask.execute)
        } finally {
            Checker.afterAsyncProc();
            Checker.setProcMode(ProcMode.SYNCMain);
        }

        return false;
    }

    public void unbindService(ServiceConnection c) {

    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        mBroadcastReceivers.add(receiver);
        return new Intent();
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        mBroadcastReceivers.remove(receiver);
    }

    //public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler);

    public void sendBroadcast(Intent intent) {
        // resolve synchronously
        for(BroadcastReceiver br: mBroadcastReceivers) {
            br.onReceive(this, intent);
        }
    }
}
