package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public final class NotificationType {

  public static final NotificationType CALL = new NotificationType("CALL", null);
  public static final NotificationType IM = new NotificationType("IM", null);
  public static final NotificationType EMAIL = new NotificationType("EMAIL", null);
  public static final NotificationType MMS = new NotificationType("MMS", null);
  public static final NotificationType SMS = new NotificationType("SMS", null);
  public static final NotificationType REMINDER = new NotificationType("REMINDER", null);
  private final Image icon = null;
  private final String label = "";

  public NotificationType(final java.lang.String defaultLabel, final Image defaultImage) {
  }

}
