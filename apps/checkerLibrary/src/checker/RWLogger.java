/**
 * Robustness Violation Checker Library
 */

package checker;


public class RWLogger extends BaseViolationChecker {

    public RWLogger() {
    }

    @Override
    public void beforeEvent() {

    }

    @Override
    public void afterEvent() {

    }

    @Override
    public void beforeAsyncProc(ProcMode mode) {

    }

    @Override
    public void afterAsyncProc() {

    }

    @Override
    public void onStatement(String accessType, String objId, String className, String fieldName, String methodName) {
        FieldAccess.logFieldAccess(new FieldAccess(accessType, objId, className, fieldName, methodName));
    }
}
