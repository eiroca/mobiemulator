package javax.microedition.io.file;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.NoSuchElementException;

final class a implements Enumeration {

  int a = 0;

  @Override
  public final boolean hasMoreElements() {
    return a < 1;
  }

  @Override
  public final Object nextElement() {
    if (a++ < 1) { return "root/"; }

    throw new NoSuchElementException("FileSystemRegistry Enumeration");
  }
}
