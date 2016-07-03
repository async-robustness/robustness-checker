package android.os;


public class HandlerThread extends Thread {

    Looper mLooper;

    public HandlerThread(String name) {
        mLooper = new Looper();
    }

    public HandlerThread(String name, int priority) {
        mLooper = new Looper();
    }

    protected void onLooperPrepared() {
    }

    @Override
    public void run() {
    }

    public Looper getLooper() {
        return mLooper;
    }

    public boolean quit() {
        return false;
    }

    public boolean quitSafely() {
        return false;
    }

    public int getThreadId() {
        return -1;
    }
}
