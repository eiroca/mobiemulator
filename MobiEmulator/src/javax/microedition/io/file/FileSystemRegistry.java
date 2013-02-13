package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.Vector;

public class FileSystemRegistry {
    static Vector roots;
    public static boolean addFileSystemListener(FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        roots.add(listener);

        return true;
    }
    public static boolean removeFileSystemListener(FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        roots.remove(listener);

        return true;
    }
    public static Enumeration listRoots() {
        return new a();
    }
    private static void notifyListeners(int event, String root) {
        for (int i = 0;i < roots.size();i++) {
            try {
                ((FileSystemListener) roots.elementAt(i)).rootChanged(event, root);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
