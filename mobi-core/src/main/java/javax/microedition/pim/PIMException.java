package javax.microedition.pim;

public class PIMException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 7207096370968487824L;
  public static final int FEATURE_NOT_SUPPORTED = 0;
  public static final int GENERAL_ERROR = 1;
  public static final int LIST_CLOSED = 2;
  public static final int LIST_NOT_ACCESSIBLE = 3;
  public static final int MAX_CATEGORIES_EXCEEDED = 4;
  public static final int UNSUPPORTED_VERSION = 5;
  public static final int UPDATE_ERROR = 6;
  private final int exception_reason;

  public PIMException() {
    exception_reason = 0;
  }

  public PIMException(final String detailMessage) {
    super(detailMessage);
    exception_reason = 0;
  }

  public PIMException(final String detailMessage, final int reason) {
    super(detailMessage);
    exception_reason = reason;
  }

  public int getReason() {
    return exception_reason;
  }
}
