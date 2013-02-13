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
public class ChoiceGroup extends Item implements Choice {
    public Vector<ChoiceGroupElement> elements         = new Vector<ChoiceGroupElement>();
    private Font                      f                = font;
    public int                        highlightedIndex = -1;
    private int                       xPadding         = 2;
    private int                       previousIndex    = highlightedIndex;
    private boolean                   isListExpanded   = false;
    public boolean                    isList           = false;
    private int                       choiceType;
    public int                        h;
    private Image                     imgPart;
    private String                    label;
    private int                       selectedIndex;
    private String                    strPart;
    public ChoiceGroup(String label, int choiceType) {
        this.label      = label;
        this.choiceType = choiceType;
    }

    public ChoiceGroup(String label, int choiceType, String[] stringElements, Image[] imageElements) {
        this.label      = label;
        this.choiceType = choiceType;
        for (int i = 0;i < stringElements.length;i++) {
            elements.add(new ChoiceGroupElement(stringElements[i], ((imageElements == null) ? null
                    : imageElements[i]), f, this));
        }
    }

    public int append(String stringPart, Image imagePart) {
        this.elements.add(new ChoiceGroupElement(stringPart, imagePart, null, this));

        return this.elements.size();
    }
    public void delete(int elementNum) {
        this.elements.remove(elementNum);
    }
    public void deleteAll() {
        this.elements.removeAllElements();
    }
    public int getFitPolicy() {
        System.out.println("* getFitPolicy not implemented yet*");

        return 0;
    }
    public Font getFont(int elementNum) {
        return elements.get(elementNum).font;
    }
    public Image getImage(int elementNum) {
        return this.elements.get(elementNum).imagePart;
    }
    public int getSelectedFlags(boolean[] b) {
        int len   = elements.size();
        int count = 0;
        for (int i = 0;i < len;i++) {
            if (elements.get(i).isSelected()) {
                b[i] = true;
                count++;
            }
        }

        return count;
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public String getString(int elementNum) {
        return this.elements.get(elementNum).stringPart;
    }
    public void insert(int elementNum, String stringPart, Image imagePart) {
        elements.insertElementAt(new ChoiceGroupElement(stringPart, imagePart, null, this), elementNum);
    }
    public boolean isSelected(int elementNum) {
        return elements.get(elementNum).isSelected();
    }
    public void set(int elementNum, String stringPart, Image imagePart) {
        elements.set(elementNum, new ChoiceGroupElement(stringPart, imagePart, null, this));
    }
    public void setFitPolicy(int fitPolicy) {
        System.out.println("* setFitPolicy not implemented yet*");
    }
    public void setType(boolean isList) {
        this.isList = isList;
    }
    public void setFont(int elementNum, Font font) {
        elements.get(elementNum).setFont(f);
    }
    public void setSelectedFlags(boolean[] b) {
        if (elements.size() == 0) {
            return;
        }
        for (int i = 0;i < b.length;i++) {
            elements.get(i).setOptionSelected(b[i]);
        }
    }
    public void setSelectedIndex(int elementNum, boolean selected) {
        elements.get(elementNum).setOptionSelected(selected);
        if (selected) {
            highlightedIndex = elementNum;
        }
    }
    public int size() {
        return this.elements.size();
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(0);
        int y = yPos;
        int x = xPadding;
        int w = 0;
        if (label != null) {
            g.setFont(Displayable.boldFont);
            g.drawString(label, xPadding, y, Graphics.TOP | Graphics.LEFT);
            if (choiceType != Choice.POPUP) {
                y += (Displayable.boldFontHeight + 4);
            }
        }
        if (choiceType == Choice.POPUP) {
            x += ((Displayable.boldFont.stringWidth(label)) + 2);
            // System.out.println("w is "+w);
        }
        w = getMaxWidh(elements) + 10;
        if (choiceType == Choice.POPUP) {
            // u can add icon before combobox
            g.setColor(0);
            g.drawRect(x, y, ((w > (Displayable.SCREEN_WIDTH - w)) ? (Displayable.SCREEN_WIDTH - w)
                    : (w + 10)), Displayable.boldFontHeight + 2);
            g.setColor(0);
            g.fillTriangle(x + w, y + 5, x + w + 9, y + 5, x + w + 5, y + Displayable.boldFontHeight - 3);
            if (isListExpanded) {

                /** highlighted option should be in combobox */
                elements.get(previousIndex).setCheckBoxes(false);
                elements.get(previousIndex).setXPos(x);
                elements.get(previousIndex).setYPos(y);
                elements.get(previousIndex).paint(g);
                // displayng dropdown box to bottom of combobox
                y += Displayable.boldFontHeight + 2;
                for (int i = 0;i < elements.size();i++) {
                    elements.get(i).setCheckBoxes(false);
                    elements.get(i).setXPos(x);
                    elements.get(i).setYPos(y);
                    if (i == highlightedIndex) {
                        g.setColor(0X0000F6);
                        elements.get(i).doLayout();
                        g.fillRect(x, y, w + 1, elements.get(i).getHeight());
                    }
                    g.setColor(0);
                    elements.get(i).paint(g);
                    y += elements.get(i).getHeight();
                }
                g.setColor(0);
                g.drawRect(x, yPos + (Displayable.boldFontHeight + 2), w, y - (yPos + Displayable.boldFontHeight + 2));
                y += 2;
            } else {
                highlightedIndex = (highlightedIndex != -1) ? highlightedIndex
                        : 0;
                elements.get(highlightedIndex).setCheckBoxes(false);
                elements.get(highlightedIndex).setXPos(x);
                elements.get(highlightedIndex).setYPos(y);
                elements.get(highlightedIndex).paint(g);
                y += elements.get(highlightedIndex).getHeight();
            }
            w += 10;
        } else {
            for (int i = 0;i < elements.size();i++) {
                if ((i == highlightedIndex) && (highlightedIndex != -1)) {
                    y = y + 4;
                }
                elements.get(i).setCheckBoxes(!isList);
                elements.get(i).setYPos(y);
                elements.get(i).setXPos((choiceType != Choice.POPUP) ? (xPadding << 1)
                        : x);
                elements.get(i).paint(g);
                if (i == highlightedIndex) {
                    g.drawRect(xPadding, y - 2, getPreferredWidth() - (xPadding << 1), elements.get(i).getHeight() + 4);
                    y += 2;
                }
                y += elements.get(i).getHeight();
                w += 10;
            }
        }
        y += 4;
        if (isSelected) {
            g.setColor(0);
            g.drawRect(xPadding, yPos - 2, (x + w), (y - yPos) + 6);
            y += 8;
        }
        itemHeight = y - yPos;
    }
    private int getMaxWidh(Vector items) {
        int     maxWidth = 0;
        boolean hasImage = false;
        for (int i = 0;i < items.size();i++) {
            if (!hasImage && (elements.get(i).imagePart != null)) {
                hasImage = true;
            }
            // System.out.println("string is "+elements.get(i).stringPart+" "+elements.get(i).font.stringWidth(elements.get(i).stringPart));
            maxWidth = Math.max(elements.get(i).font.stringWidth(elements.get(i).stringPart), maxWidth);
        }

        return maxWidth + (hasImage ? 12
                                    : 0);
    }
    @Override
    public void doLayout() {
        int y = yPos;
        if (label != null) {
            y += (fontHeight + 4);
        }
        for (ChoiceGroupElement element : elements) {
            element.setYPos(y);
            element.setMaxWidth((choiceType == Choice.POPUP) ? Displayable.SCREEN_WIDTH
                    : prefferedwidth);
            element.doLayout();
            y += element.getHeight();
        }
        itemHeight = y - yPos;
    }
    public void handleKey(int keyCode) {
        switch (keyCode) {
        case Displayable.KEY_UP_ARROW :
        case Displayable.KEY_NUM2 :
            if (highlightedIndex > 0) {
                if (!isListExpanded && (choiceType == Choice.POPUP)) {
                    handledKey = false;
                } else {
                    highlightedIndex--;
                    handledKey = true;
                }
            } else {
                handledKey = isListExpanded;
            }

            break;
        case Displayable.KEY_DOWN_ARROW :
        case Displayable.KEY_NUM8 :
            if (highlightedIndex < elements.size() - 1) {
                if (!isListExpanded && (choiceType == Choice.POPUP)) {
                    handledKey = false;
                } else {
                    highlightedIndex++;
                    handledKey = true;
                }
            } else {
                handledKey = isListExpanded;
            }

            break;
        case Displayable.KEY_FIRE :
        case Displayable.KEY_NUM5 :
            if (highlightedIndex != -1) {
                if (choiceType == Choice.MULTIPLE) {
                    elements.get(highlightedIndex).setOptionSelected(!elements.get(highlightedIndex).isSelected());
                } else if (choiceType == Choice.EXCLUSIVE) {
                    boolean isAnyOptionSelected = false;
                    for (ChoiceGroupElement element : elements) {
                        isAnyOptionSelected = element.isSelected();
                    }
                    if (!isAnyOptionSelected) {
                        elements.get(highlightedIndex).setOptionSelected(true);
                    }
                } else if (choiceType == Choice.POPUP) {
                    previousIndex  = highlightedIndex;
                    isListExpanded = !isListExpanded;
                }
                handledKey = true;
            } else {
                handledKey = false;
            }

            break;
        default :
            handledKey = true;
        }
    }
}
