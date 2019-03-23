package javax.microedition.io.file;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.Vector;

public class FileSystemRegistry {

  static Vector roots;

  public static boolean addFileSystemListener(final FileSystemListener listener) {
    if (listener == null) { throw new NullPointerException(); }
    FileSystemRegistry.roots.add(listener);

    return true;
  }

  public static boolean removeFileSystemListener(final FileSystemListener listener) {
    if (listener == null) { throw new NullPointerException(); }
    FileSystemRegistry.roots.remove(listener);

    return true;
  }

  public static Enumeration listRoots() {
    return new a();
  }

  private static void notifyListeners(final int event, final String root) {
    for (int i = 0; i < FileSystemRegistry.roots.size(); i++) {
      try {
        ((FileSystemListener)FileSystemRegistry.roots.elementAt(i)).rootChanged(event, root);
      }
      catch (final Throwable t) {
        t.printStackTrace();
      }
    }
  }
}
