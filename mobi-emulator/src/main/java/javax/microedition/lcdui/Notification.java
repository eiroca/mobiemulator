package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Notification {

  private NotificationType notificaton;

  Notification(final NotificationType type) {
    notificaton = type;
  }

  Notification(final NotificationType type, final java.lang.String label) {
    notificaton = type;
  }

  Notification(final NotificationType type, final java.lang.String label, final Image image) {
  }

  public NotificationType getType() {
    return notificaton;
  }

  public void setLabel(final java.lang.String label) {
  }

  public void setImage(final Image image) {
  }

  public void setListener(final NotificationListener listener) {
  }

  public void post(final boolean selectable) {
  }

  public void post(final boolean selectable, final int duration) {
  }

  public void remove() {
  }

  public long getTimestamp() {
    return 0L;
  }

}
