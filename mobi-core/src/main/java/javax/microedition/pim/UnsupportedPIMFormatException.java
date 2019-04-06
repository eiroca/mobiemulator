package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public class UnsupportedPIMFormatException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = -8963926535674081319L;

  public UnsupportedPIMFormatException() {
  }

  public UnsupportedPIMFormatException(final String message) {
    super(message);
  }
}
