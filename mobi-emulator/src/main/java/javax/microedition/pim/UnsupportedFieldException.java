package javax.microedition.pim;

public class UnsupportedFieldException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 307410715215957925L;

  public UnsupportedFieldException() {
  }

  public UnsupportedFieldException(final String detailMessage) {
    super(detailMessage);
  }

  public UnsupportedFieldException(final String detailMessage, final int field) {
  }

  public int getField() {
    return 0;
  }
}
