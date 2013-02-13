/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface SocketConnection extends StreamConnection {
    public static final byte DELAY     = 0;
    public static final byte KEEPALIVE = 2;
    public static final byte LINGER    = 1;
    public static final byte RCVBUF    = 3;
    public static final byte SNDBUF    = 4;
    public abstract String getAddress() throws IOException;
    public abstract int getPort() throws IOException;
    public abstract int getLocalPort() throws IOException;
    public abstract String getLocalAddress() throws IOException;
    public abstract int getSocketOption(byte option) throws IllegalArgumentException, IOException;
    public abstract void setSocketOption(byte option, int value) throws IllegalArgumentException, IOException;
}
