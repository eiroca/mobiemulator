package javax.microedition.pim;

public class FieldEmptyException extends RuntimeException {
    private int offending_field;
    public FieldEmptyException() {
        offending_field = 0;
    }

    public FieldEmptyException(String detailMessage) {
        super(detailMessage);
        offending_field = 0;
    }

    public FieldEmptyException(String detailMessage, int field) {
        super(detailMessage);
        offending_field = field;
    }

    public int getField() {
        return offending_field;
    }
}
