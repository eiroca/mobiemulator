package javax.microedition.io;

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface ServerSocketConnection extends StreamConnectionNotifier {

  public abstract String getLocalAddress() throws IOException;

  public abstract int getLocalPort() throws IOException;

}
