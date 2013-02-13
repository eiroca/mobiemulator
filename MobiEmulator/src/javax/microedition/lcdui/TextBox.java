/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextBox extends Screen {
    private int       displayableHeight;
    private int       pointerDraggedX;
    private int       pointerDraggedY;
    private int       pointerX;
    private int       pointerY;
    private int       scrollOffsetY;
    private int       sliderHeight;
    private int       sliderY;
    private TextField tf;
    private Ticker    ticker;
    private int       tickerYPos;
    public TextBox(String title, String text, int maxSize, int constraints) {
        tf = new TextField(title, text, maxSize, constraints);
        tf.setFullScreen(true);
    }

    public void setString(String text) {
        tf.setString(text);
    }
    public int getChars(char[] data) {
        return tf.getChars(data);
    }
    public void insert(String src, int position) {
        tf.insert(src, position);
    }
    public void insert(char[] data, int offset, int length, int position) {
        tf.insert(data, offset, length, position);
    }
    public int getMaxSize() {
        return tf.getMaxSize();
    }
    public int size() {
        return tf.size();
    }
    public void setConstraints(int constraints) {
        tf.setConstraints(constraints);
    }
    public int getConstraints() {
        return tf.getConstraints();
    }
    public void setInitialInputMode(String characterSubset) {
        tf.setInitialInputMode(characterSubset);
    }
    public void setTitle(String s) {
        tf.setLabel(s);
    }
    public void delete(int offset, int length) {
        tf.delete(offset, length);
    }
    public int getCaretPosition() {
        return tf.getCaretPosition();
    }
    public String getString() {
        return tf.getString();
    }
    public void setChars(char[] data, int offset, int length) {
        tf.setChars(data, offset, length);
    }
    public int setMaxSize(int maxSize) {
        return tf.setMaxSize(maxSize);
    }
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
    @Override
    protected void paint(Graphics g) {
        tf.yPos = 0;
        g.setColor(0xFFFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        int y            = tf.yPos + fontHeight + scrollOffsetY;
        int tickerHeight = (fontHeight + 5);
        if (getTicker() != null) {
            tickerYPos = SCREEN_HEIGHT - tickerHeight;
        }
        // tf.setPreferredSize(getWidth(), getHeight());
        tf.yPos            = y;
        tf.isSelected      = true;
        tf.isTickerPresent = (getTicker() != null);
        tf.tickerHeight    = tickerHeight;
        System.out.println("y pos is " + y);
        tf.paint(g);
        displayableHeight = (SCREEN_HEIGHT - y - ((getTicker() != null) ? tickerHeight
                : 0));
        sliderHeight      = Math.min(displayableHeight, displayableHeight * displayableHeight / tf.itemHeight);
        sliderY           = displayableHeight * (-scrollOffsetY + yPos) / tf.itemHeight;
        if (tf.itemHeight > displayableHeight) {
            drawVerticalScrollbar(g, SCREEN_WIDTH - 4, sliderY, 4, sliderHeight);
        }
        drawTitle(g, "TEXTBOX", 0);
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
        tf.handleKey(keyCode);
        isKeyHandled = tf.handledKey;
    }
    protected void keyRepeated(int keyCode) {
        if (showCommandMenu) {
            return;
        }
        boolean isKeyHandled = false;
        tf.handleKey(keyCode);
        isKeyHandled = tf.handledKey;
    }
    protected void keyReleased(int keyCode) {}
    protected void pointerPressed(int x, int y) {
        pointerX = x;
        pointerY = y;
        for (int i = 0;i < tf.lines.size();i++) {
            int lineY      = i * (fontHeight);
            int lineHeight = fontHeight;
            if ((pointerY > lineY) && (pointerY < (lineY + lineHeight))) {
                tf.cursorPosY = lineY + lineHeight;
            }
        }
    }
    protected void pointerDragged(int x, int y) {
        pointerDraggedX = x;
        pointerDraggedY = y;
        if (pointerDraggedY < pointerY)           // upwards
        {
            scrollOffsetY -= (pointerY - pointerDraggedY);
            if (-scrollOffsetY + displayableHeight >= (tf.itemHeight)) {
                scrollOffsetY = -(tf.itemHeight - displayableHeight) - 20;
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
}
