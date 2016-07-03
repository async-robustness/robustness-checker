package instrumentor;

import soot.*;
import soot.jimple.*;

import java.util.*;

public class RobCheckInst extends BodyTransformer {

    private SootMethod instStmtMethod, identityHashCodeMethod, toHexMethod;

    // variables to be used in instrumenting accesses
    private Local hashInt = null, hashStr = null, accessTypeStr = null, objNameStr = null, fieldNameStr = null, methodNameStr = null;

    private boolean isRWLocalsDeclared = false;

    private boolean initiated = false;

    public static void main(String[] args) {
        // args[0]: directory from which to process classes

        if(args.length < 1) {
            System.out.println("Please enter the arg: <process-dir>");
            return;
        }

        RobCheckInst transformer = new RobCheckInst();

        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myInstrumenter", transformer));

        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);

        soot.Main.main(new String[]{
                "-debug",
                "-prepend-classpath",
                "-process-dir", args[0],
                "-output-format", "class",
                "-allow-phantom-refs"
        });
    }

    public void initTransformer() {

        SootClass violationCheckerClass, systemClass, integerClass;

        Scene.v().addBasicClass("checker.Checker", SootClass.BODIES);
        violationCheckerClass = Scene.v().getSootClass("checker.Checker");
        violationCheckerClass.setApplicationClass();
        instStmtMethod = violationCheckerClass.getMethodByName("onStatement");

        systemClass = Scene.v().getSootClass("java.lang.System");
        integerClass = Scene.v().getSootClass("java.lang.Integer");

        identityHashCodeMethod = systemClass.getMethodByName("identityHashCode");
        toHexMethod = integerClass.getMethodByName("toHexString");
    }

    @Override
    protected void internalTransform(final Body b, String phaseName,
                                     @SuppressWarnings("rawtypes") Map options) {

        if(!initiated) {
            initTransformer();
            initiated = true;
        }

        String className = b.getMethod().getDeclaringClass().toString();

        if (className.startsWith("checker.")) {
            // System.out.println("Skipped: " + className);
            return;
        }

        isRWLocalsDeclared = false;
        
        instrumentForRW(b);

        b.validate();
    }

    /**
     * Instruments R/W field accesses in the method
     */
    public void instrumentForRW(final Body b) {
        final PatchingChain<Unit> units = b.getUnits();
        Iterator stmtIt = units.snapshotIterator();

        String methodName = b.getMethod().getName();

        while (stmtIt.hasNext()) {
            Stmt s = (Stmt) stmtIt.next();
            Iterator boxDef, boxUse;
            boxDef = s.getDefBoxes().iterator();
            boxUse = s.getUseBoxes().iterator();

            while (boxDef.hasNext()) {
                ValueBox vBox = (ValueBox) boxDef.next();
                Value v = vBox.getValue();

                if (v instanceof InstanceFieldRef) {
                    logFieldAccess(v, b, units, s, "PUT", methodName, true);
                    break;
                }

                if (v instanceof StaticFieldRef) {
                    logFieldAccess(v, b, units, s, "PUT", methodName, false);
                    break;
                }
            }

            while (boxUse.hasNext()) {
                ValueBox vBox = (ValueBox) boxUse.next();
                Value v = vBox.getValue();

                if (v instanceof InstanceFieldRef) {
                    logFieldAccess(v, b, units, s, "GET", methodName, true);
                    break;
                }

                if (v instanceof StaticFieldRef) {
                    logFieldAccess(v, b, units, s, "GET", methodName, false);
                    break;
                }
            }
        }
    }


    private void logFieldAccess(Value v, Body b, PatchingChain<Unit> units, Stmt s, String accType, String methodName, boolean isInstanceField) {
        Value obj = null;
        SootField field = null;

        if(isInstanceField)
            obj = ((InstanceFieldRef) v).getBase();

        field = ((FieldRef) v).getField();

        if(field.isFinal()) return;

        // instrument only modcount of List classes
        if(isInstanceField && (obj.getType().toString().startsWith("rjava.util.AbstractList") || obj.getType().toString().startsWith("rjava.util.ArrayList")) && !field.getName().equals("modCount")) {
            System.out.println("Returning: " + field.getName());
            return;
        }

        if(!isRWLocalsDeclared) {
            hashInt = SootUtils.addTmpPrimitiveInt(b, "rob_hashInt");
            hashStr = SootUtils.addTmpString(b, "rob_hashStr");
            accessTypeStr = SootUtils.addTmpString(b, "rob_accessTypeStr");
            objNameStr = SootUtils.addTmpString(b, "rob_objNameStr");
            fieldNameStr = SootUtils.addTmpString(b, "rob_fieldNameStr");
            methodNameStr = SootUtils.addTmpString(b, "rob_methodNameStr");
            isRWLocalsDeclared = true;
        }

        // hashInt = obj.identityHashCode()
        if(isInstanceField) {
            InvokeExpr expr = Jimple.v().newStaticInvokeExpr(identityHashCodeMethod.makeRef(), ((InstanceFieldRef) v).getBase());
            units.insertBefore(Jimple.v().newAssignStmt(hashInt, expr), s);
            // hashStr = Integer.toHexString(hasInt)
            InvokeExpr expr2 = Jimple.v().newStaticInvokeExpr(toHexMethod.makeRef(), hashInt);
            units.insertBefore(Jimple.v().newAssignStmt(hashStr, expr2), s);
            units.insertBefore(Jimple.v().newAssignStmt(accessTypeStr, StringConstant.v(accType)), s);
            units.insertBefore(Jimple.v().newAssignStmt(objNameStr, StringConstant.v(obj.getType().toString())), s);

        } else {
            String staticFielddeclaringClass = ((StaticFieldRef) v).getFieldRef().declaringClass().getName();
            units.insertBefore(Jimple.v().newAssignStmt(hashStr, StringConstant.v("static")), s);
            units.insertBefore(Jimple.v().newAssignStmt(accessTypeStr, StringConstant.v(accType)), s);
            units.insertBefore(Jimple.v().newAssignStmt(objNameStr, StringConstant.v(staticFielddeclaringClass)), s);
        }

        units.insertBefore(Jimple.v().newAssignStmt(fieldNameStr, StringConstant.v(field.getName())), s);
        units.insertBefore(Jimple.v().newAssignStmt(methodNameStr, StringConstant.v(methodName)), s);

        units.insertBefore(Jimple.v().newInvokeStmt(
                Jimple.v().newStaticInvokeExpr(instStmtMethod.makeRef(), accessTypeStr, hashStr, objNameStr, fieldNameStr, methodNameStr)), s);
        
    }

}
