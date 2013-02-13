/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface OutputConnection extends Connection {
    public OutputStream openOutputStream() throws IOException;
    public DataOutputStream openDataOutputStream() throws IOException;
}
