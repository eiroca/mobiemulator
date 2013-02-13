/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ImageItem extends Item {
    String altText       = "";
    Image  imgItem       = null;
    String label         = null;
    int    layout        = LAYOUT_DEFAULT;
    int    apperanceMode = Item.PLAIN;
    int    lineSpacing   = 4;
    public ImageItem(String label, Image img, int layout, String altText) {
        this(label, img, layout, altText, PLAIN);
    }

    public ImageItem(String label, Image image, int layout, String altText, int appearanceMode) {
        this.label         = label;
        this.imgItem       = image;
        this.layout        = layout;
        this.altText       = altText;
        this.apperanceMode = appearanceMode;
    }

    public int getAppearanceMode() {
        return apperanceMode;
    }
    public void setLayout(int layout) {
        this.layout = layout;
    }
    public int getLayout() {
        return layout;
    }
    public void setAltText(String text) {
        altText = text;
    }
    public String getAltText() {
        return altText;
    }
    public void setImage(Image img) {
        imgItem = img;
    }
    public Image getImage() {
        return this.imgItem;
    }
    @Override
    public void paint(Graphics g) {
        int       y      = yPos;
        int       x      = 2;
        int       anchor = Graphics.TOP | Graphics.LEFT;
        ArrayList lines  = StringUtils.wrapToLines(altText, font, getPreferredWidth());
        if (label != null) {
            g.setColor(0);
            g.setFont(Displayable.boldFont);
            g.drawString(label, 2, y, anchor);
            y += (Displayable.boldFontHeight + lineSpacing);
        }
        if (isSelected) {
            g.setColor(((apperanceMode == BUTTON) ? 0xAADDFF
                    : 0));
            if (imgItem.getWidth() > Displayable.SCREEN_WIDTH) {
                g.drawRect(x, y, font.stringWidth((String) lines.get(0)), fontHeight + 2);
            } else {
                g.drawRect(x, y, imgItem.getWidth(), imgItem.getHeight() + 2);
            }
            y += 2;
        }
        if (imgItem != null) {
            if (imgItem.getWidth() > Displayable.SCREEN_WIDTH) {
                g.setColor(0);
                g.setFont(font);
                g.drawString((altText != null) ? altText
                                               : "NULL", x, y, anchor);
                y += (fontHeight + lineSpacing);
            } else {
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
            y += (fontHeight + lineSpacing);
        } else {
            y += imgItem.getHeight() + 2;
        }
        itemHeight = y - yPos;
    }
    @Override
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            handledKey = false;    // true for consumed by this item it won't move up / down to next item

            break;
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            handledKey = false;

            break;
        case Displayable.KEY_FIRE :
        case Displayable.KEY_NUM5 :
            if (((apperanceMode == HYPERLINK) || (apperanceMode == BUTTON)) && (defualtCommand != null)) {
                super.invokeItemCommandAction(defualtCommand, this);
            }
            handledKey = true;

            break;
        }
    }
}
