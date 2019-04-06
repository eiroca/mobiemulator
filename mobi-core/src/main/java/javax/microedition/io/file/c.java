package javax.microedition.io.file;

// ~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;

final class c implements FileFilter {

  private final FileConnectionImpl a;

  c(final FileConnectionImpl paramFileConnectionImpl) {
    a = paramFileConnectionImpl;
  }

  @Override
  public final boolean accept(final File paramFile) {
    if ((!FileConnectionImpl.a(a)) && (paramFile.isHidden())) { return false; }

    return (paramFile.getName().matches(FileConnectionImpl.a2(a)));
  }
}
