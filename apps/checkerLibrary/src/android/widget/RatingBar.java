/**
 * Stub/model code simplified/modified from the Android library
 */

package android.widget;

import android.view.View;

public class RatingBar extends View {

    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public RatingBar() {
    }

    public RatingBar(int textId) {
    }

    private long rating = 0;

    public long getRating() {
        return rating;
    }

    public void setRating(long r) {
        rating = r;
    }

    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
    }

    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return mOnRatingBarChangeListener;
    }

    public interface OnRatingBarChangeListener {
        void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser);
    }
}
