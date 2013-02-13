/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Gauge extends Item {
    public static final int CONTINUOUS_IDLE      = 0;
    public static final int CONTINUOUS_RUNNING   = 2;
    public static final int INCREMENTAL_IDLE     = 1;
    public static final int INCREMENTAL_UPDATING = 3;
    public static final int INDEFINITE           = -1;
    private Vector          commands             = new Vector();
    private int             currentValue;
    private int             height;
    private int             intialValue;
    private boolean         isInteractive;
    private String          label;
    private int             layout;
    private int             maxValue;
    private int             width;
    public Gauge(String label, boolean interactive, int maxValue, int initialValue) {
        this.label         = label;
        this.isInteractive = interactive;
        if (intialValue < 0) {
            intialValue = 0;
        }
        this.maxValue     = maxValue;
        this.currentValue = this.intialValue = initialValue;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public void setLayout(int layout) {
        this.layout = layout;
    }
    public void addCommand(Command cmd) {
        commands.add(cmd);
    }
    public void setItemCommandListener(ItemCommandListener l) {}
    public void setPreferredSize(int width, int height) {
        this.width  = width;
        this.height = width;
    }
    public void setDefaultCommand(Command cmd) {}
    public void setValue(int value) {
        currentValue = value;
    }
    public int getValue() {
        return currentValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public int getMaxValue() {
        return this.maxValue;
    }
    public boolean isInteractive() {
        return isInteractive;
    }
    @Override
    public void paint(Graphics g) {
        int y = yPos;
        int h = height;
        if (isSelected) {
            g.setColor(0);
            g.drawRect(2, y, itemWidth, ((label != null) ? (Displayable.boldFontHeight + 4 + h + 4)
                    : h + 2));
            y += 2;
            h += 4;
        }
        if (currentValue > maxValue) {
            currentValue = maxValue;
        }
        if (label != null) {
            g.setColor(0);
            g.setFont(Displayable.boldFont);
            g.drawString(label, 2, y, Graphics.TOP | Graphics.LEFT);
            y += (Displayable.boldFontHeight + 4);
        }
        g.setColor(0);
        g.drawRect(4, y, itemWidth - 2, h - 2);
        g.setColor(0x00F300);
        int percentFiled = 0;
        if (layout == INCREMENTAL_UPDATING) {
            percentFiled = ((itemWidth - 3) / maxValue) * currentValue;
        } else if ((layout == INCREMENTAL_IDLE) || (layout == CONTINUOUS_IDLE)) {
            percentFiled = ((itemWidth - 3) / maxValue) * maxValue;
        } else if (layout == CONTINUOUS_RUNNING) {
            currentValue += 5;
            if (currentValue > maxValue) {
                currentValue = 0;
            }
            percentFiled = ((itemWidth - 3) / maxValue) * currentValue;
        }
        drawTriGradientRectangle(g, 0, 185, 0, 0, 129, 0, 5, y + 1, percentFiled, h - 3, h - 3, true);
        // g.fillRoundRect(5, y+1,percentFiled , h-3, 8, 8);
        y          += h;
        itemHeight = y - yPos;
    }
    @Override
    public void doLayout() {
        itemWidth  = (width == 0) ? getPreferredWidth()
                                  : width;
        height     = (height == 0) ? 20
                                   : height;
        itemHeight = ((height > (getPreferredHeight() - height)) ? (getPreferredHeight() - height)
                : height);
    }
    @Override
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            handledKey = false;

            break;
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            handledKey = false;

            break;
        case Displayable.KEY_LEFT_ARROW :
        case Displayable.KEY_NUM4 :
            if (layout == INCREMENTAL_UPDATING) {
                currentValue -= 5;
                if (currentValue < 0) {
                    currentValue = 0;
                }
            }
            handledKey = true;

            break;
        case Displayable.KEY_RIGHT_ARROW :
        case Displayable.KEY_NUM6 :
            if (layout == INCREMENTAL_UPDATING) {
                currentValue += 5;
                if (currentValue > maxValue) {
                    currentValue = maxValue;
                }
            }
            handledKey = true;

            break;
        }
    }
    private void drawTriGradientRectangle(Graphics g, int r1, int g1, int b1, int r2, int g2, int b2, int xpos,
            int ypos, int width, int height, int spread, boolean horizontal) {
        int diff       = 0;
        int c1         = 0;
        int c2         = 0;
        int c3         = 0;
        int x          = xpos;
        int y          = ypos;
        int halfWidth  = width >> 1;
        int halfHeight = height >> 1;
        int halfsize   = 0;
        if (horizontal) {
            halfsize = halfHeight;
        } else {
            halfsize = halfWidth;
        }
        for (int i = 0;i < halfsize + 1;i++) {
            if ((halfsize - i) > 0) {
                diff = (halfsize - i) * spread;
            } else {
                diff = (i - halfsize) * spread;
            }
            c1 = r1 - diff;
            if (c1 < 0) {
                c1 = r2;
            }
            c2 = g1 - diff;
            if (c2 < 0) {
                c2 = g2;
            }
            c3 = b1 - diff;
            if (c3 < 0) {
                c3 = b2;
            }
            g.setColor(c1, c2, c3);
            if (horizontal) {
                g.drawLine(x, y + i, x + width, y + i);
                g.drawLine(x, y + height - i, x + width, y + height - i);
            } else {
                g.drawLine(x + i, y, x + i, y + height);
                g.drawLine(x + width - i, y, x + width - i, y + height);
            }
        }
        if (horizontal) {
            g.drawLine(x, y + halfHeight, x + width, y + halfHeight);
        } else {
            g.drawLine(x + halfWidth, y, x + halfWidth, y + height);
        }
    }
}
