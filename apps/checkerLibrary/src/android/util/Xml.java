/**
 * Stub/model code simplified/modified from the Android library
 */

package android.util;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Xml {
    public static void parse(String xml, Object contentHandler) throws SAXException {
        throw new SAXException();
    }

    public static void parse(Reader in, Object contentHandler)
            throws IOException, SAXException {
        throw new SAXException();
    }

    public static void parse(InputStream in, Encoding encoding,
                             Object contentHandler) throws IOException, SAXException {
    }

    public enum Encoding {

        US_ASCII("US-ASCII"),
        UTF_8("UTF-8"),
        UTF_16("UTF-16"),
        ISO_8859_1("ISO-8859-1");

        final String expatName;

        Encoding(String expatName) {
            this.expatName = expatName;
        }
    }
}
