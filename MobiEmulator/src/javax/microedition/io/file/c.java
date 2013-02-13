package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;

final class c implements FileFilter {
    private final FileConnectionImpl a;
    c(FileConnectionImpl paramFileConnectionImpl) {
        this.a = paramFileConnectionImpl;
    }

    public final boolean accept(File paramFile) {
        if ((!FileConnectionImpl.a(this.a)) && (paramFile.isHidden())) {
            return false;
        }

        return (paramFile.getName().matches(FileConnectionImpl.a2(this.a)));
    }
}
