package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class List extends Screen implements Choice {

  public static final Command SELECT_COMMAND = new Command("", Command.SCREEN, 0);
  int curSelectedItemIndex = 0;
  int displayableHeight = 0;
  ChoiceGroup cg;
  private int pointerDraggedX;
  private int pointerDraggedY;
  private int pointerX;
  private int pointerY;
  private int scrollOffsetY;
  private int sliderHeight;
  private int sliderY;
  int tickerYPos;

  public List(final String title, final int listType) {
    cg = new ChoiceGroup(title, Choice.EXCLUSIVE);
  }

  public List(final String title) {
    cg = new ChoiceGroup(title, Choice.EXCLUSIVE);
  }

  public List(final String title, final int listType, final String[] stringElements, final Image[] imageElements) {
    cg = new ChoiceGroup(title, listType, stringElements, imageElements);
    cg.setSelectedIndex(curSelectedItemIndex, true);
  }

  @Override
  public int append(final String stringPart, final Image imagePart) {
    return cg.append(stringPart, imagePart);
  }

  @Override
  public void delete(final int elementNum) {
    cg.delete(elementNum);
  }

  @Override
  public void deleteAll() {
    cg.deleteAll();
  }

  @Override
  public int getFitPolicy() {
    return cg.getFitPolicy();
  }

  @Override
  public Font getFont(final int elementNum) {
    return cg.getFont(elementNum);
  }

  @Override
  public Image getImage(final int elementNum) {
    return cg.getImage(elementNum);
  }

  @Override
  public int getSelectedFlags(final boolean[] selectedArray_return) {
    return cg.getSelectedFlags(selectedArray_return);
  }

  @Override
  public int getSelectedIndex() {
    return cg.getSelectedIndex();
  }

  @Override
  public String getString(final int elementNum) {
    return cg.getString(elementNum);
  }

  @Override
  public Ticker getTicker() {
    return ticker;
  }

  @Override
  public void insert(final int elementNum, final String stringPart, final Image imagePart) {
    cg.insert(elementNum, stringPart, imagePart);
  }

  @Override
  public boolean isSelected(final int elementNum) {
    return cg.isSelected(elementNum);
  }

  @Override
  public void removeCommand(final Command cmd) {
    cg.removeCommand(cmd);
  }

  @Override
  public void set(final int elementNum, final String stringPart, final Image imagePart) {
    cg.set(elementNum, stringPart, imagePart);
  }

  @Override
  public void setFitPolicy(final int fitPolicy) {
    cg.setFitPolicy(fitPolicy);
  }

  @Override
  public void setFont(final int elementNum, final Font font) {
    cg.setFont(elementNum, font);
  }

  public void setSelectCommand(final Command command) {
    cg.setDefaultCommand(command);
  }

  @Override
  public void setSelectedFlags(final boolean[] selectedArray) {
    cg.setSelectedFlags(selectedArray);
  }

  @Override
  public void setSelectedIndex(final int elementNum, final boolean selected) {
    cg.setSelectedIndex(elementNum, selected);
  }

  @Override
  public void setTicker(final Ticker ticker) {
    this.ticker = ticker;
  }

  @Override
  public void setTitle(final String s) {
    cg.setLabel(s);
  }

  @Override
  public int size() {
    return cg.size();
  }

  @Override
  protected void paint(final Graphics g) {
    g.setColor(0xFFFFFFFF);
    g.fillRect(0, 0, getWidth(), getHeight());
    yPos = Font.getDefaultFont().getHeight() + 4; //
    final int tickerHeight = (Displayable.fontHeight + 5);
    if (getTicker() != null) {
      tickerYPos = Displayable.SCREEN_HEIGHT - tickerHeight;
      // yPos=yPos+(fontHeight+5);//
      // y=y+drawTicker(g, yPos);
    }
    cg.setType(true);
    final int y = yPos;
    cg.yPos = yPos + (scrollOffsetY);
    cg.paint(g);
    displayableHeight = (Displayable.SCREEN_HEIGHT - y - ((getTicker() != null) ? tickerHeight
        : 0));
    sliderHeight = Math.min(displayableHeight, (displayableHeight * displayableHeight) / cg.itemHeight);
    sliderY = (displayableHeight * (-scrollOffsetY + yPos)) / cg.itemHeight;
    cg.itemHeight += 4;
    if (cg.itemHeight > displayableHeight) {
      drawVerticalScrollbar(g, Displayable.SCREEN_WIDTH - 4, sliderY, 4, sliderHeight);
    }
    drawTitle(g, "LIST", 0);
    if (getTicker() != null) {
      drawTicker(g, tickerYPos);
    }
    if (showCommandMenu) {
      showCommandMenu(g);
    }
  }

  @Override
  protected void keyPressed(final int keyCode) {
    if (showCommandMenu) { return; }
    cg.handleKey(keyCode);
    updateList(keyCode);
  }

  @Override
  protected void keyRepeated(final int keyCode) {
    if (showCommandMenu) { return; }
    cg.handleKey(keyCode);
    updateList(keyCode);
  }

  @Override
  protected void keyReleased(final int keyCode) {
  }

  @Override
  protected void pointerPressed(final int x, final int y) {
    pointerX = x;
    pointerY = y;
    for (int i = 0; i < cg.elements.size(); i++) {
      final ChoiceGroupElement elem = cg.elements.get(i);
      if (((pointerY > elem.getYPos()) && (pointerY < (elem.getYPos() + elem.getHeight())))
          && ((pointerX > elem.getXPos()) && (pointerX < (elem.getXPos() + elem.getWidth())))) {
        cg.highlightedIndex = i;
        elem.isSelected = true;
      }
    }
  }

  @Override
  protected void pointerDragged(final int x, final int y) {
    pointerDraggedX = x;
    pointerDraggedY = y;
    if (pointerDraggedY < pointerY) // upwards
    {
      scrollOffsetY -= (pointerY - pointerDraggedY);
      if ((-scrollOffsetY + displayableHeight) >= (cg.itemHeight)) {
        scrollOffsetY = -(cg.itemHeight - displayableHeight) - 20;
      }
    }
    else if (pointerDraggedY > pointerY) // downwards
    {
      scrollOffsetY += (pointerDraggedY - pointerY);
      if (scrollOffsetY > 0) {
        scrollOffsetY = 0;
      }
    }
  }

  @Override
  protected void pointerReleased(final int x, final int y) {
  }

  public void updateList(final int keyCode) {
    final int itemsLen = cg.elements.size();
    switch (keyCode) {
      case KEY_UP_ARROW:
      case KEY_NUM2:
        cg.elements.get(curSelectedItemIndex).isSelected = false; // clearing previous selected
        if (curSelectedItemIndex > 0) {
          curSelectedItemIndex--;
        }
        // scrolling according to item
        final ChoiceGroupElement curitem = cg.elements.get(curSelectedItemIndex);
        if (scrollOffsetY < 0) {
          if ((-scrollOffsetY) > (curitem.getYPos())) // cg.elements is up side totally need to move down by item height
          {
            scrollOffsetY += curitem.getHeight();
          }
          else if (((-scrollOffsetY) > curitem.getYPos())
              && ((-scrollOffsetY) < (curitem.getYPos() + curitem.getHeight()))) // item is visible
          {
            // semi visible
            scrollOffsetY += (displayableHeight); // moving down with displayHeight (Screen Height)
            if (scrollOffsetY > 0) { // beyond formheight
              scrollOffsetY = 0;
            }
          }
          else if ((-scrollOffsetY + yPos) >= curitem.getYPos()) {
            scrollOffsetY = 0;
          }
        }

        break;
      case KEY_DOWN_ARROW:
      case KEY_NUM8:
        cg.elements.get(curSelectedItemIndex).isSelected = false;
        if (curSelectedItemIndex < (itemsLen - 1)) {
          curSelectedItemIndex++;
        }
        final ChoiceGroupElement item = cg.elements.get(curSelectedItemIndex);
        if (-scrollOffsetY < (cg.itemHeight - displayableHeight)) {
          if ((-scrollOffsetY + displayableHeight) < (item.getYPos() - yPos)) // cg.elements is down side need to move up by item height
          {
            scrollOffsetY -= item.getHeight();
          }
          else if (((-scrollOffsetY + displayableHeight) > (item.getYPos() - yPos))
              && ((-scrollOffsetY + displayableHeight) < ((item.getYPos() + item.getHeight()) - yPos))) // item is visible
          {
            // semi visible
            scrollOffsetY -= (displayableHeight); // moving up with displayHeight (Screen)
            if ((-scrollOffsetY + displayableHeight) > cg.itemHeight) { // beyond formheight
              scrollOffsetY = -(cg.itemHeight - displayableHeight);
            }
          }
        }

        break;
      case KEY_LEFT_ARROW:
      case KEY_NUM4:
        break;
      case KEY_RIGHT_ARROW:
      case KEY_NUM6:
        break;
    }
    cg.elements.get(curSelectedItemIndex).isSelected = true;
  }

  public void deIntialize() {
    cg.elements = null;
    cg = null;
    ticker = null;
    super.deInit();
  }

}
