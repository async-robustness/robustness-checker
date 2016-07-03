/**
 * Robustness Violation Checker Library
 */

package checker;


public abstract class BaseViolationChecker {
    /**
     * Must be set in the beginning/end of the async procedures
     */
    protected ProcMode PROC_MODE = ProcMode.SYNCMain;

    abstract public void beforeEvent();

    abstract public void afterEvent();

    abstract public void beforeAsyncProc();

    abstract public void afterAsyncProc();

    abstract public void onStatement(String accessType, String objId, String className, String fieldName, String methodName);

    protected static boolean checkConflict(FieldAccess access1, FieldAccess access2) {
        if (access1.getObjId().equals(access2.getObjId()) && access1.getFieldName().equals(access2.getFieldName())) {
            if (access1.getAccessType().equals("PUT") || access2.getAccessType().equals("PUT")) {
                if (!access1.getObjId().equals("static"))
                    return true;
                    // if it is a static field (with objId "static"), compare names as well
                else if (access1.getClassName().equals(access2.getClassName()))
                    return true;

            }
        }
        return false;
    }

    public void setProcMode(ProcMode m) {

        if (PROC_MODE == ProcMode.ASYNCMain && m == ProcMode.ASYNCMain) {
            System.out.println("WARNING: The current robustness checker implementation does not support nested asynchronous procedures posted to the main thread!");
            // System.out.println("         The access sets for the current proc needs to be saved before moving to the next procedure to restore back on return");
            // To-Do: Keep a stack of ProcModes (they can be called nested) and procedure-local variables
            // e.g. keep conflictDetected and sets of procs of invoked async tasks in a stack
            // If conflictDetected is reset at the end of the proc, it will be overwritten to false when returns from an inner proc
            // If nor modified at the end of proc (but reset at the end of the event) will be still set even if the conflicting was the inner proc
        }

        PROC_MODE = m;
    }

    public ProcMode getProcMode() {
        return PROC_MODE;
    }


    /**
     * For hint processing: The user can constrain the search by providing hints in the driver file
     */

    /**
     * HP1: Set hintPivotField to the field name of the expected pivot
     * to search for violations with the given pivot access field name
     * (e.g. the pivot is "dictionaryService" in Aarddict app)
     */
    protected String hintPivotField = null;

    public void setHintPivot(String pivotFieldName) {
        hintPivotField = pivotFieldName;
    }

    /**
     * HP2: Set hintRootInSync==true to search for violations where the root is in SYNC
     * (The root is in the sync execution in the main thread, e.g. ignores cycles doInBack x doInBack)
     */
    protected boolean hintRootInSync = false;
    protected boolean isRootInSync = false;
    protected boolean isTmpRootInSync = false;

    public void setHintRootInSync(boolean b) {
        hintRootInSync = b;
    }

    /**
     * HP3: Set hintRootInAsync==true to search for violations where the root is in ASYNCMain
     * (The root is in the asynchronous execution in the main thread)
     */
    protected boolean hintRootInAsyncMain = false;
    protected boolean isRootInAsyncMain = false;
    protected boolean isTmpRootInAsyncMain = false;

    public void setHintRootInAsyncMain(boolean b) {
        hintRootInAsyncMain = b;
    }


    // To-Do more hint options can be added (e.g. hint pivot in ASYNCMain or ASYNCBack)
}
