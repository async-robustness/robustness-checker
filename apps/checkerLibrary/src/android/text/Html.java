/**
 * Stub/model code simplified/modified from the Android library
 */

package android.text;

public class Html {

    public static Spanned fromHtml(String source) {
        return new Spanned() {
            public CharSequence subSequence(int start, int end) {
                return "";
            }

            public char charAt(int index) {
                return ' ';
            }

            public int length() {
                return 0;
            }
        };
    }
}
