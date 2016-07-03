/**
 * Stub/model code simplified/modified from the Android library
 */

package android.graphics;

import java.io.OutputStream;

public final class Bitmap {

    public enum CompressFormat {
        JPEG(0), PNG(1), WEBP(2);

        CompressFormat(int nativeInt) {
        }
    }

    public static final Bitmap DEFAULT_BITMAP = new Bitmap();

    public Bitmap() {
    }

    public int getHeight() {
        return 100;
    }

    public int getWidth() {
        return 100;
    }

    public static Bitmap createScaledBitmap(Bitmap source, int scaledWidth, int scaledHeight, boolean b) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        return Bitmap.DEFAULT_BITMAP;
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        return false;
    }

    public enum Config {
        ALPHA_8(2),
        RGB_565(4),
        ARGB_4444(5),
        ARGB_8888(6);
        int i;

        Config(int i) {
            /*this.i = i;*/
        }
    }

}