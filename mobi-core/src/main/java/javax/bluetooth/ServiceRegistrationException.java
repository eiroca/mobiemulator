package javax.bluetooth;

// ~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

public class ServiceRegistrationException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = -4890544761973810436L;

  public ServiceRegistrationException() {
  }

  public ServiceRegistrationException(final String msg) {
    super(msg);
  }
}
