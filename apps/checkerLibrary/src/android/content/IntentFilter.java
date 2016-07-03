/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import java.util.ArrayList;

public class IntentFilter {

    private final ArrayList<String> mActions = new ArrayList<String>();

    public final void addAction(String action) {
        if (!mActions.contains(action)) {
            mActions.add(action.intern());
        }
    }

    public final void addDataScheme(String scheme) {
    }
}
