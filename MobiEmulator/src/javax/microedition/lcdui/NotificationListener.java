/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



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
