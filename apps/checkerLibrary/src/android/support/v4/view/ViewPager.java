/**
 * Stub/model code simplified/modified from the Android library
 */

package android.support.v4.view;

import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;

public class ViewPager extends View {

    public void setAdapter(FragmentPagerAdapter a) {
    }

    public void setOffscreenPageLimit(int limit) {
    }

    public void setCurrentItem(int id) {
    }

    private OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public interface OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int state);
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // This space for rent
        }
        public void onPageSelected(int position) {
            // This space for rent
        }
        public void onPageScrollStateChanged(int state) {
            // This space for rent
        }
    }
}
