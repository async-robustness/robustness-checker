/**
 * Robustness Violation Checker Library
 */

package checker;

public enum CheckerMode {
    // the violation checker can be enabled for:
    SER,        // event-serializability analysis
    DET,        // event-determinism analysis
    LOGRW,      // log the r/w accesses
    NOP
};
