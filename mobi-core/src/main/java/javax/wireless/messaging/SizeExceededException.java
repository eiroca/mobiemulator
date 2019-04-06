package javax.wireless.messaging;

// ~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.IOException;

public class SizeExceededException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = -1995561838805781257L;

  public SizeExceededException(final String reason) {
    super(reason);
  }
}
