
package com.vlille.checker.utils;

import android.view.View;

public final class ViewUtils {

	private ViewUtils() {}
	
	public static void switchView(View view, boolean show) {
        if (view != null) {
            if (show) {
                show(view);
            }
            else {
                hide(view);
            }
        }
	}
	
	public static void hide(View view) {

	}
	
	public static void show(View view) {

	}
}
