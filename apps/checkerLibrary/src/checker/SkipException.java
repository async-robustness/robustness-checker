/**
 * Robustness Violation Checker Library
 */

package checker;


public class SkipException extends RuntimeException {

}

/**
 * We use SkipExceptions:
 * To implement skipping/delaying of asynchronous procedures
 * (where an asynchronous procedure skips the rest of its execution)
 *
 * The invocation of asynchronous procedures are surrounded with try/catch
 * So that the async procedure execution skips and returns to its caller
 *
 * See the sample usage in AsyncTask class doInBackground and onPostExecute asynchronous procedures
 *
 *
 * Here is another example invocation of a runnable in an event handler procedure:
 * (The runnable was run on a fresh thread in the original app code.)
 *
 *    Runnable runnable = new Runnable() {
 *        public void run(){
 *            ...
 *        }
 *    };
 *    // new Thread(runnable).start(); // commented out and made synchronous
 *    try {
 *        Checker.setProcMode(ProcMode.ASYNCBack); // runs on a background thread
 *        Checker.beforeAsyncProc(); // might skip the whole async proc // must be inside try
 *        runnable.run();
 *
 *    } catch (SkipException e) {
 *        // Do not block the caller (catch the exception and continue)
 *
 *    } finally {
 *      Checker.afterAsyncProc();
 *      Checker.setProcMode(ProcMode.SYNCMain); // returns to the sync code in the main thread
 *    }
 *
 **/