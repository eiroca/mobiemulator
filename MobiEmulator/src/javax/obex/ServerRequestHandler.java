package javax.obex;

public class ServerRequestHandler {
    private long connId;

    protected ServerRequestHandler() {
        this.connId = -1L;
    }

    public final HeaderSet createHeaderSet() {
        return null;    // new HeaderSetImpl(3);
    }

    public void setConnectionID(long id) {
        if ((id < -1L) || (id > 4294967295L)) {
            throw new IllegalArgumentException("invalid id");
        }

        this.connId = id;
    }

    public long getConnectionID() {
        return this.connId;
    }

    public int onConnect(HeaderSet request, HeaderSet reply) {
        return 160;
    }

    public void onDisconnect(HeaderSet request, HeaderSet reply) {}

    public int onSetPath(HeaderSet request, HeaderSet reply, boolean backup, boolean create) {
        return 209;
    }

    public int onDelete(HeaderSet request, HeaderSet reply) {
        return 209;
    }

    public int onPut(Operation op) {
        return 209;
    }

    public int onGet(Operation op) {
        return 209;
    }

    public void onAuthenticationFailure(byte[] userName) {}
}
