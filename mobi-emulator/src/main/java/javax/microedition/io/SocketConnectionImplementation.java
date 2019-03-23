package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class SocketConnectionImplementation implements SocketConnection {

  String host = null;
  int port = 0;
  Socket socket = null;

  public SocketConnectionImplementation(final String hostport) {
    host = hostport.substring(hostport.indexOf("://") + 3, hostport.lastIndexOf(":"));
    port = Integer.parseInt(hostport.substring(hostport.indexOf(":") + 1));
    try {
      socket = new Socket(host, port);
    }
    catch (final Exception ex) {
      System.out.println(" Exception while socket connection");
    }
  }

  @Override
  public String getAddress() throws IOException {
    if (socket != null) { return socket.getInetAddress().getHostAddress(); }

    return null;
  }

  @Override
  public int getPort() throws IOException {
    if (socket != null) { return socket.getPort(); }

    return 0;
  }

  @Override
  public int getLocalPort() throws IOException {
    if (socket != null) { return socket.getLocalPort(); }

    return 0;
  }

  @Override
  public String getLocalAddress() throws IOException {
    if (socket != null) { return socket.getLocalAddress().getHostAddress(); }

    return null;
  }

  @Override
  public int getSocketOption(final byte option) throws IllegalArgumentException, IOException {
    if (socket != null) {
      switch (option) {
        case DELAY:
          return (socket.getTcpNoDelay() ? 1
              : 0);
        case KEEPALIVE:
          return (socket.getKeepAlive() ? 1
              : 0);
        case LINGER:
          return (socket.getSoLinger());
        case RCVBUF:
          return socket.getReceiveBufferSize();
        case SNDBUF:
          return socket.getSendBufferSize();
      }
    }

    return 0;
  }

  @Override
  public void setSocketOption(final byte option, final int value) throws IllegalArgumentException, IOException {
    if (socket != null) {
      switch (option) {
        case DELAY:
          socket.setTcpNoDelay(value != 0);

          break;
        case KEEPALIVE:
          socket.setKeepAlive(value != 0);

          break;
        case LINGER:
          socket.setSoLinger(value != 0, value);

          break;
        case RCVBUF:
          socket.setReceiveBufferSize(value);

          break;
        case SNDBUF:
          socket.setSendBufferSize(value);

          break;
      }
    }
  }

  @Override
  public InputStream openInputStream() throws IOException {
    if (socket != null) { return socket.getInputStream(); }
    return null;
  }

  @Override
  public DataInputStream openDataInputStream() throws IOException {
    if (socket != null) { return new DataInputStream(socket.getInputStream()); }
    return null;
  }

  @Override
  public void close() {
    if (socket != null) {
      try {
        socket.close();
      }
      catch (final IOException ex) {
        System.out.println("IOException while closing socket connection " + ex.getMessage());
      }
    }
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    if (socket != null) { return socket.getOutputStream(); }
    return null;
  }

  @Override
  public DataOutputStream openDataOutputStream() throws IOException {
    if (socket != null) { return new DataOutputStream(socket.getOutputStream()); }
    return null;
  }

}
