package com.vlille.checker;

import android.content.Context;
import android.content.pm.PackageManager;

public class Application extends android.app.Application {

    private static Context context;

    public static int dummy = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static String getVersionNumber() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "???";
        }
    }

}