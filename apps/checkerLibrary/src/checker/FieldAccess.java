/**
 * Robustness Violation Checker Library
 */

package checker;


public class FieldAccess {
    private String objId;
    private String className;
    private String fieldName;
    private String accessType; // GET or PUT
    private String methodAccessedIn;

    public FieldAccess(String accessType, String objId, String className, String fieldName, String methodAccessedIn) {
        this.objId = objId;
        this.className = className;
        this.fieldName = fieldName;
        this.accessType = accessType;
        this.methodAccessedIn = methodAccessedIn;
    }

    public String getObjId() {
        return objId;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getMethodAccessedIn() {
        return methodAccessedIn;
    }

    public String toString() {
        return accessType + "\t" + objId + "\t" + className + "\t" + fieldName + "\t" + "In: " + methodAccessedIn;
    }

    // To be used in the Set of accesses kept in the violation checkers
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldAccess that = (FieldAccess) o;

        if (objId != null ? !objId.equals(that.objId) : that.objId != null) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (accessType != null ? !accessType.equals(that.accessType) : that.accessType != null) return false;
        return methodAccessedIn != null ? methodAccessedIn.equals(that.methodAccessedIn) : that.methodAccessedIn == null;
    }

    @Override
    public int hashCode() {
        int result = objId != null ? objId.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (accessType != null ? accessType.hashCode() : 0);
        result = 31 * result + (methodAccessedIn != null ? methodAccessedIn.hashCode() : 0);
        return result;
    }

    public static void logFieldAccess(FieldAccess fa) {
        System.out.println(fa.toString());
    }
}
