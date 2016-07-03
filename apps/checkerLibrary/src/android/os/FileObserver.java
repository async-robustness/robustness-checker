package android.os;

/**
 * Created by burcuozkan on 22/06/16.
 */
public abstract class FileObserver {
    /** Event type: Data was read from a file */
    public static final int ACCESS = 0x00000001;
    /** Event type: Data was written to a file */
    public static final int MODIFY = 0x00000002;
    /** Event type: Metadata (permissions, owner, timestamp) was changed explicitly */
    public static final int ATTRIB = 0x00000004;
    /** Event type: Someone had a file or directory open for writing, and closed it */
    public static final int CLOSE_WRITE = 0x00000008;
    /** Event type: Someone had a file or directory open read-only, and closed it */
    public static final int CLOSE_NOWRITE = 0x00000010;
    /** Event type: A file or directory was opened */
    public static final int OPEN = 0x00000020;
    /** Event type: A file or subdirectory was moved from the monitored directory */
    public static final int MOVED_FROM = 0x00000040;
    /** Event type: A file or subdirectory was moved to the monitored directory */
    public static final int MOVED_TO = 0x00000080;
    /** Event type: A new file or subdirectory was created under the monitored directory */
    public static final int CREATE = 0x00000100;
    /** Event type: A file was deleted from the monitored directory */
    public static final int DELETE = 0x00000200;
    /** Event type: The monitored file or directory was deleted; monitoring effectively stops */
    public static final int DELETE_SELF = 0x00000400;
    /** Event type: The monitored file or directory was moved; monitoring continues */
    public static final int MOVE_SELF = 0x00000800;

    /** Event mask: All valid event types, combined */
    public static final int ALL_EVENTS = ACCESS | MODIFY | ATTRIB | CLOSE_WRITE
            | CLOSE_NOWRITE | OPEN | MOVED_FROM | MOVED_TO | DELETE | CREATE
            | DELETE_SELF | MOVE_SELF;

    public FileObserver(String path) {
        this(path, ALL_EVENTS);
    }

    public FileObserver(String path, int mask) {
    }

    public void startWatching() {

    }

    public void stopWatching() {
    }

    public abstract void onEvent(int event, String path);
}
