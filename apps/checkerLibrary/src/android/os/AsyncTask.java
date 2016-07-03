/*
 * Copyright (C) 2008 The Android Open Source Project
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

import checker.*;

public abstract class AsyncTask<Params, Progress, Result> {
    private static final String LOG_TAG = "AsyncTask";
    private volatile Status mStatus = Status.PENDING;
    private boolean mCancelled = false;

    /**
     * Indicates the current status of the task. Each status will be set only once
     * during the lifetime of a task.
     */
    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that {@link AsyncTask#onPostExecute} has finished.
         */
        FINISHED,
    }

    public AsyncTask() {

    }

    public final Status getStatus() {
        return mStatus;
    }

    protected abstract Result doInBackground(Params... params);

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    protected void onProgressUpdate(Progress... values) {
    }

    protected void onCancelled(Result result) {
        onCancelled();
    }    

    protected void onCancelled() {
    }

    public final boolean isCancelled() {
        return mCancelled;
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled = true;
        return true;
    }


    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(params);
    }

    private Result result = null;

    public final AsyncTask<Params, Progress, Result> executeOnExecutor(Params... params) {

        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;


        onPreExecute();

        // to check whether doInBackground has completed
        // do not check for null (some doInBackground implementations return null by design!)
        boolean doInBackgroundDone = false;

        try {
            // runs async on a background thread
            Checker.setProcMode(ProcMode.ASYNCBack);
            Checker.beforeAsyncProc(); // might skip the whole async proc // must be inside try
            result = doInBackground(params);
            doInBackgroundDone = true; // if skipped in the exploration, just explores more, does not harm

        } catch (SkipException e) {
            // Do not block the caller (catch the exception and continue after AsyncTask.execute)
        } finally {
            Checker.afterAsyncProc();
            Checker.setProcMode(ProcMode.SYNCMain);
        }

        // if doInBackground is not skipped, run onCancelled/onPostExecute
        if(doInBackgroundDone)
            finish(result);

        return this;
    }


    protected final void publishProgress(Progress... values) {
        //onProgressUpdate(values); // only UI purposes
    }

    private void finish(Result result) {
        if (isCancelled()) {

            //onCancelled(result);
            try {
                Checker.setProcMode(ProcMode.ASYNCMain);
                Checker.beforeAsyncProc(); // might skip the whole async proc
                onCancelled(result);

            } catch (SkipException e) {
                // Do not block the caller (catch the exception and continue after AsyncTask.execute)
            } finally {
                Checker.afterAsyncProc();
                Checker.setProcMode(ProcMode.SYNCMain);
            }


        } else {
            //onPostExecute(result);

            try {
                Checker.setProcMode(ProcMode.ASYNCMain);
                Checker.beforeAsyncProc(); // might skip the whole async proc
                onPostExecute(result);

            } catch (SkipException e) {
                // Do not block the caller (catch the exception and continue after AsyncTask.execute)
            } finally {
                Checker.afterAsyncProc();
                Checker.setProcMode(ProcMode.SYNCMain);
            }
        }
        mStatus = Status.FINISHED;
    }
}
