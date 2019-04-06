package javax.microedition.io;

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface StreamConnectionNotifier extends Connection {

  public abstract StreamConnection acceptAndOpen() throws IOException;
}
