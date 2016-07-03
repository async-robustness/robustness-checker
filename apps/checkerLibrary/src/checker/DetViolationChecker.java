/**
 * Robustness Violation Checker Library
 */

package checker;

import gov.nasa.jpf.vm.Verify;

import java.util.HashSet;
import java.util.Set;

public class DetViolationChecker extends BaseViolationChecker {

    // keeps the pivot access that delays and gets executed at a later point in the execution
    private FieldAccess pivotAccess = null;
    private FieldAccess exitAccess = null;

    // the error flag is set to true if guessed exit conflicts with the pivot
    private boolean error = false;

    private boolean inEvent = false; // checks or prints r/w inside an event

    /* Used to check the accesses of an async proc skipped in the main thread */
    private boolean skipMainSet = false;
    private boolean conflictDetected = false; // async proc local
    private Set rdSetGlobal;
    private Set wrSetGlobal;
    private Set rdSetProc;
    private Set wrSetProc;

    public void beforeEvent() {
        // start monitoring r/w accesses of the event
        inEvent = true;

        pivotAccess = null;
        exitAccess = null;
    }

    public void afterEvent() {
        // stop monitoring r/w accesses of the event
        inEvent = false;

        if (error) {
            System.out.println("----------- Violation detected: -----------");
            System.out.println("Pivot access:   " + pivotAccess.toString());
            System.out.println("Cycle access:   " + exitAccess.toString());

            assert (false);
        }
    }

    // called before any async procedure
    public void beforeAsyncProc() {
        if (!inEvent) return;

        conflictDetected = false; // async proc local

        if (PROC_MODE == ProcMode.ASYNCMain) {
            boolean skipProc = Verify.getBoolean(); // skipProc?
            if (skipProc) {
                throw new SkipException(); // skip the whole procedure
            }

            // accumulate the r/w accesses of the async proc running on the main thread
            rdSetProc = new HashSet();
            wrSetProc = new HashSet();
        }
    }

    public void afterAsyncProc() {
        if (!inEvent) return;

        if (PROC_MODE == ProcMode.ASYNCMain) {
            // The executions on the main thread (no matter in SYNC or ASYNC) which conflict with the skipping task's accesses
            // Are ignored immediately in updateGlobalRWSets, so the next line is commented out
            //Verify.ignoreIf(skipMainSet && conflictDetected);
            nondetGoToPivot();
        }

        // Need to reset here!! Otherwise it is set in the sync main thread as well!!
        conflictDetected = false;
    }

    public void onStatement(String accessType, String objId, String className, String fieldName, String methodName) {
        if (!inEvent) return;

        FieldAccess currentAccess = new FieldAccess(accessType, objId, className, fieldName, methodName);

        if (PROC_MODE == ProcMode.ASYNCMain) { // can skip or be pivot only in ASYNC
            if (!skipMainSet) {
                boolean skip = Verify.getBoolean();

                if (skip) {
                    skipMainSet = true;
                    // write to the global rd/wr sets: - executed once
                    rdSetGlobal = rdSetProc;
                    wrSetGlobal = wrSetProc;

                    if (pivotAccess == null) {  // this might be the pivot stmt if it is not set yet
                        boolean isDelaying = Verify.getBoolean();
                        // just skip the stmt
                        if (isDelaying) {
                            pivotAccess = currentAccess;

                            // Hint processing: Constrain the search if the user provided hints in the driver file
                            // HP1: check for conflicts where the pivot access field name is equal to hintPivotField
                            if (hintPivotField != null)
                                Verify.ignoreIf(!pivotAccess.getFieldName().equals(hintPivotField));

                            throw new SkipException();
                        }
                    }

                    // not delaying, just skips
                    throw new SkipException();
                }
            }

            // access is not skipped, update rd/wr sets with the new access info:
            if (accessType.equals("GET")) {
                rdSetProc.add(currentAccess);
            } else {
                wrSetProc.add(currentAccess);
            }

        } else if (PROC_MODE == ProcMode.ASYNCBack) { // can skip or be pivot only in ASYNC

            boolean skip = Verify.getBoolean(); // skip?

            if (skip && pivotAccess == null) {
                boolean isDelaying = Verify.getBoolean();

                // just skip the stmt
                if (!isDelaying) {
                    throw new SkipException();

                    // delay the stmt - this access will form a cycle in the conflict graph
                } else {
                    pivotAccess = currentAccess;

                    // Hint processing: Constrain the search if the user provided hints in the driver file
                    // HP1: check for conflicts where the pivot access field name is equal to hintPivotField
                    if (hintPivotField != null)
                        Verify.ignoreIf(!pivotAccess.getFieldName().equals(hintPivotField));

                    throw new SkipException();
                }
            }
        }

        // check if the current access conflicts with the global r/w sets
        updateGlobalRWSets(currentAccess);

        // nondet guess this access as the exit access (can be set once in an event)
        if (pivotAccess != null && exitAccess == null) {

            boolean exit = Verify.getBoolean();
            if (exit) {
                exitAccess = currentAccess;
            }
        }

        nondetGoToPivot();
    }

    private void updateGlobalRWSets(FieldAccess currentAccess) {

        if (skipMainSet && !conflictDetected) {
            if (currentAccess.getAccessType().equals("GET") && wrSetGlobal.contains(currentAccess)) {
                conflictDetected = true;
                // If the conflicting access is in the main thread, ignore this execution (not valid)
                Verify.ignoreIf(PROC_MODE == ProcMode.ASYNCMain || PROC_MODE == ProcMode.SYNCMain);
            } else if (currentAccess.getAccessType().equals("PUT") && (rdSetGlobal.contains(currentAccess) || wrSetGlobal.contains(currentAccess))) {
                conflictDetected = true;
                // If the conflicting access is in the main thread, ignore this execution (not valid)
                Verify.ignoreIf(PROC_MODE == ProcMode.ASYNCMain || PROC_MODE == ProcMode.SYNCMain);
            }
        }
        // If this access or a previous access in this async proc conflicts, update the sets with the accesses in the proc
        if (conflictDetected) {
            if (currentAccess.getAccessType().equals("GET")) {
                rdSetGlobal.add(currentAccess);
            } else {
                wrSetGlobal.add(currentAccess);
            }
        }
    }

    private void nondetGoToPivot() {
        // nondet goto pivot and validate violation
        if (pivotAccess != null && exitAccess != null) {

            boolean gotoPivotAccess = Verify.getBoolean();
            if (gotoPivotAccess) {
                if (checkConflict(exitAccess, pivotAccess)) {
                    // The violation will be printed at the end of the event
                    error = true;
                }
            }
        }
    }
}

/**
 *  Insert the instrumentation statement onStatement(...) before each read/write stmt
 * (Inserted automatically by the instrumentor)
 *
 * Surround the originally asynchronous procedures with try/catch block as follows
 * To simulate their concurrent behavior
 * (See AsyncTask.java for posting doInBackground and onPostExecute methods as examples)
 *
 * public void onClick() {
 *    Checker.onStatement(...);
 *    stmt1;
 *
 *    Checker.onStatement(...);
 *    stmt2;
 *
 *    try {
 *        Checker.setProcMode(ProcMode.ASYNCBack);
 *        Checker.beforeAsyncProc();
 *        runnable.run();  // this runnable originally runs in the background
 *    } catch (SkipException e) {
 *    } finally {
 *       Checker.afterAsyncProc();
 *       // continue synchronously
 *       Checker.setProcMode(ProcMode.SYNCMain);
 *    }
 *
 *    ...
 * }
 *
 *
 *  public static void main (String[] args) {
 *
 *      // initialize app and an activity
 *      // retrieve UI element
 *
 *      Checker.setCheckerMode("DET");
 *
 *      // Check whether an event is deterministic:
 *      Checker.beforeEvent();
 *      button.onClick();
 *      Checker.afterEvent();
 *  }
 *
 **/

