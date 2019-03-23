package javax.bluetooth;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public class BluetoothStateException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = 1416755682950937086L;

  public BluetoothStateException() {
  }

  public BluetoothStateException(final String msg) {
    super(msg);
  }
}
