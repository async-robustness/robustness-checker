/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;

public class FileBackupHelper implements BackupHelper {

    public FileBackupHelper(Context context, String... files) {
    }

    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
    }

    public void restoreEntity(BackupDataInputStream data) {
    }

    @Override
    public void writeNewStateDescription(ParcelFileDescriptor newState) {

    }
}

