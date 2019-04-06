package javax.microedition.lcdui;

import java.util.ArrayList;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class ChoiceGroupElement {

  private final int borderColor = 0;
  private final int checkBoxHeight = 12;
  private final int checkBoxWidth = 12;
  private final int gap = 2;
  private final int imageIconHeight = 12;
  private final int imageIconWidth = 12;
  private final int lineSpacing = 2;
  private boolean isCheckBoxesEnabled = true;
  private final ChoiceGroup choiceGroup;
  public Font font;
  public Image imagePart;
  public boolean isSelected;
  private ArrayList lines;
  private int maxWidth;
  private final int[] rect;
  public String stringPart;

  public ChoiceGroupElement(final String text, final Image image, final Font f, final ChoiceGroup choiceGroup) {
    stringPart = (text == null) ? "" : text;
    imagePart = image;
    font = (f == null) ? Displayable.font : f;
    this.choiceGroup = choiceGroup;
    rect = new int[4];
    rect[2] = checkBoxWidth;
    rect[3] = checkBoxHeight;
    isSelected = false;
    lines = StringUtils.wrapToLines(stringPart, font, Displayable.SCREEN_WIDTH - 10);
  }

  public void setOptionSelected(final boolean sel) {
    isSelected = sel;
  }

  public void setMaxWidth(final int width) {
    maxWidth = width;
  }

  public void setXPos(final int x) {
    rect[0] = x;
  }

  public int getWidth() {
    return Math.max(maxWidth, rect[3]);
  }

  public int getXPos() {
    return rect[0];
  }

  public boolean isSelected() {
    return isSelected;
  }

  public int getYPos() {
    return rect[1];
  }

  public int getHeight() {
    return rect[3];
  }

  public void setYPos(final int y) {
    rect[1] = y;
  }

  public void setFont(final Font f) {
    font = f;
  }

  public void setCheckBoxes(final boolean isCheckBoxesEnabled) {
    this.isCheckBoxesEnabled = isCheckBoxesEnabled;
  }

  public void paint(final Graphics g) {
    int x = rect[0];
    final int y = rect[1];
    final int w = rect[2];
    final int h = rect[3];
    //      if(isSelected())
    //      {
    //          g.setColor(borderColor);
    //          g.drawRect(x,y,choiceGroup.getPreferredWidth(),h);
    //          x+=1;
    //          y+=1;
    ////          w-=1;
    ////          h-=1;
    //      }
    if (isCheckBoxesEnabled) {
      g.setColor(0);
      g.drawRect(x + gap, y + gap, checkBoxWidth, checkBoxHeight);
      if (isSelected()) {
        g.setColor(0);
        g.fillRect(x + gap + 2, y + gap + 2, checkBoxWidth - (gap << 1) - 4, checkBoxHeight - (gap << 1));
      }
      x += (checkBoxWidth + gap);
    }
    if (imagePart != null) {
      g.drawRegion(imagePart, 0, 0, imageIconWidth, imageIconHeight, Sprite.TRANS_NONE, x, y, 0);
      x += (imageIconWidth + (2 * gap));
    }
    if (lines.size() > 0) {
      for (int i = 0; i < lines.size(); i++) {
        g.setFont(font);
        g.drawString((String)lines.get(i), x + (((i != 0) && (imagePart != null)) ? -imageIconWidth : 0), y + (i * (font.getHeight() + lineSpacing)), Graphics.LEFT | Graphics.TOP);
      }
    }
    rect[0] = x;
    rect[1] = y;
    rect[2] = w;
    rect[3] = h;
  }

  public void doLayout() {
    lines = StringUtils.wrapToLines(stringPart, font, maxWidth - (rect[0] + 12 + ((imagePart != null) ? 12 : 0)));
    rect[3] = (lines.size() * (font.getHeight() + 4));
  }

}
