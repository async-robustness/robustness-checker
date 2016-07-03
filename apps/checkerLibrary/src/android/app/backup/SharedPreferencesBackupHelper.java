/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;

public class SharedPreferencesBackupHelper implements BackupHelper {

    public SharedPreferencesBackupHelper(Context context, String s) {

    }

    @Override
    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {

    }

    @Override
    public void restoreEntity(BackupDataInputStream data) {

    }

    @Override
    public void writeNewStateDescription(ParcelFileDescriptor newState) {

    }
}
