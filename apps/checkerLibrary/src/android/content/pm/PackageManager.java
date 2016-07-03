/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class PackageManager {

    public static class NameNotFoundException extends Exception {
        public NameNotFoundException() {
        }

        public NameNotFoundException(String name) {
            super(name);
        }
    }

    public static final PackageInfo mPackageInfo = new PackageInfo();
    public static final ApplicationInfo mApplicationInfo = new ApplicationInfo();
    public static final ActivityInfo mActivityInfo = new ActivityInfo();

    public PackageManager() {
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
        return mApplicationInfo;
    }

    public ActivityInfo getActivityInfo(ComponentName component, int flags) throws NameNotFoundException {
        return mActivityInfo;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
        return mPackageInfo;
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        return new ResolveInfo();
    }

    public CharSequence getApplicationLabel(ApplicationInfo info) {
        return "";
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        return new ArrayList<ResolveInfo>();
    }

    public Intent getLaunchIntentForPackage(String packageName) {
        return new Intent();
    }

    public Resources getResourcesForApplication(String name) throws NameNotFoundException {
        return Resources.getInstance();
    }

    public Resources getResourcesForApplication(ApplicationInfo info) throws NameNotFoundException {
        return Resources.getInstance();
    }

}
