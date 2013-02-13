package javax.obex;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.Connection;
import java.io.IOException;

public abstract interface SessionNotifier extends Connection {
    public abstract Connection acceptAndOpen(ServerRequestHandler paramServerRequestHandler) throws IOException;

    public abstract Connection acceptAndOpen(ServerRequestHandler paramServerRequestHandler,
            Authenticator paramAuthenticator)
            throws IOException;
}
