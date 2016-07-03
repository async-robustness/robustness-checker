/**
 * Stub/model code simplified/modified from the Android library
 */

package android.graphics;

import java.io.InputStream;

public class BitmapFactory {

    public BitmapFactory() {
    }

    public static Bitmap decodeResource(android.content.res.Resources param0, int param1) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static Bitmap decodeStream(InputStream is) {
        return Bitmap.DEFAULT_BITMAP;
    }

    ///**(B) Modification to JPA's models (B)**//

    public static Bitmap decodeResource(android.content.res.Resources res, int id, Options opts) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static Bitmap decodeFile(String pathName, Options opts) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static Bitmap decodeStream(InputStream is, Object outPadding, Options opts) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static class Options {

        public Options() {
            inDither = false;
            inScaled = true;
            inPremultiplied = true;
        }

        public Bitmap inBitmap;

        public boolean inJustDecodeBounds;

        public final Bitmap.Config inPreferredConfig = Bitmap.Config.ARGB_8888;

        public boolean inPremultiplied;

        public boolean inDither;

        public int inDensity;

        public int inTargetDensity;

        public int inScreenDensity;

        public boolean inScaled;

        public boolean inPurgeable;

        public boolean inInputShareable;

        public boolean inPreferQualityOverSpeed;

        public int outWidth;

        public int outHeight;

        public String outMimeType;

        public byte[] inTempStorage;

        private native void requestCancel();

        public boolean mCancel;

        public void requestCancelDecode() {
            mCancel = true;
            requestCancel();
        }
    }

    ///**(B) Modification to JPA's models (B)**//
}