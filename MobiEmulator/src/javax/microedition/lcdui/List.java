/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class List extends Screen implements Choice {
    public static final Command SELECT_COMMAND       = new Command("", Command.SCREEN, 0);
    int                         curSelectedItemIndex = 0;
    int                         displayableHeight    = 0;
    ChoiceGroup                 cg;
    private int                 pointerDraggedX;
    private int                 pointerDraggedY;
    private int                 pointerX;
    private int                 pointerY;
    private int                 scrollOffsetY;
    private int                 sliderHeight;
    private int                 sliderY;
    int                         tickerYPos;
    public List(String title) {
        cg = new ChoiceGroup(title, Choice.EXCLUSIVE);
    }

    public List(String title, int listType, String[] stringElements, Image[] imageElements) {
        cg = new ChoiceGroup(title, listType, stringElements, imageElements);
        cg.setSelectedIndex(curSelectedItemIndex, true);
    }

    public int append(String stringPart, Image imagePart) {
        return cg.append(stringPart, imagePart);
    }
    public void delete(int elementNum) {
        cg.delete(elementNum);
    }
    public void deleteAll() {
        cg.deleteAll();
    }
    public int getFitPolicy() {
        return cg.getFitPolicy();
    }
    public Font getFont(int elementNum) {
        return cg.getFont(elementNum);
    }
    public Image getImage(int elementNum) {
        return cg.getImage(elementNum);
    }
    public int getSelectedFlags(boolean[] selectedArray_return) {
        return cg.getSelectedFlags(selectedArray_return);
    }
    public int getSelectedIndex() {
        return cg.getSelectedIndex();
    }
    public String getString(int elementNum) {
        return cg.getString(elementNum);
    }
    public Ticker getTicker() {
        return ticker;
    }
    public void insert(int elementNum, String stringPart, Image imagePart) {
        cg.insert(elementNum, stringPart, imagePart);
    }
    public boolean isSelected(int elementNum) {
        return cg.isSelected(elementNum);
    }
    public void removeCommand(Command cmd) {
        cg.removeCommand(cmd);
    }
    public void set(int elementNum, String stringPart, Image imagePart) {
        cg.set(elementNum, stringPart, imagePart);
    }
    public void setFitPolicy(int fitPolicy) {
        cg.setFitPolicy(fitPolicy);
    }
    public void setFont(int elementNum, Font font) {
        cg.setFont(elementNum, font);
    }
    public void setSelectCommand(Command command) {
        cg.setDefaultCommand(command);
    }
    public void setSelectedFlags(boolean[] selectedArray) {
        cg.setSelectedFlags(selectedArray);
    }
    public void setSelectedIndex(int elementNum, boolean selected) {
        cg.setSelectedIndex(elementNum, selected);
    }
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
    public void setTitle(String s) {
        cg.setLabel(s);
    }
    public int size() {
        return cg.size();
    }
    @Override
    protected void paint(Graphics g) {
        g.setColor(0xFFFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        yPos = Font.getDefaultFont().getHeight() + 4;    //
        int tickerHeight = (fontHeight + 5);
        if (getTicker() != null) {
            tickerYPos = SCREEN_HEIGHT - tickerHeight;
            // yPos=yPos+(fontHeight+5);//
            // y=y+drawTicker(g, yPos);
        }
        cg.setType(true);
        int y = yPos;
        cg.yPos = yPos + (scrollOffsetY);
        cg.paint(g);
        displayableHeight = (SCREEN_HEIGHT - y - ((getTicker() != null) ? tickerHeight
                : 0));
        sliderHeight      = Math.min(displayableHeight, displayableHeight * displayableHeight / cg.itemHeight);
        sliderY           = displayableHeight * (-scrollOffsetY + yPos) / cg.itemHeight;
        cg.itemHeight     += 4;
        if (cg.itemHeight > displayableHeight) {
            drawVerticalScrollbar(g, SCREEN_WIDTH - 4, sliderY, 4, sliderHeight);
        }
        drawTitle(g, "LIST", 0);
        if (getTicker() != null) {
            drawTicker(g, tickerYPos);
        }
        if (showCommandMenu) {
            showCommandMenu(g);
        }
    }
    protected void keyPressed(int keyCode) {
        if (showCommandMenu) {
            return;
        }
        boolean isKeyHandled = false;
        cg.handleKey(keyCode);
        isKeyHandled = cg.handledKey;
        updateList(keyCode);
    }
    protected void keyRepeated(int keyCode) {
        if (showCommandMenu) {
            return;
        }
        boolean isKeyHandled = false;
        cg.handleKey(keyCode);
        isKeyHandled = cg.handledKey;
        updateList(keyCode);
    }
    protected void keyReleased(int keyCode) {}
    protected void pointerPressed(int x, int y) {
        pointerX = x;
        pointerY = y;
        for (int i = 0;i < cg.elements.size();i++) {
            ChoiceGroupElement elem = cg.elements.get(i);
            if (((pointerY > elem.getYPos()) && (pointerY < (elem.getYPos() + elem.getHeight())))
                    && ((pointerX > elem.getXPos()) && (pointerX < (elem.getXPos() + elem.getWidth())))) {
                cg.highlightedIndex = i;
                elem.isSelected     = true;
            }
        }
    }
    protected void pointerDragged(int x, int y) {
        pointerDraggedX = x;
        pointerDraggedY = y;
        if (pointerDraggedY < pointerY)           // upwards
        {
            scrollOffsetY -= (pointerY - pointerDraggedY);
            if (-scrollOffsetY + displayableHeight >= (cg.itemHeight)) {
                scrollOffsetY = -(cg.itemHeight - displayableHeight) - 20;
            }
        } else if (pointerDraggedY > pointerY)    // downwards
        {
            scrollOffsetY += (pointerDraggedY - pointerY);
            if (scrollOffsetY > 0) {
                scrollOffsetY = 0;
            }
        }
    }
    protected void pointerReleased(int x, int y) {}
    public void updateList(int keyCode) {
        int itemsLen = cg.elements.size();
        switch (keyCode) {
        case KEY_UP_ARROW :
        case KEY_NUM2 :
            cg.elements.get(curSelectedItemIndex).isSelected = false;    // clearing previous selected
            if (curSelectedItemIndex > 0) {
                curSelectedItemIndex--;
            }
            // scrolling according to item
            ChoiceGroupElement curitem = cg.elements.get(curSelectedItemIndex);
            if (scrollOffsetY < 0) {
                if ((-scrollOffsetY) > (curitem.getYPos()))              // cg.elements is up side totally need to move down by item height
                {
                    scrollOffsetY += curitem.getHeight();
                } else if ((-scrollOffsetY) > curitem.getYPos()
                           && ((-scrollOffsetY) < (curitem.getYPos() + curitem.getHeight())))    // item is visible
                {
                    // semi visible
                    scrollOffsetY += (displayableHeight);    // moving down with displayHeight (Screen Height)
                    if (scrollOffsetY > 0) {                                         // beyond formheight
                        scrollOffsetY = 0;
                    }
                } else if ((-scrollOffsetY + yPos) >= curitem.getYPos()) {
                    scrollOffsetY = 0;
                }
            }

            break;
        case KEY_DOWN_ARROW :
        case KEY_NUM8 :
            cg.elements.get(curSelectedItemIndex).isSelected = false;
            if (curSelectedItemIndex < itemsLen - 1) {
                curSelectedItemIndex++;
            }
            ChoiceGroupElement item = cg.elements.get(curSelectedItemIndex);
            if (-scrollOffsetY < (cg.itemHeight - displayableHeight)) {
                if (-scrollOffsetY + displayableHeight < (item.getYPos() - yPos))    // cg.elements is down side need to move up by item height
                {
                    scrollOffsetY -= item.getHeight();
                } else if ((-scrollOffsetY + displayableHeight) > (item.getYPos() - yPos)
                           && (-scrollOffsetY + displayableHeight) < (item.getYPos() + item.getHeight() - yPos))    // item is visible
                {
                    // semi visible
                    scrollOffsetY -= (displayableHeight);    // moving up with displayHeight (Screen)
                    if (-scrollOffsetY + displayableHeight > cg.itemHeight) {    // beyond formheight
                        scrollOffsetY = -(cg.itemHeight - displayableHeight);
                    }
                }
            }

            break;
        case KEY_LEFT_ARROW :
        case KEY_NUM4 :
            break;
        case KEY_RIGHT_ARROW :
        case KEY_NUM6 :
            break;
        }
        cg.elements.get(curSelectedItemIndex).isSelected = true;
    }
    public void deIntialize() {
        cg.elements = null;
        cg          = null;
        ticker      = null;
        super.deInit();
    }
}
