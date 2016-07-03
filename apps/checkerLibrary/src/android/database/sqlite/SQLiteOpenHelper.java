/**
 * Stub/model code simplified/modified from the Android library
 */

package android.database.sqlite;

import android.content.Context;

public abstract class SQLiteOpenHelper {

    public SQLiteOpenHelper(Context context, String name, Object factory, int version) {
        this(context, name, factory, version, null);
    }

    public SQLiteOpenHelper(Context context, String name, Object factory, int version,
            Object errorHandler) {
    }

    public SQLiteDatabase getWritableDatabase() {
        return new SQLiteDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return new SQLiteDatabase();
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        return new SQLiteDatabase();
    }

    public void close() {
    }

    public void onConfigure(SQLiteDatabase db) {}

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /*public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }*/
    public void onOpen(SQLiteDatabase db) {}
}
