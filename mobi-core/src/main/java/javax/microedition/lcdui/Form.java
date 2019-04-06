package javax.microedition.lcdui;

import java.util.Vector;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Form extends Screen {

  private int curSelectedItemIndex = 0;
  int displayableHeight = 0;
  private Vector<Item> items = new Vector();
  private int pointerX = 0;
  private int pointerY = 0;
  public int formHeight;
  private String formTitle;
  public int formWidth;
  private ItemStateListener iStateListener;
  private int pointerDraggedX;
  private int pointerDraggedY;
  private int previousSelectedIndex;
  private int scrollOffsetY;
  private int selectedIndex;
  private int sliderHeight;
  private int sliderY;

  public Form(final String title) {
    formTitle = title;
    doLayout();
    formWidth = super.getWidth();
    formHeight = super.getHeight();
  }

  public Form(final String title, final Item[] items) {
    formTitle = title;
    doLayout();
    for (final Item item : items) {
      item.yPos = yPos;
      item.doLayout();
      this.items.add(item);
      yPos += item.itemHeight;
    }
    formWidth = super.getWidth();
    formHeight = super.getHeight();
  }

  public int append(final Image img) {
    final ImageItem item = new ImageItem(null, img, Item.LAYOUT_DEFAULT, null);
    append(item);

    return getIndex(item);
  }

  public int append(final Item item) {
    if (item != null) {
      System.out.println("ypos is " + yPos);
      item.yPos = yPos;
      items.add(item);
      item.doLayout();
      yPos += item.itemHeight;
      System.out.println("after ypos is " + yPos + " ht " + item.itemHeight);

      return getIndex(item);
    }
    else {
      throw new NullPointerException();
    }
  }

  public int append(final String str) {
    final StringItem item = new StringItem(null, str);
    append(item);

    return getIndex(item);
  }

  public void delete(final int itemNum) {
    items.remove(itemNum);
  }

  public void deleteAll() {
    items.removeAllElements();
  }

  public Item get(final int itemNum) {
    return items.elementAt(itemNum);
  }

  @Override
  public int getHeight() {
    return super.getHeight();
  }

  @Override
  public int getWidth() {
    return super.getWidth();
  }

  public void insert(final int itemNum, final Item item) {
    items.insertElementAt(item, itemNum);
  }

  public void set(final int itemNum, final Item item) {
    items.setElementAt(item, itemNum);
  }

  public void setItemStateListener(final ItemStateListener listener) {
    iStateListener = listener;
  }

  public int size() {
    return items.size();
  }

  private int getIndex(final Item item) {
    final int size = items.size();
    for (int i = 0; i < size; i++) {
      if (items.elementAt(i) == item) { return i; }
    }

    return 0;
  }

  @Override
  protected void paint(final Graphics g) {
    // /System.out.println("in form paint");
    synchronized (Screen.paintsync) {
      int tickerYPos = 0;
      final int tickerHeight = (Displayable.fontHeight + 5);
      g.setColor(0xFFFFFFFF);
      g.fillRect(0, 0, getWidth(), getHeight());
      yPos = Font.getDefaultFont().getHeight() + 2; //
      if (getTicker() != null) {
        tickerYPos = Displayable.SCREEN_HEIGHT - tickerHeight;
        // yPos=yPos+(fontHeight+5);//
      }
      final int starty = yPos;
      int y = yPos + (scrollOffsetY);
      final int len = items.size();
      for (int i = 0; i < len; i++) {
        final Item curItem = items.elementAt(i);
        // curItem.isSelected=true;
        curItem.keyEvent = keyTypedEvent;
        curItem.yPos = y;
        curItem.paint(g);
        y += curItem.itemHeight;
      }
      formHeight = y - yPos;
      if (formHeight == 0) { return; }
      Displayable.isKeyPressed = false;
      // calculating scrollbar
      displayableHeight = Displayable.SCREEN_HEIGHT - starty - ((getTicker() != null) ? tickerHeight
          : 0);
      sliderHeight = Math.min(displayableHeight, (displayableHeight * displayableHeight) / formHeight);
      sliderY = (displayableHeight * (-scrollOffsetY + yPos)) / formHeight;
      // System.out.println("sliderY is "+sliderY+" sliderHeight "+sliderHeight+" scrollOffsetY "+scrollOffsetY);
      if (formHeight > displayableHeight) {
        drawVerticalScrollbar(g, Displayable.SCREEN_WIDTH - 4, sliderY, 4, sliderHeight);
      }
      drawTitle(g, "FORM", 0);
      if (getTicker() != null) {
        drawTicker(g, tickerYPos);
      }
      if (showCommandMenu) {
        showCommandMenu(g);
      }
    }
  }

  public void doLayout() {
    yPos = Font.getDefaultFont().getHeight() + 4;
  }

  @Override
  protected void keyPressed(final int keyCode) {
    if (showCommandMenu) { return; }
    boolean isKeyHandled = false;
    if (curSelectedItemIndex != -1) {
      updateItem(keyCode);
      isKeyHandled = items.get(curSelectedItemIndex).handledKey;
      if (!isKeyHandled) {
        updateForm(keyCode);
      }
    }
    else {
      updateForm(keyCode);
    }
  }

  @Override
  protected void keyRepeated(final int keyCode) {
  }

  @Override
  protected void keyReleased(final int keyCode) {
  }

  @Override
  protected void pointerPressed(final int x, final int y) {
    pointerX = x;
    pointerY = y;
    for (final Item item1 : items) {
      item1.isSelected = false;
    }
    if (items.size() == 0) { return; }
    curSelectedItemIndex = findItem(pointerX, pointerY);
    final Item item = items.get(curSelectedItemIndex);
    if (item instanceof ChoiceGroup) {
      final ChoiceGroup cg = (ChoiceGroup)item;
      for (int i = 0; i < cg.elements.size(); i++) {
        final ChoiceGroupElement elem = cg.elements.get(i);
        if (((pointerY > elem.getYPos()) && (pointerY < (elem.getYPos() + elem.getHeight())))
            && ((pointerX > elem.getXPos()) && (pointerX < (elem.getXPos() + elem.getWidth())))) {
          cg.highlightedIndex = i;
          elem.isSelected = true;
        }
      }
    }
    if (curSelectedItemIndex == previousSelectedIndex) {
      keyPressed(Displayable.KEY_NUM5);
    }
    else {
      item.isSelected = true;
      previousSelectedIndex = curSelectedItemIndex;
    }
  }

  @Override
  protected void pointerDragged(final int x, final int y) {
    pointerDraggedX = x;
    pointerDraggedY = y;
    if (pointerDraggedY < pointerY) // upwards
    {
      scrollOffsetY -= (pointerY - pointerDraggedY);
      if ((-scrollOffsetY + displayableHeight) >= (formHeight)) {
        scrollOffsetY = -(formHeight - displayableHeight) - 20;
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

  private int findItem(final int x, final int y) {
    for (int i = 0; i < items.size(); i++) {
      if (((y > items.get(i).yPos) && (y < (items.get(i).yPos + items.get(i).itemHeight)))) { return i; }
    }

    return 0;
  }

  private void updateForm(final int keyCode) {
    final int itemsLen = items.size();
    if (itemsLen == 0) { return; }
    switch (keyCode) {
      case KEY_UP_ARROW:
      case KEY_NUM2:
        items.get(curSelectedItemIndex).isSelected = false; // clearing previous selected
        if (curSelectedItemIndex > 0) {
          curSelectedItemIndex--;
        }
        while (items.get(curSelectedItemIndex) instanceof Spacer) // skiping spacer
        {
          if (curSelectedItemIndex > 0) {
            curSelectedItemIndex--;
          }
          // items.get(curSelectedItemIndex).handleKey(keyCode);
        }
        // scrolling according to item
        final Item curitem = items.get(curSelectedItemIndex);
        if (scrollOffsetY < 0) {
          if ((-scrollOffsetY) > (curitem.yPos)) // items is up side totally need to move down by item height
          {
            scrollOffsetY += curitem.itemHeight;
          }
          else if (((-scrollOffsetY) > curitem.yPos) && ((-scrollOffsetY) < (curitem.yPos + curitem.itemHeight))) // item is visible
          {
            // semi visible
            scrollOffsetY += (displayableHeight); // moving down with displayHeight (Screen Height)
            if (scrollOffsetY > 0) { // beyond formheight
              scrollOffsetY = 0;
            }
          }
          else if ((-scrollOffsetY + yPos) >= curitem.yPos) {
            scrollOffsetY = 0;
          }
        }

        break;
      case KEY_DOWN_ARROW:
      case KEY_NUM8:
        items.get(curSelectedItemIndex).isSelected = false;
        System.out.println("in down");
        if (curSelectedItemIndex < (itemsLen - 1)) {
          curSelectedItemIndex++;
        }
        while (items.get(curSelectedItemIndex) instanceof Spacer) // skiping spacer
        {
          if (curSelectedItemIndex < (itemsLen - 1)) {
            curSelectedItemIndex++;
          }
          // items.get(curSelectedItemIndex).handleKey(keyCode);
        }
        final Item item = items.get(curSelectedItemIndex);
        if (-scrollOffsetY < (formHeight - displayableHeight)) {
          if ((-scrollOffsetY + displayableHeight) < (item.yPos - yPos)) // items is down side need to move up by item height
          {
            scrollOffsetY -= item.itemHeight;
          }
          else if (((-scrollOffsetY + displayableHeight) > (item.yPos - yPos)) && ((-scrollOffsetY + displayableHeight) < ((item.yPos + item.itemHeight) - yPos))) {
            // item is visible
            // semi visible
            scrollOffsetY -= (displayableHeight); // moving up with displayHeight (Screen)
            if ((-scrollOffsetY + displayableHeight) > formHeight) { // beyond formheight
              scrollOffsetY = -(formHeight - displayableHeight);
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
    items.get(curSelectedItemIndex).isSelected = true;
  }

  private void updateItem(final int keyCode) {
    items.get(curSelectedItemIndex).handleKey(keyCode);
  }

  public void deIntialize() {
    items = null;
    iStateListener = null;
    formTitle = null;
    ticker = null;
    super.deInit();
  }

}
