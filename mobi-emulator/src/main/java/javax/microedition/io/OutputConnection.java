package javax.microedition.io;

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
