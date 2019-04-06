package javax.obex;

import java.io.IOException;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.io.Connection;

public abstract interface SessionNotifier extends Connection {

  public abstract Connection acceptAndOpen(ServerRequestHandler paramServerRequestHandler) throws IOException;

  public abstract Connection acceptAndOpen(ServerRequestHandler paramServerRequestHandler, Authenticator paramAuthenticator)
      throws IOException;
}
