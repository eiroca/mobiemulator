package javax.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
// ~--- JDK imports ------------------------------------------------------------
/**
 *
 * @author Ashok Kumar Gujarathi
 */
import javax.microedition.io.Connection;

public class L2CAPConnectionImpl implements L2CAPConnection {

  Socket a;
  InputStream b;
  OutputStream c;

  protected L2CAPConnectionImpl(final Socket paramSocket) throws IOException {
    a = paramSocket;
    b = paramSocket.getInputStream();
    c = paramSocket.getOutputStream();
    paramSocket.setReceiveBufferSize(672);
    paramSocket.setSendBufferSize(672);
  }

  @Override
  public int getTransmitMTU() throws IOException {
    return a.getSendBufferSize();
  }

  @Override
  public int getReceiveMTU() throws IOException {
    return a.getReceiveBufferSize();
  }

  @Override
  public void send(final byte[] paramArrayOfByte) throws IOException {
    c.write(paramArrayOfByte, 0, Math.min(672, paramArrayOfByte.length));
  }

  @Override
  public int receive(final byte[] paramArrayOfByte) throws IOException {
    return b.read(paramArrayOfByte, 0, Math.min(672, paramArrayOfByte.length));
  }

  @Override
  public boolean ready() throws IOException {
    return b.available() > 0;
  }

  @Override
  public void close() {
    try {
      a.close();
    }
    catch (final IOException ex) {
      Logger.getLogger(L2CAPConnectionImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static Connection open(final String paramString) throws IOException {
    final String str1 = paramString.substring(0, paramString.indexOf("://"));
    final int i = paramString.indexOf("://") + 3;
    final String str2 = paramString.substring(i, paramString.indexOf(":", i));
    final int j = paramString.indexOf(";");
    final String str3 = paramString.substring(paramString.indexOf(":", i) + 1, (j >= 0) ? j : paramString.length());
    final String str4 = (j >= 0) ? paramString.substring(j) : "";
    System.out.println("L2CAPConnection:");
    System.out.println("procotol=" + str1);
    System.out.println("host    =" + str2);
    System.out.println("uuid    =" + str3);
    System.out.println("options =" + str4);

    // if (paramString.indexOf("//localhost") >= 0)
    // return new a(str3);
    return new L2CAPConnectionImpl(new Socket(str2, Integer.parseInt(str3)));
  }

}
