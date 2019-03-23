package javax.microedition.lcdui;

import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class StringItem extends Item {

  int lineSpacing = 4;
  private final int strItemHeight = 0;
  private final int strItemWidth = 0;
  private String strText = null;
  private int apperanceMode;
  private final String label;
  private Font renderFont;

  public StringItem(final String label, final String text) {
    this.label = label;
    strText = text;
    renderFont = Displayable.font;
  }

  public StringItem(final String label, final String text, final int appearanceMode) {
    this.label = label;
    strText = text;
    apperanceMode = appearanceMode;
    renderFont = Font.getDefaultFont();
  }

  public String getText() {
    return strText;
  }

  public void setText(final String text) {
    strText = text;
  }

  public int getAppearanceMode() {
    return apperanceMode;
  }

  public void setFont(final Font font) {
    renderFont = font;
  }

  public Font getFont() {
    return renderFont;
  }

  @Override
  public void setPreferredSize(final int width, final int height) {
    if ((width == -1) || (height == -1)) { throw new IllegalArgumentException(); }
    super.setPreferredSize(width, height);
  }

  @Override
  public void paint(final Graphics g) {
    int y = yPos;
    final int x = 2;
    ArrayList lines = new ArrayList();
    if (strText != null) {
      lines = StringUtils.wrapToLines(strText, renderFont, getPreferredWidth() - 8);
    }
    if (isSelected) {
      g.setColor(0);
      g.drawRect(x - 1, y, getPreferredWidth(), ((label != null) ? (Displayable.boldFontHeight + lineSpacing) : 0) + (lines.size() * (renderFont.getHeight() + lineSpacing)) + 2);
    }
    if (label != null) {
      g.setColor(0);
      g.setFont(Displayable.boldFont);
      g.drawString(label, x, y, Graphics.LEFT | Graphics.TOP);
      y += (Displayable.boldFontHeight + 4);
    }
    g.setColor(0);
    g.setFont(Item.font);
    if ((apperanceMode == Item.BUTTON) && (defualtCommand != null)) {
      // drawing button
      g.setColor(0xAADDFF);
      g.fillRoundRect(x, y - 2, renderFont.stringWidth((String)lines.get(0)) + 2, renderFont.getHeight() + 2, 8,
          8);
    }
    // drawing single line
    g.setColor(0);
    g.drawString((String)lines.get(0), x + 2, y + 2, Graphics.LEFT | Graphics.TOP);
    if ((apperanceMode == Item.HYPERLINK) && (defualtCommand != null)) {
      g.setColor(0);
      g.drawLine(x + 2, y + renderFont.getBaselinePosition(), renderFont.stringWidth((String)lines.get(0)) + 2, y + renderFont.getBaselinePosition());
    }
    y += (renderFont.getHeight() + lineSpacing);
    if (isSelected) {
      y += 2;
    }
    itemHeight = y - yPos;
  }

  @Override
  public void doLayout() {
    int y = yPos;
    if (label != null) {
      y += (Displayable.boldFontHeight + 4);
    }
    if (strText != null) {
      y += (renderFont.getHeight() + lineSpacing);
    }
    y += 4;
    itemHeight = y - yPos;
  }

  @Override
  public void handleKey(final int keyCode) {
    switch (keyCode) {
      case Displayable.KEY_UP_ARROW:
      case Displayable.KEY_NUM2:
        handledKey = false; // true for consumed by this item it won't move up / down to next item
        break;
      case Displayable.KEY_DOWN_ARROW:
      case Displayable.KEY_NUM8:
        handledKey = false;
        break;
      case Displayable.KEY_FIRE:
      case Displayable.KEY_NUM5:
        super.invokeItemCommandAction(defualtCommand, this);
        handledKey = true;
        break;
    }
  }

}
