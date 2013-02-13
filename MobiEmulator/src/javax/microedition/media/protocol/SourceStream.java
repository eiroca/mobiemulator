/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media.protocol;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.Controllable;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface SourceStream extends Controllable {
    public static final int NOT_SEEKABLE      = 0;
    public static final int RANDOM_ACCESSIBLE = 2;
    public static final int SEEKABLE_TO_START = 1;
    public ContentDescriptor getContentDescriptor();
    public long getContentLength();
    public int read(byte[] b, int off, int len) throws java.io.IOException;
    public int getTransferSize();
    public long seek(long where) throws java.io.IOException;
    public long tell();
    public int getSeekType();
}
