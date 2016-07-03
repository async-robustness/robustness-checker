/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;

public abstract class BackupAgent extends Context {

    public abstract void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                                  ParcelFileDescriptor newState) throws IOException;

    public abstract void onRestore(BackupDataInput data, int appVersionCode,
                                   ParcelFileDescriptor newState)
            throws IOException;

    public void onRestoreFile(ParcelFileDescriptor data, long size,
                              File destination, int type, long mode, long mtime)
            throws IOException {
    }

}
