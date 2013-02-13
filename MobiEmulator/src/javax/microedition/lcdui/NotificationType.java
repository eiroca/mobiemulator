/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public final class NotificationType {
    public static final NotificationType CALL     = new NotificationType("CALL", null);
    public static final NotificationType IM       = new NotificationType("IM", null);
    public static final NotificationType EMAIL    = new NotificationType("EMAIL", null);
    public static final NotificationType MMS      = new NotificationType("MMS", null);
    public static final NotificationType SMS      = new NotificationType("SMS", null);
    public static final NotificationType REMINDER = new NotificationType("REMINDER", null);
    private Image                        icon     = null;
    private String                       label    = "";
    public NotificationType(java.lang.String defaultLabel, Image defaultImage) {}
}
