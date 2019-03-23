package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface NotificationListener {

  public void notificationDismissed(Notification notification);

  public void notificationSelected(Notification notification);

  public void notificationTimeout(Notification notification);

}
