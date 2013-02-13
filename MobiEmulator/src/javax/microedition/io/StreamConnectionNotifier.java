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
public abstract interface StreamConnectionNotifier extends Connection {
    public abstract StreamConnection acceptAndOpen() throws IOException;
}
