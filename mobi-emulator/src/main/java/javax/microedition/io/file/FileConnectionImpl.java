package javax.microedition.io.file;

// ~--- JDK imports ------------------------------------------------------------

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class FileConnectionImpl implements FileConnection {

  private static String fileRoot = new File("").getAbsolutePath() + "/file/" + "root/";
  private static String fileRootPhotos = new File("").getAbsolutePath() + "/file/" + "root/photos";
  private String d;
  private File file;
  private String fileUrl;
  private boolean isConnClosed;
  private boolean jdField_b_of_type_Boolean;
  private String path;

  public FileConnectionImpl(String fileurl) {
    fileUrl = fileurl;
    isConnClosed = false;
    File localFile;
    if ((!(localFile = new File(FileConnectionImpl.fileRoot)).exists()) || (localFile.isFile())) {
      localFile.mkdirs();
    }
    if (!(new File(FileConnectionImpl.fileRootPhotos)).exists()) {
      new File(FileConnectionImpl.fileRootPhotos).mkdirs();
    }
    fileurl = fileurl.replaceFirst("localhost", "");
    path = (new File("").getAbsolutePath() + "/file/" + fileurl.substring("file://".length()));
    file = new File(path);
  }

  @Override
  public long availableSize() {
    return 100000000L;
  }

  @Override
  public boolean canRead() {
    return true;
  }

  @Override
  public boolean canWrite() {
    return true;
  }

  @Override
  public void create() throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((file.exists()) || (file.isDirectory())) { throw new IOException(); }
    String dir = file.getAbsolutePath();
    dir = dir.substring(0, dir.lastIndexOf("\\"));
    if (!new File(dir).exists()) {
      new File(dir).mkdirs();
    }
    try {
      final File f = new File(file.getAbsolutePath());
      f.createNewFile();
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete() throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }
    file.delete();
  }

  @Override
  public long directorySize(final boolean includeSubDirs) throws IOException {
    if (file.isFile()) { throw new IOException(); }
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return getDirectorySize(includeSubDirs);
  }

  private int getDirectorySize(final boolean paramBoolean) {
    int size = 0;
    if (file.isDirectory()) {
      final File[] files = file.listFiles();
      for (final File file2 : files) {
        if (file2.isFile()) {
          size = (int)(size + file2.length());
        }
        else {
          if (!paramBoolean) {
            continue;
          }
          size += getDirectorySize(true);
        }
      }
    }

    return size;
  }

  @Override
  public boolean exists() {
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return file.exists();
  }

  @Override
  public long fileSize() throws IOException {
    if (file.isDirectory()) { throw new IOException(); }
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return file.length();
  }

  @Override
  public String getName() {
    return file.getName();
  }

  @Override
  public String getPath() {
    return fileUrl.replaceFirst("localhost", "").substring("file://".length());
  }

  @Override
  public String getURL() {
    return fileUrl;
  }

  @Override
  public boolean isDirectory() {
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return file.isDirectory();
  }

  @Override
  public boolean isHidden() {
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return file.isHidden();
  }

  @Override
  public boolean isOpen() {
    return !isConnClosed;
  }

  @Override
  public long lastModified() {
    if (isConnClosed) { throw new ConnectionClosedException(); }

    return file.lastModified();
  }

  @Override
  public Enumeration list() throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((!file.exists()) || (file.isFile())) { throw new IOException(); }
    File[] files;
    if ((files = file.listFiles()) != null) { return new d(this, files); }

    return null;
  }

  @Override
  public Enumeration list(final String filter, final boolean includeHidden) throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((!file.exists()) || (file.isFile())) { throw new IOException(); }
    File[] files;
    if ((files = file.listFiles(new c(this))) != null) { return new b(this, files); }

    return null;
  }

  @Override
  public void mkdir() throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((file.exists()) || (file.isFile())) { throw new IOException(); }
    file.mkdir();
  }

  @Override
  public DataInputStream openDataInputStream() throws IOException {
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }

    return new DataInputStream(new FileInputStream(file));
  }

  @Override
  public DataOutputStream openDataOutputStream() throws IOException {
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }

    return new DataOutputStream(new FileOutputStream(file));
  }

  @Override
  public InputStream openInputStream() throws IOException {
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }

    return new FileInputStream(file);
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }

    return new FileOutputStream(file);
  }

  @Override
  public OutputStream openOutputStream(final long byteOffset) throws IOException {
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }
    final FileInputStream fis = new FileInputStream(file);
    final byte[] data = new byte[(int)Math.min(byteOffset, fis.available())];
    fis.read(data);
    final FileOutputStream fos = new FileOutputStream(file);
    fos.write(data);
    fos.flush();

    return fos;
  }

  @Override
  public void rename(final String newFileName) throws IOException {
    if (newFileName == null) { throw new NullPointerException(); }
    if (isConnClosed) { throw new ConnectionClosedException(); }
    final File localFile = new File(file.getParent() + "//" + newFileName);
    file.renameTo(localFile);
  }

  @Override
  public void setFileConnection(final String filePath) throws IOException {
    if (filePath == null) { throw new NullPointerException(); }
    if (isConnClosed) { throw new ConnectionClosedException(); }
    File file;
    if (!(file = new File(this.file.getParent() + "//" + filePath)).exists()) { throw new IllegalArgumentException(); }
    fileUrl = ("file://" + file.getAbsolutePath().substring(FileConnectionImpl.fileRoot.length()));
    isConnClosed = false;
    path = file.getAbsolutePath();
    this.file = new File(path);
  }

  @Override
  public void setHidden(final boolean paramBoolean) throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if (!file.exists()) { throw new IOException(); }
  }

  @Override
  public void setReadable(final boolean isReadable) throws IOException {
  }

  @Override
  public void setWritable(final boolean isWritable) throws IOException {
  }

  @Override
  public long totalSize() {
    return 100000000L;
  }

  @Override
  public void truncate(final long paramLong) throws IOException {
    if (isConnClosed) { throw new ConnectionClosedException(); }
    if ((!file.exists()) || (file.isDirectory())) { throw new IOException(); }
    final FileInputStream fis = new FileInputStream(file);
    final byte[] data = new byte[(int)Math.min(paramLong, fis.available())];
    fis.read(data);
    fis.close();
    final FileOutputStream fos = new FileOutputStream(file);
    fos.write(data);
    fos.flush();
    fos.close();
  }

  @Override
  public long usedSize() {
    return 1000000L;
  }

  @Override
  public void close() {
    isConnClosed = true;
  }

  static boolean a(final FileConnectionImpl fileconn) {
    return fileconn.jdField_b_of_type_Boolean;
  }

  static String a2(final FileConnectionImpl paramFileConnectionImpl) {
    return paramFileConnectionImpl.d;
  }
}
