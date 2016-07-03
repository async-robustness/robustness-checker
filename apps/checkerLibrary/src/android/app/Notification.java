/**
 * Stub/model code simplified/modified from the Android library
 */

package android.app;

import android.widget.RemoteViews;

public class Notification {

    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_PRIVATE = 0;

    public RemoteViews bigContentView;

    public static final class Builder {
        private final int mIcon;
        private final CharSequence mTitle;
        private final PendingIntent mIntent;

        public Builder(int icon, CharSequence title, PendingIntent intent) {
            mIcon = icon;
            mTitle = title;
            mIntent = intent;
        }

        public Builder(Service s) {
            this(0, "", new PendingIntent());
        }

        public Builder setContent(RemoteViews views) {
            return this;
        }

        public Builder setSmallIcon(int icon) {
            return this;
        }

        public Builder setSmallIcon(int icon, int level) {
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            return this;
        }

        public Builder setContentText(CharSequence text) {
            return this;
        }

        public Builder setSubText(CharSequence text) {
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            return this;
        }

        public Builder setPriority(int pri) {
            return this;
        }

        public Builder setVisibility(int visibility) {
            return this;
        }

        // returns Action in the API
        public Notification build() {
            return new Notification();
        }
    }
}
