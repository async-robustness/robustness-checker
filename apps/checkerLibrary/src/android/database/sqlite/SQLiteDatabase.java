/**
 * Stub/model code simplified/modified from the Android library
 */

package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import android.os.CancellationSignal;

public final class SQLiteDatabase {

    public void execSQL(String sql) {
    }

    public boolean isOpen() {
        return false;
    }

    public void open() {
    }

    public void close() {
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return 0;
    }

    public SQLiteStatement compileStatement(String sql) throws SQLException {
        return new SQLiteStatement();
    }

    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        return new Cursor();
    }

    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return new Cursor();
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, null);
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {

        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return 0;
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return new Cursor();
    }

    public Cursor rawQuery(String sql, String[] selectionArgs,
                           CancellationSignal cancellationSignal) {
        return new Cursor();
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return 0;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return 0;
    }

    public void beginTransaction() {
    }

    public void endTransaction() {
    }

    public void setTransactionSuccessful() {
    }

    public static SQLiteDatabase openDatabase(String path, Object factory, int flags) {
        return new SQLiteDatabase();
    }

    public static SQLiteDatabase openDatabase(String path, Object factory, int flags,
                                              Object errorHandler) {
        return new SQLiteDatabase();
    }

    public static final int OPEN_READWRITE = 0x00000000;
    public static final int OPEN_READONLY = 0x00000001;
}
