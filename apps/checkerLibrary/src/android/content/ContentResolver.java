/**
 * Stub/model code simplified/modified from the Android library
 */

package android.content;

import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ContentResolver {

    public String getType(Uri type) {
        return "";
    }

    public final InputStream openInputStream(Object uri) throws FileNotFoundException {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }
}
