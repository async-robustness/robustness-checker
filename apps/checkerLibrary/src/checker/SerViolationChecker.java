/**
 * Robustness Violation Checker Library
 */

package checker;

import gov.nasa.jpf.vm.Verify;

import java.util.HashSet;
import java.util.Set;

public class SerViolationChecker extends BaseViolationChecker {

    // keeps the pivot access that delays and gets executed at a later point in the execution
    private FieldAccess pivotAccess = null;
    // keeps the exit value that conflicts with the pivot which sets error=true
    // (the value kept in exitAccess might be overwriten after setting error=true)
    private FieldAccess cycleAccess = null;
    // the event which guesses "cycleAccess" to conflict with "pivotAccess"
    private int cycleAccessIn = 0;

    private FieldAccess exitAccess = null;
    private FieldAccess tmpExitAccess = null;

    // booleans for [stmt]_shared
    private boolean firstSet = false;
    private boolean pivotAccessSet = false;
    public boolean pivotGoneToDone = false;

    // booleans for [stmt]_event
    // whether the current event guessed its exit
    private boolean exitSetInEvent = false;
    // whether the current event is validated to be in the conflict graph
    private boolean entrySetInEvent = false;

    // to prevent dependency inside the same event
    private int curEventID = 0;
    // the last event which guesses "exitAccess"
    private int exitAccessIn = 0;
    private int tmpExitAccessIn = 0;

    /* The following variables are kept to print the cycle info */
    // the last event is dependent to the previous event by exitFrom and entryTo accesses
    private FieldAccess exitFrom = null;
    private FieldAccess entryTo = null;
    private int firstEventID = 0;
    private int pivotAccessSetInEventID = 0;

    // the error flag is set to true if guessed exit conflicts with the pivot
    private boolean error = false;

    /* Used to check the accesses of an async proc skipped in the main thread */
    private boolean skipMainSet = false;
    private boolean conflictDetected = false; // async proc local
    private Set rdSetGlobal;
    private Set wrSetGlobal;
    private Set rdSetProc;
    private Set wrSetProc;


    public void beforeEvent() {
        PROC_MODE = ProcMode.SYNCMain;

        curEventID++;

        // reset event local booleans for [stmt]_event
        entrySetInEvent = false;
        exitSetInEvent = false;

        if (!firstSet) {  // if the first event is not set yet
            boolean first = Verify.getBoolean();
            if (first) {
                firstSet = true;
                firstEventID = curEventID;
                // the first event is in the conflict graph
                entrySetInEvent = true;
            }
        }
    }

    public void afterEvent() {
        // Each event is guaranteed to either:
        // - set both its entry and exit to the cycle (the event is IN the cycle)
        // - or, set none of them (the event is not included in the cycle)
        // assume (entrySetInEvent == exitSetInEvent)
        Verify.ignoreIf(entrySetInEvent != exitSetInEvent);


        // The error is set when the exit and the pivot conflicts
        // It is a true violation if the event that set exit is validated to be in the cycle
        // (possibly after it has set the pivot)
        // Check if the event that set exit is in the cycle:
        // If the exit is set
        // - In the current event: the entry and exit are set in this event (entryTo!=null) && (exitFrom != null)
        // - In a previous event (where (entrySetInEvent == exitSetInEvent)): exitSetIn != currentEventId
        if (error && entryTo != null && exitFrom != null && ((entrySetInEvent && firstEventID != curEventID) || (curEventID != cycleAccessIn))) {
            System.out.println("----------- Violation detected: -----------");
            System.out.println("Pivot access:   " + pivotAccess.toString());
            System.out.println("Cycle access:   " + cycleAccess.toString());
            System.out.println("Dependent by Entry  :   " + entryTo.toString());
            System.out.println("Dependent by exit  :   " + exitFrom.toString());
            System.out.println("First event in the graph:   " + firstEventID);
            System.out.println("Pivot access in:   " + pivotAccessSetInEventID);
            System.out.println("Cycle access in: " + cycleAccessIn);

            assert (false);
        }
    }

    // called before any async procedure
    public void beforeAsyncProc() {
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
        FieldAccess currentAccess = new FieldAccess(accessType, objId, className, fieldName, methodName);

        if (PROC_MODE == ProcMode.ASYNCMain) {
            if (!skipMainSet) {
                boolean skip = Verify.getBoolean();

                if (skip) {
                    skipMainSet = true;
                    // write to the global rd/wr sets: - executed once
                    rdSetGlobal = rdSetProc;
                    wrSetGlobal = wrSetProc;

                    if (!pivotAccessSet && firstSet) {  // this might be the pivot stmt if it is not set yet
                        boolean isDelaying = Verify.getBoolean();
                        // just skip the stmt
                        if (isDelaying) {
                            pivotAccess = currentAccess;
                            pivotAccessSet = true;
                            pivotAccessSetInEventID = curEventID;

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

            // the access is either inside an async procedure running in the backg thread
            // or sync procedure running in the main thread (does not set pivot but nondeterministically goes to)
        } else if (PROC_MODE == ProcMode.ASYNCBack) { // can skip or be pivot only in ASYNC

            boolean skip = Verify.getBoolean(); // skip?

            if (skip && !pivotAccessSet && firstSet) { //added first set: otherwise, first:3, pivot in 1

                boolean isDelaying = Verify.getBoolean();

                // just skip the stmt
                if (!isDelaying) {
                    throw new SkipException();

                    // delay the stmt - this access will form a cycle in the conflict graph
                } else {
                    pivotAccess = currentAccess;
                    pivotAccessSet = true;
                    pivotAccessSetInEventID = curEventID;

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

        // nondet guess exit, nondet validate entry
        checkWitness(currentAccess);

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

    /**
     * guess entry/exit
     **/
    private void checkWitness(FieldAccess currentAccess) {
        /********************* Witnessing event entry/exit points   *******************/
        // nondet guess this access as the exit access (executed once in an event)
        if (firstSet && !exitSetInEvent) {

            boolean exit = Verify.getBoolean();
            if (exit) {
                if (entrySetInEvent) {
                    exitAccess = tmpExitAccess = currentAccess;
                    exitAccessIn = tmpExitAccessIn = curEventID;

                    isRootInSync = isTmpRootInSync = (PROC_MODE == ProcMode.SYNCMain); // for hint processing HP2
                    isRootInAsyncMain = isTmpRootInAsyncMain = (PROC_MODE == ProcMode.ASYNCMain); // for hint processing HP3

                } else {
                    tmpExitAccess = currentAccess;
                    tmpExitAccessIn = curEventID;

                    isTmpRootInSync = (PROC_MODE == ProcMode.SYNCMain); // for hint processing HP2
                    isTmpRootInAsyncMain = (PROC_MODE == ProcMode.ASYNCMain); // for hint processing HP3
                }
                exitSetInEvent = true;
            }
        }

        // if the current exitAccess is set by a prev event, nondet guess the entry access
        if (exitAccessIn != curEventID && exitAccess != null && !entrySetInEvent) {

            boolean entry = Verify.getBoolean();
            if (entry) {
                if (checkConflict(currentAccess, exitAccess)) {  //validate entry of the current event
                    // kept just to print info:
                    entryTo = currentAccess;
                    exitFrom = exitAccess;

                    // the current event is validated to be in the cycle
                    exitAccess = tmpExitAccess;
                    exitAccessIn = tmpExitAccessIn;
                    tmpExitAccess = null;
                    entrySetInEvent = true;

                    isRootInSync = isTmpRootInSync; // for hint processing HP2
                    isRootInAsyncMain = isTmpRootInAsyncMain; // for hint processing HP3
                }
            }
        }
    }

    private void nondetGoToPivot() {
        // nondet goto pivot and validate violation
        // need to check pivotAccessSetInEventID != exitAccessIn:
        // (e.g. pivot might be set in an async proc of an event that skips, but also the sync part of an event chooses an exit (to form dependency with the next event))
        if (pivotAccessSet && exitAccess != null && pivotAccessSetInEventID < exitAccessIn && !pivotGoneToDone) {
            boolean gotoPivotAccess = Verify.getBoolean();
            if (gotoPivotAccess) {

                if (checkConflict(exitAccess, pivotAccess)) {
                    // The conflict is a true violation if the event that set the exit is validated to be in the cycle
                    // At the end of the event, check if the event that set exit is in the cycle
                    error = true;
                    cycleAccess = exitAccess;
                    cycleAccessIn = exitAccessIn;  // cycle access is the saved exit access
                    pivotGoneToDone = true;

                    // Do the violation print and check at the end of the event:
                    // e.g. an event might set an entry but not an exit
                    // (in this case, old exit value might have been used in conflict check)

                    // Hint processing: Constrain the search if the user provided hints in the driver file
                    // HP2: ignore cycle if the root is in ASYNC proc (e.g. a cycle due to doInBack x doInBack)
                    Verify.ignoreIf(hintRootInSync && !isRootInSync);
                    // HP3: ignore cycle if the root is in not in ASYNC proc on the main thread
                    Verify.ignoreIf(hintRootInAsyncMain && !isRootInAsyncMain);
                }
            }
        }
    }


}

/**
 * Insert the instrumentation statement onStatement(...) before each read/write stmt
 * (Inserted automatically by the instrumentor)
 *
 * Surround the originally asynchronous procedures with try/catch block as follows
 * To simulate their concurrent behavior
 * (See AsyncTask.java for posting doInBackground and onPostExecute methods as examples)
 *
 * public void onClick() {
 *    Checker.onStatament(...);
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
 *    ...
 * }
 *
 *
 *  public static void main (String[] args) {
 *
 *   Checker.setCheckerMode(args[0]);
 *   // (Optional) Provide hint to the Checker
 *
 *   Checker.beforeEvent();
 *   onClick1();
 *   Checker.afterEvent();
 *
 *   Checker.beforeEvent();
 *   onClick2();
 *   Checker.afterEvent();
 *
 *   ...
 * }
 *
 **/

