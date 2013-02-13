package javax.bluetooth;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;
import java.io.IOException;

public abstract interface L2CAPConnectionNotifier extends Connection {
    public abstract L2CAPConnection acceptAndOpen() throws IOException;
}
