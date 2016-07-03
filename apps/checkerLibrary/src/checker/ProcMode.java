/**
 * Robustness Violation Checker Library
 */

package checker;


/**
 * Set in the beginning/end of the originally asynchronous procedure calls
 * Initially, the execution is synchronous in the main thread "SYNCMain"
 * (e.g. in AsyncTask, PROC_MODE is set to "ASYNCBack" before "doInBackground" and "ASYNCMain" before "onPostExecute")
 * To simulate the asynchronous behavior of a procedure in an app,
 * the programmer marks the asynchronous procedures with ProcMode inside try/catch blocks.
 * (see the example usage in AsyncTask)
 */
public enum ProcMode {
    // currently analyzed procedure originally:
    SYNCMain,   // runs synchronously on the main thread
    ASYNCBack,  // runs asynchronously on a background thread
    ASYNCMain   // runs asynchronosly on the main thread
};
