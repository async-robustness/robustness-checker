/*
 * Copyright (C) 2006 The Android Open Source Project
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

package android.os;

public class Handler {

    public interface Callback {
        public boolean handleMessage(Message msg);
    }
    
    /**
     * Subclasses must implement this to receive messages.
     */

    public void handleMessage(Message msg) {
    }

    public void dispatchMessage(Message msg) {
        handleMessage(msg);
    }
    public Handler() {
        this(null, false);
    }

    public Handler(Callback callback) {
        this(callback, false);
    }

    public Handler(Looper looper) {
        this(looper, null, false);
    }

    public Handler(Looper looper, Callback callback) {
        this(looper, callback, false);
    }
    public Handler(boolean async) {
        this(null, async);
    }

    public Handler(Callback callback, boolean async) {
    }

    public Handler(Looper looper, Callback callback, boolean async) {
    }

    public final Message obtainMessage()
    {
        return Message.obtain(this);
    }

    public final Message obtainMessage(int what)
    {
        return Message.obtain(this, what);
    }
    
    public final Message obtainMessage(int what, Object obj)
    {
        return Message.obtain(this, what, obj);
    }

    public final Message obtainMessage(int what, int arg1, int arg2)
    {
        return Message.obtain(this, what, arg1, arg2);
    }
    
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj)
    {
        return Message.obtain(this, what, arg1, arg2, obj);
    }

    /**
     * NOTE:
     * Runnables are executed synchronously by default
     * To simulate their async execution, the programmer should surround the call in his app with proper try/catch blocks
     * (See AsyncTask doInBackground and onPostExecute to see how to simulate an async proc)
     */
    public final boolean post(Runnable r)
    {
        r.run();
        return true;
    }

    public final boolean postAtTime(Runnable r, long uptimeMillis)
    {
        r.run();
        return true;
    }
    
    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis)
    {
        r.run();
        return true;
    }
    
    public final boolean postDelayed(Runnable r, long delayMillis)
    {
        r.run();
        return true;
    }
    
    public final boolean postAtFrontOfQueue(Runnable r)
    {
        r.run();
        return true;
    }

    public final void removeCallbacks(Runnable r)
    {

    }

    public final void removeCallbacks(Runnable r, Object token)
    {

    }

    public final boolean sendMessage(Message msg)
    {
        handleMessage(msg);
        return true;
    }

    public final boolean sendEmptyMessage(int what)
    {
        return false;
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return false;
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return false;
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis)
    {
        handleMessage(msg);
        return true;
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        handleMessage(msg);
        return true;
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        handleMessage(msg);
        return true;
    }

    public final void removeMessages(int what) {
    }

    public final void removeMessages(int what, Object object) {
    }

    public final void removeCallbacksAndMessages(Object token) {
    }

    public final boolean hasMessages(int what) {
        return false;
    }

    public final boolean hasMessages(int what, Object object) {
        return false;
    }

    public final boolean hasCallbacks(Runnable r) {
        return false;
    }

    public final Looper getLooper() {
        return new Looper();
    }

    @Override
    public String toString() {
        return "Handler (" + getClass().getName() + ") {"
        + Integer.toHexString(System.identityHashCode(this))
        + "}";
    }

}
