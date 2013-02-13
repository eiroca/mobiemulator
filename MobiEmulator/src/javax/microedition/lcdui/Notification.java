/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Notification {
    private NotificationType notificaton;
    Notification(NotificationType type) {
        notificaton = type;
    }

    Notification(NotificationType type, java.lang.String label) {
        notificaton = type;
    }

    Notification(NotificationType type, java.lang.String label, Image image) {}

    public NotificationType getType() {
        return notificaton;
    }
    public void setLabel(java.lang.String label) {}
    public void setImage(Image image) {}
    public void setListener(NotificationListener listener) {}
    public void post(boolean selectable) {}
    public void post(boolean selectable, int duration) {}
    public void remove() {}
    public long getTimestamp() {
        return 0L;
    }
}
