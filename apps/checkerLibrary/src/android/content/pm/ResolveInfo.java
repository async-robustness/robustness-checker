/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content.pm;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;

public class ResolveInfo {

    public static final ActivityInfo activityInfo = new ActivityInfo();
    public static final IntentFilter filter = new IntentFilter();

    public ResolveInfo() {
    }
    public Drawable loadIcon(PackageManager pm) {
        return new Drawable();
    }

    public CharSequence loadLabel(PackageManager pm) {
        return "";
    }
}
