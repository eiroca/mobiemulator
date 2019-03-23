package javax.bluetooth;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public class BluetoothConnectionException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = -2469854078306308029L;
  public static final int FAILED_NOINFO = 4;
  public static final int NO_RESOURCES = 3;
  public static final int SECURITY_BLOCK = 2;
  public static final int TIMEOUT = 5;
  public static final int UNACCEPTABLE_PARAMS = 6;
  public static final int UNKNOWN_PSM = 1;
  private int status;

  public BluetoothConnectionException(final int error) {
    this(error, null);
  }

  public BluetoothConnectionException(final int error, final String msg) {
    super(msg);

    switch (error) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        status = error;

        break;
      default:
        throw new IllegalArgumentException("Invalid error code: " + error);
    }
  }

  public int getStatus() {
    return status;
  }
}
