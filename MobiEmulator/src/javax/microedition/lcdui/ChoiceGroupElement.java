/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.game.Sprite;
import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class ChoiceGroupElement {
    private int         borderColor         = 0;
    private int         checkBoxHeight      = 12;
    private int         checkBoxWidth       = 12;
    private int         gap                 = 2;
    private int         imageIconHeight     = 12;
    private int         imageIconWidth      = 12;
    private int         lineSpacing         = 2;
    private boolean     isCheckBoxesEnabled = true;
    private ChoiceGroup choiceGroup;
    public Font         font;
    public Image        imagePart;
    public boolean      isSelected;
    private ArrayList   lines;
    private int         maxWidth;
    private int[]       rect;
    public String       stringPart;
    public ChoiceGroupElement(String text, Image image, Font f, ChoiceGroup choiceGroup) {
        this.stringPart  = (text == null) ? ""
                                          : text;
        this.imagePart   = image;
        this.font        = (f == null) ? Displayable.font
                                       : f;
        this.choiceGroup = choiceGroup;
        this.rect        = new int[4];
        this.rect[2]     = checkBoxWidth;
        this.rect[3]     = checkBoxHeight;
        this.isSelected  = false;
        lines            = StringUtils.wrapToLines(stringPart, font, Displayable.SCREEN_WIDTH - 10);
    }

    public void setOptionSelected(boolean sel) {
        this.isSelected = sel;
    }
    public void setMaxWidth(int width) {
        maxWidth = width;
    }
    public void setXPos(int x) {
        rect[0] = x;
    }
    public int getWidth() {
        return Math.max(maxWidth, rect[3]);
    }
    public int getXPos() {
        return rect[0];
    }
    public boolean isSelected() {
        return this.isSelected;
    }
    public int getYPos() {
        return rect[1];
    }
    public int getHeight() {
        return rect[3];
    }
    public void setYPos(int y) {
        rect[1] = y;
    }
    public void setFont(Font f) {
        this.font = f;
    }
    public void setCheckBoxes(boolean isCheckBoxesEnabled) {
        this.isCheckBoxesEnabled = isCheckBoxesEnabled;
    }
    public void paint(Graphics g) {
        int x = rect[0];
        int y = rect[1];
        int w = rect[2];
        int h = rect[3];
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
            x += (imageIconWidth + 2 * gap);
        }
        if (lines.size() > 0) {
            for (int i = 0;i < lines.size();i++) {
                g.setFont(font);
                g.drawString((String) lines.get(i), x + (((i != 0) && (imagePart != null)) ? -imageIconWidth
                        : 0), y + (i * (font.getHeight() + lineSpacing)), Graphics.LEFT | Graphics.TOP);
            }
        }
        rect[0] = x;
        rect[1] = y;
        rect[2] = w;
        rect[3] = h;
    }
    public void doLayout() {
        lines   = StringUtils.wrapToLines(this.stringPart, font, maxWidth - (rect[0] + 12 + ((imagePart != null) ? 12
                : 0)));
        rect[3] = (lines.size() * (font.getHeight() + 4));
    }
}
