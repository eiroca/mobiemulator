package javax.microedition.lcdui;

import java.util.ArrayList;

// ~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MidletUtils;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Alert extends Screen {

  private int AlertTimeout = 100;
  private Image alertImage = null;
  private String alertText = null;
  private AlertType alertType = AlertType.FOREVER;
  private final Command DISMISS_COMMAND = new Command("DISMISS", Command.BACK, 0);
  private ArrayList lines = new ArrayList();
  private CommandListener cmdListener;
  private Gauge guageIndicator;
  private final int height;
  private Displayable previousDisplay;
  private final String title;
  private final int width;

  public Alert(final String title) {
    this.title = title;
    width = MidletUtils.getInstance().getMidletListener().getCanvasWidth();
    height = MidletUtils.getInstance().getMidletListener().getCanvasHeight();
  }

  public Alert(final String title, String alertText, final Image alertImage, final AlertType alertType) {
    this.title = title;
    this.alertText = alertText;
    this.alertImage = alertImage;
    this.alertType = alertType;
    width = MidletUtils.getInstance().getMidletListener().getCanvasWidth();
    height = MidletUtils.getInstance().getMidletListener().getCanvasHeight();
    previousDisplay = Display.display.getCurrent();
    if (alertText == null) {
      alertText = "";
    }
    lines = StringUtils.wrapToLines(alertText, Screen.font, width - 10);
    addCommand(DISMISS_COMMAND);
  }

  public int getDefaultTimeout() {
    return AlertTimeout;
  }

  public int getTimeout() {
    return AlertTimeout;
  }

  public void setTimeout(final int time) {
    AlertTimeout = time;
  }

  public AlertType getType() {
    return alertType;
  }

  public void setType(final AlertType type) {
    alertType = type;
  }

  public String getString() {
    return alertText;
  }

  public void setString(String str) {
    if (str == null) {
      str = "";
    }
    alertText = str;
    lines = StringUtils.wrapToLines(alertText, Screen.font, width - 10);
  }

  public Image getImage() {
    return alertImage;
  }

  public void setImage(final Image img) {
    alertImage = img;
  }

  public void setIndicator(final Gauge indicator) {
    guageIndicator = indicator;
  }

  public Gauge getIndicator() {
    return guageIndicator;
  }

  @Override
  public void addCommand(final Command cmd) {
    if (cmd == DISMISS_COMMAND) { return; }
    removeCommand(DISMISS_COMMAND);
    addCommand(cmd);
  }

  @Override
  public void removeCommand(final Command cmd) {
    if (getCommandsCount() == 0) {
      addCommand(DISMISS_COMMAND);
    }
  }

  @Override
  public void setCommandListener(final CommandListener l) {
    cmdListener = l;
  }

  @Override
  protected void keyPressed(final int keyCode) {
  }

  @Override
  protected void keyRepeated(final int keyCode) {
  }

  @Override
  protected void keyReleased(final int keyCode) {
  }

  @Override
  protected void pointerPressed(final int x, final int y) {
  }

  @Override
  protected void pointerDragged(final int x, final int y) {
  }

  @Override
  protected void pointerReleased(final int x, final int y) {
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void paint(final Graphics g) {
    synchronized (Screen.paintsync) {
      final int anchor = Graphics.HCENTER | Graphics.TOP;
      g.setColor(0xFFFFFFFF);
      g.fillRect(0, 0, getWidth(), getHeight());
      yPos = drawTitle(g, "ALERT", yPos);
      //          if(getTicker()!=null)
      //          {
      //              yPos=yPos+drawTicker(g, yPos);
      //          }
      int y = yPos;
      final int x = 2;
      if (title != null) {
        g.setColor(0);
        g.drawString(title, x, y, Graphics.LEFT | Graphics.TOP);
        y += (Displayable.fontHeight + 5);
      }
      if (alertImage != null) {
        g.setClip(0, 0, getWidth(), 40);
        if ((anchor & Graphics.HCENTER) != 0) {
          g.drawImage(alertImage, ((getWidth() >> 1) - (alertImage.getWidth() >> 1)), y + 2, anchor);
        }
        else {
          g.drawImage(alertImage, x, y + 2, anchor);
        }
        y += 40;
      }
      if (alertText != null) {
        g.setColor(0);
        for (final Object line : lines) {
          if ((anchor & Graphics.HCENTER) != 0) {
            g.drawString((String)line, ((getWidth() >> 1) - (Screen.font.stringWidth(alertText) >> 1)),
                y, yPos);
          }
          else {
            g.drawString((String)line, x, y, yPos);
          }
        }
        y += (Displayable.fontHeight + 4);
      }
    }
  }

}
