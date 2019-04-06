package javax.microedition.lcdui;

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ChoiceGroup extends Item implements Choice {

  public Vector<ChoiceGroupElement> elements = new Vector<>();
  private final Font f = Item.font;
  public int highlightedIndex = -1;
  private final int xPadding = 2;
  private int previousIndex = highlightedIndex;
  private boolean isListExpanded = false;
  public boolean isList = false;
  private final int choiceType;
  public int h;
  private Image imgPart;
  private final String label;
  private int selectedIndex;
  private String strPart;

  public ChoiceGroup(final String label, final int choiceType) {
    this.label = label;
    this.choiceType = choiceType;
  }

  public ChoiceGroup(final String label, final int choiceType, final String[] stringElements, final Image[] imageElements) {
    this.label = label;
    this.choiceType = choiceType;
    for (int i = 0; i < stringElements.length; i++) {
      elements.add(new ChoiceGroupElement(stringElements[i], ((imageElements == null) ? null
          : imageElements[i]), f, this));
    }
  }

  @Override
  public int append(final String stringPart, final Image imagePart) {
    elements.add(new ChoiceGroupElement(stringPart, imagePart, null, this));

    return elements.size();
  }

  @Override
  public void delete(final int elementNum) {
    elements.remove(elementNum);
  }

  @Override
  public void deleteAll() {
    elements.removeAllElements();
  }

  @Override
  public int getFitPolicy() {
    System.out.println("* getFitPolicy not implemented yet*");

    return 0;
  }

  @Override
  public Font getFont(final int elementNum) {
    return elements.get(elementNum).font;
  }

  @Override
  public Image getImage(final int elementNum) {
    return elements.get(elementNum).imagePart;
  }

  @Override
  public int getSelectedFlags(final boolean[] b) {
    final int len = elements.size();
    int count = 0;
    for (int i = 0; i < len; i++) {
      if (elements.get(i).isSelected()) {
        b[i] = true;
        count++;
      }
    }

    return count;
  }

  @Override
  public int getSelectedIndex() {
    return selectedIndex;
  }

  @Override
  public String getString(final int elementNum) {
    return elements.get(elementNum).stringPart;
  }

  @Override
  public void insert(final int elementNum, final String stringPart, final Image imagePart) {
    elements.insertElementAt(new ChoiceGroupElement(stringPart, imagePart, null, this), elementNum);
  }

  @Override
  public boolean isSelected(final int elementNum) {
    return elements.get(elementNum).isSelected();
  }

  @Override
  public void set(final int elementNum, final String stringPart, final Image imagePart) {
    elements.set(elementNum, new ChoiceGroupElement(stringPart, imagePart, null, this));
  }

  @Override
  public void setFitPolicy(final int fitPolicy) {
    System.out.println("* setFitPolicy not implemented yet*");
  }

  public void setType(final boolean isList) {
    this.isList = isList;
  }

  @Override
  public void setFont(final int elementNum, final Font font) {
    elements.get(elementNum).setFont(f);
  }

  @Override
  public void setSelectedFlags(final boolean[] b) {
    if (elements.size() == 0) { return; }
    for (int i = 0; i < b.length; i++) {
      elements.get(i).setOptionSelected(b[i]);
    }
  }

  @Override
  public void setSelectedIndex(final int elementNum, final boolean selected) {
    elements.get(elementNum).setOptionSelected(selected);
    if (selected) {
      highlightedIndex = elementNum;
    }
  }

  @Override
  public int size() {
    return elements.size();
  }

  @Override
  public void paint(final Graphics g) {
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
      g.fillTriangle(x + w, y + 5, x + w + 9, y + 5, x + w + 5, (y + Displayable.boldFontHeight) - 3);
      if (isListExpanded) {

        /** highlighted option should be in combobox */
        elements.get(previousIndex).setCheckBoxes(false);
        elements.get(previousIndex).setXPos(x);
        elements.get(previousIndex).setYPos(y);
        elements.get(previousIndex).paint(g);
        // displayng dropdown box to bottom of combobox
        y += Displayable.boldFontHeight + 2;
        for (int i = 0; i < elements.size(); i++) {
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
      }
      else {
        highlightedIndex = (highlightedIndex != -1) ? highlightedIndex
            : 0;
        elements.get(highlightedIndex).setCheckBoxes(false);
        elements.get(highlightedIndex).setXPos(x);
        elements.get(highlightedIndex).setYPos(y);
        elements.get(highlightedIndex).paint(g);
        y += elements.get(highlightedIndex).getHeight();
      }
      w += 10;
    }
    else {
      for (int i = 0; i < elements.size(); i++) {
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

  private int getMaxWidh(final Vector items) {
    int maxWidth = 0;
    boolean hasImage = false;
    for (int i = 0; i < items.size(); i++) {
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
      y += (Item.fontHeight + 4);
    }
    for (final ChoiceGroupElement element : elements) {
      element.setYPos(y);
      element.setMaxWidth((choiceType == Choice.POPUP) ? Displayable.SCREEN_WIDTH
          : prefferedwidth);
      element.doLayout();
      y += element.getHeight();
    }
    itemHeight = y - yPos;
  }

  @Override
  public void handleKey(final int keyCode) {
    switch (keyCode) {
      case Displayable.KEY_UP_ARROW:
      case Displayable.KEY_NUM2:
        if (highlightedIndex > 0) {
          if (!isListExpanded && (choiceType == Choice.POPUP)) {
            handledKey = false;
          }
          else {
            highlightedIndex--;
            handledKey = true;
          }
        }
        else {
          handledKey = isListExpanded;
        }

        break;
      case Displayable.KEY_DOWN_ARROW:
      case Displayable.KEY_NUM8:
        if (highlightedIndex < (elements.size() - 1)) {
          if (!isListExpanded && (choiceType == Choice.POPUP)) {
            handledKey = false;
          }
          else {
            highlightedIndex++;
            handledKey = true;
          }
        }
        else {
          handledKey = isListExpanded;
        }

        break;
      case Displayable.KEY_FIRE:
      case Displayable.KEY_NUM5:
        if (highlightedIndex != -1) {
          if (choiceType == Choice.MULTIPLE) {
            elements.get(highlightedIndex).setOptionSelected(!elements.get(highlightedIndex).isSelected());
          }
          else if (choiceType == Choice.EXCLUSIVE) {
            boolean isAnyOptionSelected = false;
            for (final ChoiceGroupElement element : elements) {
              isAnyOptionSelected = element.isSelected();
            }
            if (!isAnyOptionSelected) {
              elements.get(highlightedIndex).setOptionSelected(true);
            }
          }
          else if (choiceType == Choice.POPUP) {
            previousIndex = highlightedIndex;
            isListExpanded = !isListExpanded;
          }
          handledKey = true;
        }
        else {
          handledKey = false;
        }

        break;
      default:
        handledKey = true;
    }
  }

}
