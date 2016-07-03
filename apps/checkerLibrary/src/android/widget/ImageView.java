/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

public class ImageView extends View {

    public static ImageView DEFAULT_IMAGEVIEW;
    public int imageResource;

    public ImageView() {
        if (DEFAULT_IMAGEVIEW == null) {
            DEFAULT_IMAGEVIEW = this;
        }
    }

    public ImageView(int resId) {
        if (DEFAULT_IMAGEVIEW == null) {
            DEFAULT_IMAGEVIEW = this;
        }
    }

    public ImageView(Context c) {
        if (DEFAULT_IMAGEVIEW == null) {
            DEFAULT_IMAGEVIEW = this;
        }
    }

    public void setImageURI(Uri uri) {
    }

    public void setImageBitmap(Bitmap bitmap) {
    }

    public void setImageDrawable(Object drawable) {
    }

    public void setImageResource(int resId) {
        imageResource = resId;
    }

    /*public Drawable getDrawable() {
        return new Drawable();
    }

    public void setImageDrawable(Drawable param0) {
    }*/

}