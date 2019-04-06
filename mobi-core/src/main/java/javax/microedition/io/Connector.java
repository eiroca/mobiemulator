package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.io.file.FileConnectionImpl;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Connector {

  public static final int READ = 1;
  public static final int READ_WRITE = 3;
  public static final int WRITE = 2;
  public static Connection connection;

  public static Connection open(final String name) throws IOException {
    return Connector.open(name, Connector.READ_WRITE, false);
  }

  public static Connection open(final String name, final int mode) throws IOException {
    return Connector.open(name, mode, false);
  }

  public static Connection open(final String name, final int mode, final boolean timeouts) throws IOException {
    if (name.startsWith("http://")) {
      // HttpConnectionImplementation con=;
      Connector.connection = new HttpConnectionImplementation(name);
      // System.out.println("getLength is "+con.getLength()+" encoding "+con.getEncoding());
    }
    if (name.startsWith("file://")) {
      Connector.connection = new FileConnectionImpl(name);
    }

    return Connector.connection;
  }

  public static DataInputStream openDataInputStream(final String name) throws IOException {
    return null;
  }

  public static DataOutputStream openDataOutputStream(final String name) throws IOException {
    return null;
  }

  public static InputStream openInputStream(final String name) throws IOException {
    return null;
  }

  public static OutputStream openOutputStream(final String name) throws IOException {
    return null;
  }

}
