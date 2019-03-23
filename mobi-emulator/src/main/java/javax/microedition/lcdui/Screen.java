package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class Screen extends Displayable {

  public final static Object globalSync = new Object();
  public final static Object paintsync = new Object();
  public static Font font = Font.getDefaultFont();
  public Image icon = null; // for tab;
  public String title = null; // for tabtitle
  public int yPos = 0;

  /**
   * Abstract method that needs to be implemented by concrete midlet supplied subclasses.
   */
  protected abstract void paint(javax.microedition.lcdui.Graphics g);

  @Override
  public void invokePaint(final Graphics g) {
    synchronized (Screen.paintsync) {
      paint(g);
    }
  }

}
