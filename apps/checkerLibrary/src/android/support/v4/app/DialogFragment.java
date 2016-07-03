/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.app;

import android.app.Dialog;
import android.os.Bundle;

public class DialogFragment extends Fragment {

    private Dialog mDialog = new Dialog();

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }

    public void dismiss() {
    }

    public boolean getShowsDialog() {
        return false;
    }
}
