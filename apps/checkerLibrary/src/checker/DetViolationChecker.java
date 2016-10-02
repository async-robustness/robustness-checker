/**
 * Robustness Violation Checker Library
 */

package checker;

import gov.nasa.jpf.vm.Verify;

public class DetViolationChecker extends BaseViolationChecker {

    // keeps the pivot access that delays and gets executed at a later point in the execution
    protected FieldAccess pivotAccess = null;
    protected FieldAccess exitAccess = null;

    // the error flag is set to true if guessed exit conflicts with the pivot
    protected boolean error = false;

    protected boolean inEvent = false;

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
    public void beforeAsyncProc(ProcMode mode) {
        if (!inEvent) return;

        asyncInvocations.push(currentAsync.copy());
        currentAsync = new AsyncInvocationData(mode);

        if (currentAsync.mode == ProcMode.ASYNCMain) {
            boolean skipProc = Verify.getBoolean(); // skipProc?
            if (skipProc) {
                throw new SkipException(); // skip the whole procedure
            }
        }
    }

    public void afterAsyncProc() {
        if (!inEvent) return;

        if (currentAsync.mode == ProcMode.ASYNCMain) {
            // The executions on the main thread (no matter in SYNC or ASYNC) which conflict with the skipping task's accesses
            // Are ignored immediately in updateGlobalRWSets, so the next line is commented out
            //Verify.ignoreIf(skipMainSet && conflictDetected);
            nondetGoToPivot();
        }

        // update the current async invocation as the caller
        if(!asyncInvocations.empty())
            currentAsync = asyncInvocations.pop();
        else
            currentAsync = null;
    }

    public void onStatement(String accessType, String objId, String className, String fieldName, String methodName) {
        if (!inEvent) return;

        FieldAccess currentAccess = new FieldAccess(accessType, objId, className, fieldName, methodName);

        if (currentAsync.mode == ProcMode.ASYNCMain) { // can skip or be pivot only in ASYNC

            if (!skipMainSet) {
                boolean skip = Verify.getBoolean();

                if (skip) {
                    skipMainSet = true;
                    // write to the global rd/wr sets: - executed once
                    rdSetGlobal = currentAsync.rdSetProc; // not null since in ProcMode.ASYNCMain
                    wrSetGlobal = currentAsync.wrSetProc; // not null since in ProcMode.ASYNCMain

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
                currentAsync.rdSetProc.add(currentAccess);
            } else {
                currentAsync.wrSetProc.add(currentAccess);
            }

        } else if (currentAsync.mode == ProcMode.ASYNCBack) { // can skip or be pivot only in ASYNC

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

            // access is not skipped, update rd/wr sets with the new access info:
            if (accessType.equals("GET")) {
                //currentAsync.rdSetProc.add(currentAccess);
                currentAsync.rdSetProc.addAll(currentAsync.rdSetProc);
            } else {
                currentAsync.wrSetProc.addAll(currentAsync.wrSetProc);
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
 *        Checker.beforeAsyncProc(ProcMode.ASYNCBack);
 *        runnable.run();  // this runnable originally runs in the background
 *    } catch (SkipException e) {
 *    } finally {
 *       Checker.afterAsyncProc();
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

