package javax.obex;

import java.io.IOException;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.io.Connection;

public abstract interface ClientSession extends Connection {

  public abstract void setAuthenticator(Authenticator paramAuthenticator);

  public abstract HeaderSet createHeaderSet();

  public abstract void setConnectionID(long paramLong);

  public abstract long getConnectionID();

  public abstract HeaderSet connect(HeaderSet paramHeaderSet) throws IOException;

  public abstract HeaderSet disconnect(HeaderSet paramHeaderSet) throws IOException;

  public abstract HeaderSet setPath(HeaderSet paramHeaderSet, boolean paramBoolean1, boolean paramBoolean) throws IOException;

  public abstract HeaderSet delete(HeaderSet paramHeaderSet) throws IOException;

  public abstract Operation get(HeaderSet paramHeaderSet) throws IOException;

  public abstract Operation put(HeaderSet paramHeaderSet) throws IOException;
}
