/**
 * Stub/model code simplified/modified from the Android library
 */

package android.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class BitmapDrawable extends Drawable {

    private Bitmap mBitmap;

    public BitmapDrawable(Resources res, Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

}
