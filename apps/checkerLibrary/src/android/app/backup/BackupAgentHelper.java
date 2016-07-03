/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app.backup;

import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class BackupAgentHelper extends BackupAgent {

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState)
            throws IOException {
    }

    public void addHelper(String keyPrefix, BackupHelper helper) {

    }
}


