package javax.microedition.pim;

public class FieldFullException extends RuntimeException {
    private int offending_field;
    public FieldFullException() {
        offending_field = 0;
    }

    public FieldFullException(String detailMessage) {
        super(detailMessage);
        offending_field = 0;
    }

    public FieldFullException(String detailMessage, int field) {
        super(detailMessage);
        offending_field = field;
    }

    public int getField() {
        return offending_field;
    }
}
