package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.NoSuchElementException;

final class a implements Enumeration {
    int a = 0;
    public final boolean hasMoreElements() {
        return this.a < 1;
    }
    public final Object nextElement() {
        if (this.a++ < 1) {
            return "root/";
        }

        throw new NoSuchElementException("FileSystemRegistry Enumeration");
    }
}
