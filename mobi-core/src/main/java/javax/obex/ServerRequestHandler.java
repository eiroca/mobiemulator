package javax.obex;

public class ServerRequestHandler {

  private long connId;

  protected ServerRequestHandler() {
    connId = -1L;
  }

  public final HeaderSet createHeaderSet() {
    return null; // new HeaderSetImpl(3);
  }

  public void setConnectionID(final long id) {
    if ((id < -1L) || (id > 4294967295L)) { throw new IllegalArgumentException("invalid id"); }

    connId = id;
  }

  public long getConnectionID() {
    return connId;
  }

  public int onConnect(final HeaderSet request, final HeaderSet reply) {
    return 160;
  }

  public void onDisconnect(final HeaderSet request, final HeaderSet reply) {
  }

  public int onSetPath(final HeaderSet request, final HeaderSet reply, final boolean backup, final boolean create) {
    return 209;
  }

  public int onDelete(final HeaderSet request, final HeaderSet reply) {
    return 209;
  }

  public int onPut(final Operation op) {
    return 209;
  }

  public int onGet(final Operation op) {
    return 209;
  }

  public void onAuthenticationFailure(final byte[] userName) {
  }

}
