/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content.res;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

public class Resources {

    private static Resources instance;

    public String getString(int id) throws NotFoundException {
        CharSequence res = getText(id);
        if (res != null) {
            return res.toString();
        }
        throw new NotFoundException("String resource ID #0x"
                + Integer.toHexString(id));
    }

    private Resources() {
    }

    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    public static Resources getSystem() {
        return getInstance();
    }

    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return "";
    }

    public String[] getStringArray(int id) throws NotFoundException {
        String[] s = {""};
        return s;
    }

    public String getQuantityString(int resId, int count) {
        return "";
    }

    public String getQuantityString(int id, int quantity, Object... formatArgs)
            throws NotFoundException {
        return "";
    }

    public int[] getIntArray(int id) throws NotFoundException {
        int[] i = {0};
        return i;
    }

    public boolean getBoolean(int resId) {
        return false;
    }

    public int getInteger(int resId) {
        return resId;
    }


    public CharSequence getText(int id) throws NotFoundException {
        return "";
    }

    public Drawable getDrawable(int resId) {
        return new Drawable();
    }

    public Drawable getDrawableForDensity(int i, int j) {
        return new Drawable();
    }

    public int getDimensionPixelSize(int i) {
        return 0;
    }

    public int getDimension(int resId) {
        return 0;
    }

    public int getIdentifier(String name, String defType, String defPackage) {
        return 0;
    }

    public int getColorStateList(int id) {
        return id;
    }

    public int getColor() {
        return 0;
    }

    public int getColor(int resId) {
        return 0;
    }

    public InputStream openRawResource(int resId) {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    public final AssetManager getAssets() {
        return new AssetManager();
    }

    public Configuration getConfiguration() {
        return new Configuration();
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
        }

        public NotFoundException(String name) {
            super(name);
        }
    }
}
