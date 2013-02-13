package javax.microedition.pim;

public class UnsupportedFieldException extends RuntimeException {
    public UnsupportedFieldException() {}

    public UnsupportedFieldException(String detailMessage) {
        super(detailMessage);
    }

    public UnsupportedFieldException(String detailMessage, int field) {}

    public int getField() {
        return 0;
    }
}
