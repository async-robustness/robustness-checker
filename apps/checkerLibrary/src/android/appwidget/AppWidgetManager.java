/**
 * Stub/model code simplified/modified from the Android library
 */

package android.appwidget;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;

public class AppWidgetManager {

    public static final String EXTRA_APPWIDGET_ID = "appWidgetId";
    public static final int INVALID_APPWIDGET_ID = 0;
    public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String OPTION_APPWIDGET_MIN_WIDTH = "appWidgetMinWidth";
    public static final String OPTION_APPWIDGET_MIN_HEIGHT = "appWidgetMinHeight";
    public static final String OPTION_APPWIDGET_MAX_WIDTH = "appWidgetMaxWidth";
    public static final String OPTION_APPWIDGET_MAX_HEIGHT = "appWidgetMaxHeight";

    private static AppWidgetManager instance;

    private Context mContext;

    private AppWidgetManager(Context c) {
        mContext = c;
    }

    public static AppWidgetManager getInstance(Context c) {
        if(instance == null) {
            instance = new AppWidgetManager(c);
        }
        return instance;
    }

    public int[] getAppWidgetIds(ComponentName componentName) {
        return new int[1];
    }

    public Bundle getAppWidgetOptions(int appWidgetId) {
        return new Bundle();
    }

    public void updateAppWidget(int appWidgetId, RemoteViews views) {
    }

    public void updateAppWidgetOptions(int appWidgetId, Bundle options) {
    }

    public void updateAppWidget(int[] appWidgetIds, RemoteViews views) {
    }

}
