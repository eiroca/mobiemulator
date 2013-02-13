package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

final class b implements Enumeration {
    private final File[] jdField_a_of_type_ArrayOfJavaIoFile;
    int                  jdField_a_of_type_Int;
    b(FileConnectionImpl paramFileConnectionImpl, File[] paramArrayOfFile) {
        this.jdField_a_of_type_ArrayOfJavaIoFile = paramArrayOfFile;
        this.jdField_a_of_type_Int               = 0;
    }

    public final boolean hasMoreElements() {
        return this.jdField_a_of_type_Int < this.jdField_a_of_type_ArrayOfJavaIoFile.length;
    }
    public final Object nextElement() {
        if (this.jdField_a_of_type_Int < this.jdField_a_of_type_ArrayOfJavaIoFile.length) {
            return this.jdField_a_of_type_ArrayOfJavaIoFile[this.jdField_a_of_type_Int]
                   + (this.jdField_a_of_type_ArrayOfJavaIoFile[(this.jdField_a_of_type_Int++)].isDirectory() ? "/"
                    : "");
        }

        throw new NoSuchElementException("FileConection.list Enumeration");
    }
}
