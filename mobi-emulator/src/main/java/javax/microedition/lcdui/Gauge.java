package javax.microedition.lcdui;

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Gauge extends Item {

  public static final int CONTINUOUS_IDLE = 0;
  public static final int CONTINUOUS_RUNNING = 2;
  public static final int INCREMENTAL_IDLE = 1;
  public static final int INCREMENTAL_UPDATING = 3;
  public static final int INDEFINITE = -1;
  private final Vector commands = new Vector();
  private int currentValue;
  private int height;
  private int intialValue;
  private final boolean isInteractive;
  private String label;
  private int layout;
  private int maxValue;
  private int width;

  public Gauge(final String label, final boolean interactive, final int maxValue, final int initialValue) {
    this.label = label;
    isInteractive = interactive;
    if (intialValue < 0) {
      intialValue = 0;
    }
    this.maxValue = maxValue;
    currentValue = intialValue = initialValue;
  }

  @Override
  public void setLabel(final String label) {
    this.label = label;
  }

  @Override
  public void setLayout(final int layout) {
    this.layout = layout;
  }

  @Override
  public void addCommand(final Command cmd) {
    commands.add(cmd);
  }

  @Override
  public void setItemCommandListener(final ItemCommandListener l) {
  }

  @Override
  public void setPreferredSize(final int width, final int height) {
    this.width = width;
    this.height = width;
  }

  @Override
  public void setDefaultCommand(final Command cmd) {
  }

  public void setValue(final int value) {
    currentValue = value;
  }

  public int getValue() {
    return currentValue;
  }

  public void setMaxValue(final int maxValue) {
    this.maxValue = maxValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public boolean isInteractive() {
    return isInteractive;
  }

  @Override
  public void paint(final Graphics g) {
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
    if (layout == Gauge.INCREMENTAL_UPDATING) {
      percentFiled = ((itemWidth - 3) / maxValue) * currentValue;
    }
    else if ((layout == Gauge.INCREMENTAL_IDLE) || (layout == Gauge.CONTINUOUS_IDLE)) {
      percentFiled = ((itemWidth - 3) / maxValue) * maxValue;
    }
    else if (layout == Gauge.CONTINUOUS_RUNNING) {
      currentValue += 5;
      if (currentValue > maxValue) {
        currentValue = 0;
      }
      percentFiled = ((itemWidth - 3) / maxValue) * currentValue;
    }
    drawTriGradientRectangle(g, 0, 185, 0, 0, 129, 0, 5, y + 1, percentFiled, h - 3, h - 3, true);
    // g.fillRoundRect(5, y+1,percentFiled , h-3, 8, 8);
    y += h;
    itemHeight = y - yPos;
  }

  @Override
  public void doLayout() {
    itemWidth = (width == 0) ? getPreferredWidth()
        : width;
    height = (height == 0) ? 20
        : height;
    itemHeight = ((height > (getPreferredHeight() - height)) ? (getPreferredHeight() - height)
        : height);
  }

  @Override
  public void handleKey(final int keyCode) {
    switch (keyCode) {
      case Displayable.KEY_UP_ARROW:
      case Displayable.KEY_NUM2:
        handledKey = false;

        break;
      case Displayable.KEY_DOWN_ARROW:
      case Displayable.KEY_NUM8:
        handledKey = false;

        break;
      case Displayable.KEY_LEFT_ARROW:
      case Displayable.KEY_NUM4:
        if (layout == Gauge.INCREMENTAL_UPDATING) {
          currentValue -= 5;
          if (currentValue < 0) {
            currentValue = 0;
          }
        }
        handledKey = true;

        break;
      case Displayable.KEY_RIGHT_ARROW:
      case Displayable.KEY_NUM6:
        if (layout == Gauge.INCREMENTAL_UPDATING) {
          currentValue += 5;
          if (currentValue > maxValue) {
            currentValue = maxValue;
          }
        }
        handledKey = true;

        break;
    }
  }

  private void drawTriGradientRectangle(final Graphics g, final int r1, final int g1, final int b1, final int r2, final int g2, final int b2, final int xpos, final int ypos, final int width, final int height, final int spread, final boolean horizontal) {
    int diff = 0;
    int c1 = 0;
    int c2 = 0;
    int c3 = 0;
    final int x = xpos;
    final int y = ypos;
    final int halfWidth = width >> 1;
    final int halfHeight = height >> 1;
    int halfsize = 0;
    if (horizontal) {
      halfsize = halfHeight;
    }
    else {
      halfsize = halfWidth;
    }
    for (int i = 0; i < (halfsize + 1); i++) {
      if ((halfsize - i) > 0) {
        diff = (halfsize - i) * spread;
      }
      else {
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
        g.drawLine(x, (y + height) - i, x + width, (y + height) - i);
      }
      else {
        g.drawLine(x + i, y, x + i, y + height);
        g.drawLine((x + width) - i, y, (x + width) - i, y + height);
      }
    }
    if (horizontal) {
      g.drawLine(x, y + halfHeight, x + width, y + halfHeight);
    }
    else {
      g.drawLine(x + halfWidth, y, x + halfWidth, y + height);
    }
  }

}
