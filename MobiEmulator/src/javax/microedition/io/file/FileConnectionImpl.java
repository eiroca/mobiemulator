package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

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
    private static String fileRoot       = new File("").getAbsolutePath() + "/file/" + "root/";
    private static String fileRootPhotos = new File("").getAbsolutePath() + "/file/" + "root/photos";
    private String        d;
    private File          file;
    private String        fileUrl;
    private boolean       isConnClosed;
    private boolean       jdField_b_of_type_Boolean;
    private String        path;
    public FileConnectionImpl(String fileurl) {
        this.fileUrl      = fileurl;
        this.isConnClosed = false;
        File localFile;
        if ((!(localFile = new File(fileRoot)).exists()) || (localFile.isFile())) {
            localFile.mkdirs();
        }
        if (!(new File(fileRootPhotos)).exists()) {
            new File(fileRootPhotos).mkdirs();
        }
        fileurl   = fileurl.replaceFirst("localhost", "");
        this.path = (new File("").getAbsolutePath() + "/file/" + fileurl.substring("file://".length()));
        this.file = new File(this.path);
    }

    public long availableSize() {
        return 100000000L;
    }
    public boolean canRead() {
        return true;
    }
    public boolean canWrite() {
        return true;
    }
    public void create() throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }
        String dir = this.file.getAbsolutePath();
        dir = dir.substring(0, dir.lastIndexOf("\\"));
        if (!new File(dir).exists()) {
            new File(dir).mkdirs();
        }
        try {
            File f = new File(this.file.getAbsolutePath());
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete() throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }
        this.file.delete();
    }
    public long directorySize(boolean includeSubDirs) throws IOException {
        if (this.file.isFile()) {
            throw new IOException();
        }
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return getDirectorySize(includeSubDirs);
    }
    private int getDirectorySize(boolean paramBoolean) {
        int size = 0;
        if (this.file.isDirectory()) {
            File[] files = this.file.listFiles();
            for (int j = 0;j < files.length;j++) {
                if (files[j].isFile()) {
                    size = (int) (size + files[j].length());
                } else {
                    if (!paramBoolean) {
                        continue;
                    }
                    size += getDirectorySize(true);
                }
            }
        }

        return size;
    }
    public boolean exists() {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return this.file.exists();
    }
    public long fileSize() throws IOException {
        if (this.file.isDirectory()) {
            throw new IOException();
        }
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return this.file.length();
    }
    public String getName() {
        return this.file.getName();
    }
    public String getPath() {
        return this.fileUrl.replaceFirst("localhost", "").substring("file://".length());
    }
    public String getURL() {
        return this.fileUrl;
    }
    public boolean isDirectory() {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return this.file.isDirectory();
    }
    public boolean isHidden() {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return this.file.isHidden();
    }
    public boolean isOpen() {
        return !this.isConnClosed;
    }
    public long lastModified() {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }

        return this.file.lastModified();
    }
    public Enumeration list() throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((!this.file.exists()) || (this.file.isFile())) {
            throw new IOException();
        }
        File[] files;
        if ((files = this.file.listFiles()) != null) {
            return new d(this, files);
        }

        return null;
    }
    public Enumeration list(String filter, boolean includeHidden) throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((!this.file.exists()) || (this.file.isFile())) {
            throw new IOException();
        }
        File[] files;
        if ((files = this.file.listFiles(new c(this))) != null) {
            return new b(this, files);
        }

        return null;
    }
    public void mkdir() throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((this.file.exists()) || (this.file.isFile())) {
            throw new IOException();
        }
        this.file.mkdir();
    }
    public DataInputStream openDataInputStream() throws IOException {
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }

        return new DataInputStream(new FileInputStream(this.file));
    }
    public DataOutputStream openDataOutputStream() throws IOException {
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }

        return new DataOutputStream(new FileOutputStream(this.file));
    }
    public InputStream openInputStream() throws IOException {
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }

        return new FileInputStream(this.file);
    }
    public OutputStream openOutputStream() throws IOException {
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }

        return new FileOutputStream(this.file);
    }
    public OutputStream openOutputStream(long byteOffset) throws IOException {
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }
        FileInputStream fis  = new FileInputStream(this.file);
        byte[]          data = new byte[(int) Math.min(byteOffset, fis.available())];
        fis.read(data);
        FileOutputStream fos = new FileOutputStream(this.file);
        fos.write(data);
        fos.flush();

        return fos;
    }
    public void rename(String newFileName) throws IOException {
        if (newFileName == null) {
            throw new NullPointerException();
        }
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        File localFile = new File(this.file.getParent() + "//" + newFileName);
        this.file.renameTo(localFile);
    }
    public void setFileConnection(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException();
        }
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        File file;
        if (!(file = new File(this.file.getParent() + "//" + filePath)).exists()) {
            throw new IllegalArgumentException();
        }
        this.fileUrl      = ("file://" + file.getAbsolutePath().substring(fileRoot.length()));
        this.isConnClosed = false;
        this.path         = file.getAbsolutePath();
        this.file         = new File(this.path);
    }
    public void setHidden(boolean paramBoolean) throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if (!this.file.exists()) {
            throw new IOException();
        }
    }
    public void setReadable(boolean isReadable) throws IOException {}
    public void setWritable(boolean isWritable) throws IOException {}
    public long totalSize() {
        return 100000000L;
    }
    public void truncate(long paramLong) throws IOException {
        if (this.isConnClosed) {
            throw new ConnectionClosedException();
        }
        if ((!this.file.exists()) || (this.file.isDirectory())) {
            throw new IOException();
        }
        FileInputStream fis  = new FileInputStream(this.file);
        byte[]          data = new byte[(int) Math.min(paramLong, fis.available())];
        fis.read(data);
        fis.close();
        FileOutputStream fos = new FileOutputStream(this.file);
        fos.write(data);
        fos.flush();
        fos.close();
    }
    public long usedSize() {
        return 1000000L;
    }
    public void close() {
        this.isConnClosed = true;
    }
    static boolean a(FileConnectionImpl fileconn) {
        return fileconn.jdField_b_of_type_Boolean;
    }
    static String a2(FileConnectionImpl paramFileConnectionImpl) {
        return paramFileConnectionImpl.d;
    }
}
