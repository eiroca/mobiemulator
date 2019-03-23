package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextEditor extends CanvasItem {

  boolean isFocused = false;
  int caretPosition;
  private int constraints;
  int contentHeight;
  private String contentText;
  private Font font;
  private int iBackgroundColor;
  private int iForegroundColor;
  private int iHighlightColor;
  boolean isVisible;
  int lineMarginHeight;
  private int maxSize;
  int noOfLines;
  private String selectedText;
  private int visibleContentPos;

  public TextEditor(final java.lang.String text, final int maxSize, final int constraints, final int width, final int height) {
  }

  public void setFocus(final boolean focused) {
    isFocused = focused;
  }

  public boolean getFocus() {
    return isFocused;
  }

  public int getLineMarginHeight() {
    return lineMarginHeight;
  }

  public int getContentHeight() {
    return font.getHeight() * noOfLines;
  }

  public void setCaretPosition(final int index) {
    caretPosition = index;
  }

  public int getCaretPosition() {
    return caretPosition;
  }

  public int getVisibleContentPosition() {
    return visibleContentPos;
  }

  public Font getFont() {
    return font;
  }

  public void setFont(final Font font) {
    this.font = font;
  }

  public void setBackgroundColor(final int alpha, final int red, final int green, final int blue) {
  }

  public void setForegroundColor(final int alpha, final int red, final int green, final int blue) {
  }

  public void setHighlightColor(final int alpha, final int red, final int green, final int blue) {
  }

  public int getBackgroundColor() {
    return iBackgroundColor;
  }

  public int getForegroundColor() {
    return iForegroundColor;
  }

  public int getHighlightColor() {
    return iHighlightColor;
  }

  public void setString(final java.lang.String text) {
    contentText = text;
  }

  public java.lang.String getString() {
    return contentText;
  }

  public void insert(final java.lang.String text, final int position) {
  }

  public void delete(final int offset, final int length) {
  }

  public int getMaxSize() {
    return maxSize;
  }

  public int setMaxSize(final int maxSize) {
    this.maxSize = maxSize;

    return maxSize;
  }

  public int size() {
    return contentText.length();
  }

  public void setConstraints(final int constraints) {
    this.constraints = constraints;
  }

  public int getConstraints() {
    return constraints;
  }

  public void setInitialInputMode(final java.lang.String characterSubset) {
  }

  public void setSelection(final int index, final int length) {
    selectedText = contentText.substring(index, index + length);
  }

  public java.lang.String getSelection() {
    return selectedText;
  }

  public void setTextEditorListener(final TextEditorChangeListener listener) {
  }

  @Override
  public void setVisible(final boolean visible) {
    isVisible = visible;
  }

  public boolean getVisible() {
    return isVisible;
  }

}
