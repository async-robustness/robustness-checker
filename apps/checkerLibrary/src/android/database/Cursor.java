/**
 * Stub/model code simplified/modified from the Android library
 */

package android.database;

public class Cursor {

    public boolean isClosed() {
        return false;
    }

    public void close() {
    }

    public boolean move(int offset) {
        return false;
    }

    public boolean moveToPosition(int position) {
        return false;
    }

    public boolean moveToFirst() {
        return false;
    }

    public boolean moveToLast() {
        return false;
    }

    public boolean moveToNext() {
        return false;
    }

    public int getCount() {
        return 0;
    }

    public boolean isAfterLast() {
        return true;
    }

    public String getString(int columnIndex) {
        return "";
    }

    public Short getShort(int columnIndex) {
        return 0;
    }

    public int getInt(int columnIndex) {
        return 0;
    }

    public long getLong(int columnIndex) {
        return 0;
    }

    public float getFloat(int columnIndex) {
        return 0;
    }

    public double getDouble(int columnIndex) {
        return 0;
    }

    public int getColumnIndex(String columnName) {
        return 0;
    }

    public int getColumnCount() {
        return 0;
    }

    public String getColumnName(int columnIndex) {
        return "";
    }

}
