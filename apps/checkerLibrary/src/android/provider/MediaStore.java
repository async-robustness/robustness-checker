/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.provider;

import android.net.Uri;


/**
 * The Media provider contains meta data for all available media on both internal
 * and external storage devices.
 */
public final class MediaStore {
    private final static String TAG = "MediaStore";

    public static final String AUTHORITY = "media";

    private static final String CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + "/";


    public static final String INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH";
    public static final String INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH =
            "android.media.action.MEDIA_PLAY_FROM_SEARCH";
    public static final String INTENT_ACTION_TEXT_OPEN_FROM_SEARCH =
            "android.media.action.TEXT_OPEN_FROM_SEARCH";
    public static final String INTENT_ACTION_VIDEO_PLAY_FROM_SEARCH =
            "android.media.action.VIDEO_PLAY_FROM_SEARCH";


    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";
    public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";
    public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";
    public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";
    public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE =
            "android.media.action.STILL_IMAGE_CAMERA_SECURE";
    public static final String INTENT_ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA";
    public final static String ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String ACTION_IMAGE_CAPTURE_SECURE =
            "android.media.action.IMAGE_CAPTURE_SECURE";
    public final static String ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
    public final static String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";
    public final static String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";
    public final static String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";
    public final static String EXTRA_OUTPUT = "output";
    public static final String UNKNOWN_STRING = "<unknown>";


    public interface MediaColumns {
        public static final String DATA = "_data";
        public static final String SIZE = "_size";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String TITLE = "title";
        public static final String DATE_ADDED = "date_added";
        public static final String DATE_MODIFIED = "date_modified";
        public static final String MIME_TYPE = "mime_type";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
    }

    private static class InternalThumbnails {
        private static final int MINI_KIND = 1;
        private static final int FULL_SCREEN_KIND = 2;
        private static final int MICRO_KIND = 3;
        //private static final String[] PROJECTION = new String[] {_ID, MediaColumns.DATA};
        static final int DEFAULT_GROUP_ID = 0;
        private static final Object sThumbBufLock = new Object();
        private static byte[] sThumbBuf;
    }

    public static final class Images {
        public interface ImageColumns extends MediaColumns {
            public static final String DESCRIPTION = "description";
            public static final String PICASA_ID = "picasa_id";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE_TAKEN = "datetaken";
            public static final String ORIENTATION = "orientation";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String BUCKET_ID = "bucket_id";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
        }

        public static final class Media implements ImageColumns {

            public static Uri getContentUri(String volumeName) {
                return new Uri();
            }

            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";
            public static final String DEFAULT_SORT_ORDER = ImageColumns.BUCKET_DISPLAY_NAME;
        }


        public static class Thumbnails {
            public static final String DEFAULT_SORT_ORDER = "image_id ASC";
            public static final String DATA = "_data";
            public static final String IMAGE_ID = "image_id";
            public static final String KIND = "kind";
            public static final int MINI_KIND = 1;
            public static final int FULL_SCREEN_KIND = 2;
            public static final int MICRO_KIND = 3;
            public static final String THUMB_DATA = "thumb_data";
            public static final String WIDTH = "width";
            public static final String HEIGHT = "height";
        }
    }


    public static final class Audio {

        public interface AudioColumns extends MediaColumns {
            public static final String TITLE_KEY = "title_key";
            public static final String DURATION = "duration";
            public static final String BOOKMARK = "bookmark";
            public static final String ARTIST_ID = "artist_id";
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String COMPOSER = "composer";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM = "album";
            public static final String ALBUM_KEY = "album_key";
            public static final String TRACK = "track";
            public static final String YEAR = "year";
            public static final String IS_MUSIC = "is_music";
            public static final String IS_PODCAST = "is_podcast";
            public static final String IS_RINGTONE = "is_ringtone";
            public static final String IS_ALARM = "is_alarm";
            public static final String IS_NOTIFICATION = "is_notification";
        }


        public static final class Media implements AudioColumns {

            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
            public static final String DEFAULT_SORT_ORDER = TITLE_KEY;
            public static final String RECORD_SOUND_ACTION =
                    "android.provider.MediaStore.RECORD_SOUND";
            public static final String EXTRA_MAX_BYTES =
                    "android.provider.MediaStore.extra.MAX_BYTES";
        }

        public interface GenresColumns {
            public static final String NAME = "name";
        }

        public static final class Genres {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
            //public static final String DEFAULT_SORT_ORDER = NAME;

            public static final class Members implements AudioColumns {

                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = TITLE_KEY;
                public static final String AUDIO_ID = "audio_id";
                public static final String GENRE_ID = "genre_id";
            }
        }

        public interface PlaylistsColumns {
            public static final String NAME = "name";
            public static final String DATA = "_data";
            public static final String DATE_ADDED = "date_added";
            public static final String DATE_MODIFIED = "date_modified";
        }

        public static final class Playlists {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
            // public static final String DEFAULT_SORT_ORDER = NAME;


            public static final class Members implements AudioColumns {
                public static final String _ID = "_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String AUDIO_ID = "audio_id";
                public static final String PLAYLIST_ID = "playlist_id";
                public static final String PLAY_ORDER = "play_order";
                public static final String DEFAULT_SORT_ORDER = PLAY_ORDER;
            }
        }

        public interface ArtistColumns {
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String NUMBER_OF_ALBUMS = "number_of_albums";
            public static final String NUMBER_OF_TRACKS = "number_of_tracks";
        }

        public static final class Artists {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
            //public static final String DEFAULT_SORT_ORDER = ARTIST_KEY;
        }

        public interface AlbumColumns {
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM = "album";
            public static final String ARTIST = "artist";
            public static final String NUMBER_OF_SONGS = "numsongs";
            public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";
            public static final String FIRST_YEAR = "minyear";
            public static final String LAST_YEAR = "maxyear";
            public static final String ALBUM_KEY = "album_key";
            public static final String ALBUM_ART = "album_art";
        }

        public static final class Albums {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
            //public static final String DEFAULT_SORT_ORDER = ALBUM_KEY;
        }
    }

    public static final class Video {
        public static final String DEFAULT_SORT_ORDER = MediaColumns.DISPLAY_NAME;

        public interface VideoColumns extends MediaColumns {
            public static final String DURATION = "duration";
            public static final String ARTIST = "artist";
            public static final String ALBUM = "album";
            public static final String RESOLUTION = "resolution";
            public static final String DESCRIPTION = "description";
            public static final String IS_PRIVATE = "isprivate";
            public static final String TAGS = "tags";
            public static final String CATEGORY = "category";
            public static final String LANGUAGE = "language";
            public static final String LONGITUDE = "longitude";
            public static final String DATE_TAKEN = "datetaken";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String BUCKET_ID = "bucket_id";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BOOKMARK = "bookmark";
        }

        public static final class Media implements VideoColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
            public static final String DEFAULT_SORT_ORDER = TITLE;
        }

        public static class Thumbnails {
            public static final String DEFAULT_SORT_ORDER = "video_id ASC";
            public static final String DATA = "_data";
            public static final String VIDEO_ID = "video_id";
            public static final String KIND = "kind";
            public static final int MINI_KIND = 1;
            public static final int FULL_SCREEN_KIND = 2;
            public static final int MICRO_KIND = 3;
            public static final String WIDTH = "width";
            public static final String HEIGHT = "height";
        }
    }

    public static Uri getMediaScannerUri() {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + "none/media_scanner");
    }

    public static final String MEDIA_SCANNER_VOLUME = "volume";
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";


}
