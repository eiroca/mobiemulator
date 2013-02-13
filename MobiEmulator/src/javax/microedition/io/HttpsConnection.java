/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.IOException;

public abstract interface HttpsConnection extends HttpConnection {
    public abstract int getPort();
    public abstract SecurityInfo getSecurityInfo() throws IOException;
}
