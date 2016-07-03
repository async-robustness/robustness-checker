/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.os.Bundle;

public abstract class BroadcastReceiver {
    private PendingResult mPendingResult;
    private boolean mDebugUnregister;

    public static class PendingResult {
        public static final int TYPE_COMPONENT = 0;
        public static final int TYPE_REGISTERED = 1;
        public static final int TYPE_UNREGISTERED = 2;

        int mResultCode;
        String mResultData;
        Bundle mResultExtras;
        boolean mAbortBroadcast;
        boolean mFinished;

        public final void setResultCode(int code) {
            mResultCode = code;
        }

        public final int getResultCode() {
            return mResultCode;
        }

        public final void setResultData(String data) {
            mResultData = data;
        }

        public final String getResultData() {
            return mResultData;
        }

        public final void setResultExtras(Bundle extras) {
        }

        public final Bundle getResultExtras(boolean makeMap) {
            Bundle e = mResultExtras;
            if (!makeMap) return e;
            if (e == null) mResultExtras = e = new Bundle();
            return e;
        }

        public final void setResult(int code, String data, Bundle extras) {
            mResultCode = code;
            mResultData = data;
            mResultExtras = extras;
        }

        public final boolean getAbortBroadcast() {
            return mAbortBroadcast;
        }

        public final void abortBroadcast() {
            mAbortBroadcast = true;
        }

        public final void clearAbortBroadcast() {
            mAbortBroadcast = false;
        }

        public final void finish() {
        }

        public void setExtrasClassLoader(ClassLoader cl) {

        }
    }

    public BroadcastReceiver() {
    }

    public abstract void onReceive(Context context, Intent intent);

    public final PendingResult goAsync() {
        PendingResult res = mPendingResult;
        mPendingResult = null;
        return res;
    }

    /*public IBinder peekService(Context myContext, Intent service) {
        IActivityManager am = ActivityManagerNative.getDefault();
        IBinder binder = null;
        try {
            service.prepareToLeaveProcess();
            binder = am.peekService(service, service.resolveTypeIfNeeded(
                    myContext.getContentResolver()));
        } catch (RemoteException e) {
        }
        return binder;
    }*/

    public final void setResultCode(int code) {
        mPendingResult.mResultCode = code;
    }

    public final int getResultCode() {
        return mPendingResult != null ? mPendingResult.mResultCode : 0;
    }

    public final void setResultData(String data) {
        mPendingResult.mResultData = data;
    }

    public final String getResultData() {
        return mPendingResult != null ? mPendingResult.mResultData : null;
    }

    public final void setResultExtras(Bundle extras) {
        mPendingResult.mResultExtras = extras;
    }

    public final Bundle getResultExtras(boolean makeMap) {
        if (mPendingResult == null) {
            return null;
        }
        Bundle e = mPendingResult.mResultExtras;
        if (!makeMap) return e;
        if (e == null) mPendingResult.mResultExtras = e = new Bundle();
        return e;
    }

    public final void setResult(int code, String data, Bundle extras) {
        mPendingResult.mResultCode = code;
        mPendingResult.mResultData = data;
        mPendingResult.mResultExtras = extras;
    }

    public final boolean getAbortBroadcast() {
        return mPendingResult != null ? mPendingResult.mAbortBroadcast : false;
    }

    public final void abortBroadcast() {
        mPendingResult.mAbortBroadcast = true;
    }

    public final void clearAbortBroadcast() {
        if (mPendingResult != null) {
            mPendingResult.mAbortBroadcast = false;
        }
    }

    /*public final boolean isOrderedBroadcast() {
        return mPendingResult != null ? mPendingResult.mOrderedHint : false;
    }*/

    public final boolean isInitialStickyBroadcast() {
        return false;
    }

    public final void setOrderedHint(boolean isOrdered) {
        // Accidentally left in the SDK.
    }

    public final void setPendingResult(PendingResult result) {
        mPendingResult = result;
    }

    public final PendingResult getPendingResult() {
        return mPendingResult;
    }
    
    /*public int getSendingUserId() {
        return mPendingResult.mSendingUser;
    }*/

    public final void setDebugUnregister(boolean debug) {
        mDebugUnregister = debug;
    }

    public final boolean getDebugUnregister() {
        return mDebugUnregister;
    }

}

