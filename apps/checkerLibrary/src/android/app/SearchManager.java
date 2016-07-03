/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.content.DialogInterface;

public class SearchManager
        implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    public final static String QUERY = "query";
    public final static String USER_QUERY = "user_query";

    @Override
    public void onCancel(DialogInterface dialog) {
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
    }
}
