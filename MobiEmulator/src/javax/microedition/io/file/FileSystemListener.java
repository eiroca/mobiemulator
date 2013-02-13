package javax.microedition.io.file;

public abstract interface FileSystemListener {
    public static final int ROOT_ADDED   = 0;
    public static final int ROOT_REMOVED = 1;
    public abstract void rootChanged(int paramInt, String paramString);
}
