package javax.obex;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.io.ContentConnection;
import java.io.IOException;

public abstract interface Operation extends ContentConnection {
    public abstract void abort() throws IOException;

    public abstract HeaderSet getReceivedHeaders() throws IOException;

    public abstract void sendHeaders(HeaderSet paramHeaderSet) throws IOException;

    public abstract int getResponseCode() throws IOException;
}
