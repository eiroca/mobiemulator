package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextBox extends Screen {

  private int displayableHeight;
  private int pointerDraggedX;
  private int pointerDraggedY;
  private int pointerX;
  private int pointerY;
  private int scrollOffsetY;
  private int sliderHeight;
  private int sliderY;
  private final TextField tf;
  private Ticker ticker;
  private int tickerYPos;

  public TextBox(final String title, final String text, final int maxSize, final int constraints) {
    tf = new TextField(title, text, maxSize, constraints);
    tf.setFullScreen(true);
  }

  public void setString(final String text) {
    tf.setString(text);
  }

  public int getChars(final char[] data) {
    return tf.getChars(data);
  }

  public void insert(final String src, final int position) {
    tf.insert(src, position);
  }

  public void insert(final char[] data, final int offset, final int length, final int position) {
    tf.insert(data, offset, length, position);
  }

  public int getMaxSize() {
    return tf.getMaxSize();
  }

  public int size() {
    return tf.size();
  }

  public void setConstraints(final int constraints) {
    tf.setConstraints(constraints);
  }

  public int getConstraints() {
    return tf.getConstraints();
  }

  public void setInitialInputMode(final String characterSubset) {
    tf.setInitialInputMode(characterSubset);
  }

  @Override
  public void setTitle(final String s) {
    tf.setLabel(s);
  }

  public void delete(final int offset, final int length) {
    tf.delete(offset, length);
  }

  public int getCaretPosition() {
    return tf.getCaretPosition();
  }

  public String getString() {
    return tf.getString();
  }

  public void setChars(final char[] data, final int offset, final int length) {
    tf.setChars(data, offset, length);
  }

  public int setMaxSize(final int maxSize) {
    return tf.setMaxSize(maxSize);
  }

  @Override
  public void setTicker(final Ticker ticker) {
    this.ticker = ticker;
  }

  @Override
  protected void paint(final Graphics g) {
    tf.yPos = 0;
    g.setColor(0xFFFFFFFF);
    g.fillRect(0, 0, getWidth(), getHeight());
    final int y = tf.yPos + Displayable.fontHeight + scrollOffsetY;
    final int tickerHeight = (Displayable.fontHeight + 5);
    if (getTicker() != null) {
      tickerYPos = Displayable.SCREEN_HEIGHT - tickerHeight;
    }
    // tf.setPreferredSize(getWidth(), getHeight());
    tf.yPos = y;
    tf.isSelected = true;
    tf.isTickerPresent = (getTicker() != null);
    tf.tickerHeight = tickerHeight;
    System.out.println("y pos is " + y);
    tf.paint(g);
    displayableHeight = (Displayable.SCREEN_HEIGHT - y - ((getTicker() != null) ? tickerHeight
        : 0));
    sliderHeight = Math.min(displayableHeight, (displayableHeight * displayableHeight) / tf.itemHeight);
    sliderY = (displayableHeight * (-scrollOffsetY + yPos)) / tf.itemHeight;
    if (tf.itemHeight > displayableHeight) {
      drawVerticalScrollbar(g, Displayable.SCREEN_WIDTH - 4, sliderY, 4, sliderHeight);
    }
    drawTitle(g, "TEXTBOX", 0);
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
    tf.handleKey(keyCode);
  }

  @Override
  protected void keyRepeated(final int keyCode) {
    if (showCommandMenu) { return; }
    tf.handleKey(keyCode);
  }

  @Override
  protected void keyReleased(final int keyCode) {
  }

  @Override
  protected void pointerPressed(final int x, final int y) {
    pointerX = x;
    pointerY = y;
    for (int i = 0; i < tf.lines.size(); i++) {
      final int lineY = i * (Displayable.fontHeight);
      final int lineHeight = Displayable.fontHeight;
      if ((pointerY > lineY) && (pointerY < (lineY + lineHeight))) {
        tf.cursorPosY = lineY + lineHeight;
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
      if ((-scrollOffsetY + displayableHeight) >= (tf.itemHeight)) {
        scrollOffsetY = -(tf.itemHeight - displayableHeight) - 20;
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

}
