/**
 * Stub/model code simplified/modified from the Android library
 */

package android.accounts;

public class Account {
    public final String name;
    public final String type;

    public Account() {
        name = "";
        type = "";
    }

    public Account(String s, String t) {
        name = s;
        type = t;
    }
}
