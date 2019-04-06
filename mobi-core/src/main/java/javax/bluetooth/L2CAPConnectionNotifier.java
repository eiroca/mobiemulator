package javax.bluetooth;

import java.io.IOException;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.io.Connection;

public abstract interface L2CAPConnectionNotifier extends Connection {

  public abstract L2CAPConnection acceptAndOpen() throws IOException;
}
