package javax.microedition.pim;

public class FieldEmptyException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 8067332968476016857L;
  private final int offending_field;

  public FieldEmptyException() {
    offending_field = 0;
  }

  public FieldEmptyException(final String detailMessage) {
    super(detailMessage);
    offending_field = 0;
  }

  public FieldEmptyException(final String detailMessage, final int field) {
    super(detailMessage);
    offending_field = field;
  }

  public int getField() {
    return offending_field;
  }
}
