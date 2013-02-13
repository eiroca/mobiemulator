/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AlertType {
    private static boolean         isAlerted    = false;
    public static final AlertType WARNING      = new javax.microedition.lcdui.AlertType(2);
    public static final AlertType INFO         = new javax.microedition.lcdui.AlertType(1);
    public static final AlertType       FOREVER      = new javax.microedition.lcdui.AlertType(6);
    public static final AlertType ERROR        = new javax.microedition.lcdui.AlertType(3);
    public static final AlertType CONFIRMATION = new javax.microedition.lcdui.AlertType(5);
    public static final AlertType ALARM        = new javax.microedition.lcdui.AlertType(4);
    private int                   type         = 0;
    protected AlertType() {}

    private AlertType(int type) {
        this.type = type;
    }

    public boolean playSound(Display display) {
        return isAlerted;
    }
}
