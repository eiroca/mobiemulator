package javax.microedition.io.file;

// ~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

final class b implements Enumeration {

  private final File[] jdField_a_of_type_ArrayOfJavaIoFile;
  int jdField_a_of_type_Int;

  b(final FileConnectionImpl paramFileConnectionImpl, final File[] paramArrayOfFile) {
    jdField_a_of_type_ArrayOfJavaIoFile = paramArrayOfFile;
    jdField_a_of_type_Int = 0;
  }

  @Override
  public final boolean hasMoreElements() {
    return jdField_a_of_type_Int < jdField_a_of_type_ArrayOfJavaIoFile.length;
  }

  @Override
  public final Object nextElement() {
    if (jdField_a_of_type_Int < jdField_a_of_type_ArrayOfJavaIoFile.length) { return jdField_a_of_type_ArrayOfJavaIoFile[jdField_a_of_type_Int]
        + (jdField_a_of_type_ArrayOfJavaIoFile[(jdField_a_of_type_Int++)].isDirectory() ? "/"
            : ""); }

    throw new NoSuchElementException("FileConection.list Enumeration");
  }
}
