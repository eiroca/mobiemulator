package javax.microedition.io.file;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.StreamConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public abstract interface FileConnection extends StreamConnection {
    public abstract boolean isOpen();
    public abstract InputStream openInputStream() throws IOException;
    public abstract DataInputStream openDataInputStream() throws IOException;
    public abstract OutputStream openOutputStream() throws IOException;
    public abstract DataOutputStream openDataOutputStream() throws IOException;
    public abstract OutputStream openOutputStream(long paramLong) throws IOException;
    public abstract long totalSize();
    public abstract long availableSize();
    public abstract long usedSize();
    public abstract long directorySize(boolean paramBoolean) throws IOException;
    public abstract long fileSize() throws IOException;
    public abstract boolean canRead();
    public abstract boolean canWrite();
    public abstract boolean isHidden();
    public abstract void setReadable(boolean paramBoolean) throws IOException;
    public abstract void setWritable(boolean paramBoolean) throws IOException;
    public abstract void setHidden(boolean paramBoolean) throws IOException;
    public abstract Enumeration list() throws IOException;
    public abstract Enumeration list(String paramString, boolean paramBoolean) throws IOException;
    public abstract void create() throws IOException;
    public abstract void mkdir() throws IOException;
    public abstract boolean exists();
    public abstract boolean isDirectory();
    public abstract void delete() throws IOException;
    public abstract void rename(String paramString) throws IOException;
    public abstract void truncate(long paramLong) throws IOException;
    public abstract void setFileConnection(String paramString) throws IOException;
    public abstract String getName();
    public abstract String getPath();
    public abstract String getURL();
    public abstract long lastModified();
}
