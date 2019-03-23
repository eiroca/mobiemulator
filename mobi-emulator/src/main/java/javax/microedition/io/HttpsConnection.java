package javax.microedition.io;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.IOException;

public abstract interface HttpsConnection extends HttpConnection {

  @Override
  public abstract int getPort();

  public abstract SecurityInfo getSecurityInfo() throws IOException;

}
