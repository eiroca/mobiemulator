/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface ServerSocketConnection extends StreamConnectionNotifier {
    public abstract String getLocalAddress() throws IOException;
    public abstract int getLocalPort() throws IOException;
}
