/**
 * Robustness Violation Checker Library
 */

package checker;


/**
 * Contains the static methods called by the instrumented application
 */
public class Checker {

    private static CheckerMode CHECKER_MODE = CheckerMode.NOP;
    private static BaseViolationChecker checker = new NopViolationChecker();

    /**
     * Must be set in the beginning of the driver program
     */
    public static void setCheckerMode(CheckerMode m) {
        CHECKER_MODE = m;

        if (m == CheckerMode.SER) {
            checker = new SerViolationChecker();
        } else if (m == CheckerMode.DET) {
            checker = new DetViolationChecker();
        } else if (m == CheckerMode.LOGRW) {
            checker = new RWLogger();
        } else {
            System.out.println("Invalid checker mode. Running in CheckerMode.NOP.");
        }
    }

    public static void setCheckerMode(String m) {
        setCheckerMode(CheckerMode.valueOf(m));
    }

    public static CheckerMode getCheckerMode() {
        return CHECKER_MODE;
    }

    public static void setProcMode(ProcMode m) {
        checker.setProcMode(m);
    }

    public static void beforeAsyncProc() {
        checker.beforeAsyncProc();
    }

    public static void afterAsyncProc() {
        checker.afterAsyncProc();
    }

    public static void beforeEvent() {
        checker.beforeEvent();
    }

    public static void afterEvent() {
        checker.afterEvent();
    }

    public static void onStatement(String accessType, String objId, String className, String fieldName, String methodName) {
        checker.onStatement(accessType, objId, className, fieldName, methodName);
    }

    /**
     * Methods for hint processing
     **/

    public static void setHintPivot(String pivotFieldName) {
        checker.setHintPivot(pivotFieldName);
        System.out.println("Violation checker is running with a hint: ");
        System.out.println("Conflict cycle is created by the access to the field: " + pivotFieldName + "\n");
    }

    public static void setHintRootInSync(boolean b) {
        checker.setHintRootInSync(b);
        System.out.println("Violation checker is running with a hint: ");
        System.out.println("Conflict cycle root is in the synchronous code. \n");
    }

    public static void setHintRootInAsyncMain(boolean b) {
        checker.setHintRootInAsyncMain(b);
        System.out.println("Violation checker is running with a hint: ");
        System.out.println("Conflict cycle root is in a asynchronous procedure on the main thread. \n");
    }
}
