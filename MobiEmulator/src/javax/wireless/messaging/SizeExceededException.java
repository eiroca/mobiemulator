package javax.wireless.messaging;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.IOException;

public class SizeExceededException extends IOException {
    public SizeExceededException(String reason) {
        super(reason);
    }
}
