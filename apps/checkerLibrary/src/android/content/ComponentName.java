/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

public final class ComponentName {
    private final String mPackage;
    private final String mClass;

    private Class mClazz;

    public ComponentName(String pkg, String cls) {
        if (pkg == null) throw new NullPointerException("package name is null");
        if (cls == null) throw new NullPointerException("class name is null");
        mPackage = pkg;
        mClass = cls;
    }

    public ComponentName(Context pkg, String cls) {
        if (cls == null) throw new NullPointerException("class name is null");
        mPackage = pkg.getPackageName();
        mClass = cls;
    }

    public ComponentName(Context pkg, Class<?> cls) {
        mPackage = pkg.getPackageName();
        mClass = cls.getName();
        mClazz = cls; // added
    }

    public Class getComponentClass() {
        return mClazz;   // e.g. start Service using this class
    }

    // added to be used in the modelled library classes
    public ComponentName() {
        mPackage = "";
        mClass = "";
    }

    public ComponentName clone() {
        return new ComponentName(mPackage, mClass);
    }

    public String getPackageName() {
        return mPackage;
    }

    public String getClassName() {
        return mClass;
    }

    public String getShortClassName() {
        if (mClass.startsWith(mPackage)) {
            int PN = mPackage.length();
            int CN = mClass.length();
            if (CN > PN && mClass.charAt(PN) == '.') {
                return mClass.substring(PN, CN);
            }
        }
        return mClass;
    }

    private static void appendShortClassName(StringBuilder sb, String packageName,
                                             String className) {
        if (className.startsWith(packageName)) {
            int PN = packageName.length();
            int CN = className.length();
            if (CN > PN && className.charAt(PN) == '.') {
                sb.append(className, PN, CN);
                return;
            }
        }
        sb.append(className);
    }

    public String flattenToString() {
        return mPackage + "/" + mClass;
    }


    public String flattenToShortString() {
        return "";
    }

    public static ComponentName unflattenFromString(String str) {
        return new ComponentName();
    }

    public String toShortString() {
        return "{" + mPackage + "/" + mClass + "}";
    }

    @Override
    public String toString() {
        return "ComponentInfo{" + mPackage + "/" + mClass + "}";
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj != null) {
                ComponentName other = (ComponentName) obj;
                // Note: no null checks, because mPackage and mClass can
                // never be null.
                return mPackage.equals(other.mPackage)
                        && mClass.equals(other.mClass);
            }
        } catch (ClassCastException e) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mPackage.hashCode() + mClass.hashCode();
    }

    public int compareTo(ComponentName that) {
        int v;
        v = this.mPackage.compareTo(that.mPackage);
        if (v != 0) {
            return v;
        }
        return this.mClass.compareTo(that.mClass);
    }

    public int describeContents() {
        return 0;
    }

 /*
    public ComponentName(Parcel in) {
        mPackage = in.readString();
        if (mPackage == null) throw new NullPointerException(
                "package name is null");
        mClass = in.readString();
        if (mClass == null) throw new NullPointerException(
                "class name is null");
    }

    private ComponentName(String pkg, Parcel in) {
        mPackage = pkg;
        mClass = in.readString();
    }*/
}
