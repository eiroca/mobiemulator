package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkableInputStream extends InputStream {
    private ByteArrayOutputStream baos;
    private int                   bufferIndex;
    private byte                  buffer[];
    private final InputStream     in;
    public MarkableInputStream(InputStream in) {
        this.in = in;
    }

    public boolean markSupported() {
        return true;
    }
    public void mark(int lookahead) {
        baos = new ByteArrayOutputStream();
    }
    public void reset() throws IOException {
        if (baos == null) {
            throw new IOException("Cannot reset an unmarked stream");
        }
        if (baos.size() == 0) {
            baos = null;
        } else {
            buffer      = baos.toByteArray();
            baos        = null;
            bufferIndex = 0;
        }
    }
    public void close() throws IOException {
        in.close();
        baos.close();
        baos   = null;
        buffer = null;
    }
    public int read() throws IOException {
        if (buffer != null) {
            return readFromBuffer();
        } else {
            return readFromStream();
        }
    }
    private int readFromBuffer() {
        int i = buffer[bufferIndex++];
        if (baos != null) {
            baos.write(i);
        }
        if (bufferIndex == buffer.length) {
            buffer = null;
        }

        return i;
    }
    private int readFromStream() throws IOException {
        int i = in.read();
        if ((i != -1) && (baos != null)) {
            baos.write(i);
        }

        return i;
    }
    public int read(byte b[], int offset, int length) throws IOException {
        if (buffer != null) {
            return readFromBuffer(b, offset, length);
        } else {
            return readFromStream(b, offset, length);
        }
    }
    private int readFromBuffer(byte b[], int offset, int length) {
        int bytesRead = -1;
        if (length <= buffer.length - bufferIndex) {
            System.arraycopy(buffer, bufferIndex, b, offset, length);
            bufferIndex += length;
            bytesRead   = length;
        } else {
            int count = buffer.length - bufferIndex;
            System.arraycopy(buffer, bufferIndex, b, offset, count);
            buffer    = null;
            bytesRead = count;
        }
        if (baos != null) {
            baos.write(b, offset, bytesRead);
        }

        return bytesRead;
    }
    private int readFromStream(byte b[], int offset, int length) throws IOException {
        int i = in.read(b, offset, length);
        if ((i != -1) && (baos != null)) {
            baos.write(b, offset, i);
        }

        return i;
    }
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }
}
