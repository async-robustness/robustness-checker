/**
 * Stub/model code simplified/modified from the Android library
 */

package android.os;

/**
 * Kept Looper class since it is used in the references etc in the Android apps
 * However, the mechanism is made synchronous
 * All messages are already handled synchronously, without going into the looper
 */

public final class Looper {

    public static Looper DEFAULT_LOOPER = new Looper();

    public Looper() {

    }

    public static void prepare() {
    }

    public static Looper getMainLooper() {
        return DEFAULT_LOOPER;
    }

    public static void loop() {
    }

    public static Looper myLooper() {
        return DEFAULT_LOOPER;
    }

    public void quit() {
    }

    public void quitSafely() {
    }

}
