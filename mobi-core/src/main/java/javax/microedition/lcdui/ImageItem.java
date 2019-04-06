package javax.microedition.lcdui;

import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ImageItem extends Item {

  String altText = "";
  Image imgItem = null;
  String label = null;
  int layout = Item.LAYOUT_DEFAULT;
  int apperanceMode = Item.PLAIN;
  int lineSpacing = 4;

  public ImageItem(final String label, final Image img, final int layout, final String altText) {
    this(label, img, layout, altText, Item.PLAIN);
  }

  public ImageItem(final String label, final Image image, final int layout, final String altText, final int appearanceMode) {
    this.label = label;
    imgItem = image;
    this.layout = layout;
    this.altText = altText;
    apperanceMode = appearanceMode;
  }

  public int getAppearanceMode() {
    return apperanceMode;
  }

  @Override
  public void setLayout(final int layout) {
    this.layout = layout;
  }

  @Override
  public int getLayout() {
    return layout;
  }

  public void setAltText(final String text) {
    altText = text;
  }

  public String getAltText() {
    return altText;
  }

  public void setImage(final Image img) {
    imgItem = img;
  }

  public Image getImage() {
    return imgItem;
  }

  @Override
  public void paint(final Graphics g) {
    int y = yPos;
    final int x = 2;
    final int anchor = Graphics.TOP | Graphics.LEFT;
    final ArrayList lines = StringUtils.wrapToLines(altText, Item.font, getPreferredWidth());
    if (label != null) {
      g.setColor(0);
      g.setFont(Displayable.boldFont);
      g.drawString(label, 2, y, anchor);
      y += (Displayable.boldFontHeight + lineSpacing);
    }
    if (isSelected) {
      g.setColor(((apperanceMode == Item.BUTTON) ? 0xAADDFF
          : 0));
      if (imgItem.getWidth() > Displayable.SCREEN_WIDTH) {
        g.drawRect(x, y, Item.font.stringWidth((String)lines.get(0)), Item.fontHeight + 2);
      }
      else {
        g.drawRect(x, y, imgItem.getWidth(), imgItem.getHeight() + 2);
      }
      y += 2;
    }
    if (imgItem != null) {
      if (imgItem.getWidth() > Displayable.SCREEN_WIDTH) {
        g.setColor(0);
        g.setFont(Item.font);
        g.drawString((altText != null) ? altText
            : "NULL", x, y, anchor);
        y += (Item.fontHeight + lineSpacing);
      }
      else {
        g.drawImage(imgItem, x, y, anchor);
      }
    }
  }

  @Override
  public void doLayout() {
    int y = yPos;
    if (label != null) {
      y += (Displayable.boldFontHeight + lineSpacing);
    }
    if ((imgItem != null) && (imgItem.getWidth() > Displayable.SCREEN_WIDTH)) {
      y += (Item.fontHeight + lineSpacing);
    }
    else {
      y += imgItem.getHeight() + 2;
    }
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
        if (((apperanceMode == Item.HYPERLINK) || (apperanceMode == Item.BUTTON)) && (defualtCommand != null)) {
          super.invokeItemCommandAction(defualtCommand, this);
        }
        handledKey = true;
        break;
    }
  }

}
