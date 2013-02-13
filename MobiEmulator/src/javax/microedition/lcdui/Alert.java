/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MidletUtils;
import java.util.ArrayList;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Alert extends Screen {
    private int             AlertTimeout    = 100;
    private Image           alertImage      = null;
    private String          alertText       = null;
    private AlertType       alertType       = AlertType.FOREVER;
    private Command         DISMISS_COMMAND = new Command("DISMISS", Command.BACK, 0);
    private ArrayList       lines           = new ArrayList();
    private CommandListener cmdListener;
    private Gauge           guageIndicator;
    private int             height;
    private Displayable     previousDisplay;
    private String          title;
    private int             width;
    public Alert(String title) {
        this.title  = title;
        this.width  = MidletUtils.getInstance().getMidletListener().getCanvasWidth();
        this.height = MidletUtils.getInstance().getMidletListener().getCanvasHeight();
    }

    public Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        this.title      = title;
        this.alertText  = alertText;
        this.alertImage = alertImage;
        this.alertType  = alertType;
        this.width      = MidletUtils.getInstance().getMidletListener().getCanvasWidth();
        this.height     = MidletUtils.getInstance().getMidletListener().getCanvasHeight();
        previousDisplay = Display.display.getCurrent();
        if (alertText == null) {
            alertText = "";
        }
        lines = StringUtils.wrapToLines(alertText, font, width - 10);
        addCommand(DISMISS_COMMAND);
    }

    public int getDefaultTimeout() {
        return AlertTimeout;
    }
    public int getTimeout() {
        return AlertTimeout;
    }
    public void setTimeout(int time) {
        AlertTimeout = time;
    }
    public AlertType getType() {
        return alertType;
    }
    public void setType(AlertType type) {
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
        lines     = StringUtils.wrapToLines(alertText, font, width - 10);
    }
    public Image getImage() {
        return alertImage;
    }
    public void setImage(Image img) {
        alertImage = img;
    }
    public void setIndicator(Gauge indicator) {
        guageIndicator = indicator;
    }
    public Gauge getIndicator() {
        return guageIndicator;
    }
    @Override
    public void addCommand(Command cmd) {
        if (cmd == DISMISS_COMMAND) {
            return;
        }
        removeCommand(DISMISS_COMMAND);
        this.addCommand(cmd);
    }
    public void removeCommand(Command cmd) {
        if (getCommandsCount() == 0) {
            addCommand(DISMISS_COMMAND);
        }
    }
    @Override
    public void setCommandListener(CommandListener l) {
        this.cmdListener = l;
    }
    @Override
    protected void keyPressed(int keyCode) {}
    @Override
    protected void keyRepeated(int keyCode) {}
    @Override
    protected void keyReleased(int keyCode) {}
    @Override
    protected void pointerPressed(int x, int y) {}
    @Override
    protected void pointerDragged(int x, int y) {}
    @Override
    protected void pointerReleased(int x, int y) {}
    @Override
    public int getWidth() {
        return width;
    }
    @Override
    public int getHeight() {
        return height;
    }
    public void paint(Graphics g) {
        synchronized (Screen.paintsync) {
            int anchor = Graphics.HCENTER | Graphics.TOP;
            g.setColor(0xFFFFFFFF);
            g.fillRect(0, 0, getWidth(), getHeight());
            yPos = drawTitle(g, "ALERT", yPos);
//          if(getTicker()!=null)
//          {
//              yPos=yPos+drawTicker(g, yPos);
//          }
            int y = yPos;
            int x = 2;
            if (title != null) {
                g.setColor(0);
                g.drawString(title, x, y, Graphics.LEFT | Graphics.TOP);
                y += (fontHeight + 5);
            }
            if (alertImage != null) {
                g.setClip(0, 0, getWidth(), 40);
                if ((anchor & Graphics.HCENTER) != 0) {
                    g.drawImage(alertImage, ((getWidth() >> 1) - (alertImage.getWidth() >> 1)), y + 2, anchor);
                } else {
                    g.drawImage(alertImage, x, y + 2, anchor);
                }
                y += 40;
            }
            if (alertText != null) {
                g.setColor(0);
                for (Object line:lines) {
                    if ((anchor & Graphics.HCENTER) != 0) {
                        g.drawString((String) line, ((getWidth() >> 1) - (font.stringWidth(alertText) >> 1)),
                                     y, yPos);
                    } else
                        g.drawString((String) line, x, y, yPos);
                    }
                    y += (fontHeight + 4);
                }
            }
        }
    }

