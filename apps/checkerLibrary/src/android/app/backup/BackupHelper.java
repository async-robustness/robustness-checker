/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.backup;

import android.os.ParcelFileDescriptor;

public interface BackupHelper {

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                              ParcelFileDescriptor newState);

    public void restoreEntity(BackupDataInputStream data);

    public void writeNewStateDescription(ParcelFileDescriptor newState);
}

