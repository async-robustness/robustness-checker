/**
 * Stub/model code simplified/modified from the Android library
 */

package android.text;

import java.util.Iterator;

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String htmlEncode(String s) {
        return s;
    }

    public interface StringSplitter extends Iterable<String> {
        public void setString(String string);
    }

    public static class SimpleStringSplitter implements StringSplitter, Iterator<String> {

        public SimpleStringSplitter(char delimiter) {
        }

        public void setString(String string) {
        }

        public Iterator<String> iterator() {
            return this;
        }

        public boolean hasNext() {
            return false;
        }

        public String next() {
            return "";
        }

        @Override
        public void remove() {
        }
    }
}
