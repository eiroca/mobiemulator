package javax.microedition.io.file;

public class ConnectionClosedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ConnectionClosedException() {
  }

  public ConnectionClosedException(final String paramString) {
    super(paramString);
  }
}
