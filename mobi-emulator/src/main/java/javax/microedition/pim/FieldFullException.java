package javax.microedition.pim;

public class FieldFullException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -7198565320377211387L;
  private final int offending_field;

  public FieldFullException() {
    offending_field = 0;
  }

  public FieldFullException(final String detailMessage) {
    super(detailMessage);
    offending_field = 0;
  }

  public FieldFullException(final String detailMessage, final int field) {
    super(detailMessage);
    offending_field = field;
  }

  public int getField() {
    return offending_field;
  }
}
