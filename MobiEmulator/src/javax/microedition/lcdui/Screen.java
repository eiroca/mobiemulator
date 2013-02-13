/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Screen extends Displayable {
    public final static Object globalSync = new Object();
    public final static Object paintsync  = new Object();
    public static Font   font       = Font.getDefaultFont();
    public Image         icon       = null;    // for tab;
    public String        title      = null;    // for tabtitle
    public int           yPos       = 0;

    /**
     * Abstract method that needs to be implemented by concrete midlet supplied
     * subclasses.
     */
    protected abstract void paint(javax.microedition.lcdui.Graphics g);
    public void invokePaint(Graphics g) {
        synchronized (Screen.paintsync) {
            paint(g);
        }
    }
}
