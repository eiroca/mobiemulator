package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;
import java.io.IOException;

public abstract interface L2CAPConnection extends Connection {
    public static final int DEFAULT_MTU = 672;
    public static final int MINIMUM_MTU = 48;

    public abstract int getTransmitMTU() throws IOException;

    public abstract int getReceiveMTU() throws IOException;

    public abstract void send(byte[] paramArrayOfByte) throws IOException;

    public abstract int receive(byte[] paramArrayOfByte) throws IOException;

    public abstract boolean ready() throws IOException;
}
