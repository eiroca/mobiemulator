/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------


/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextEditor extends CanvasItem {
    boolean        isFocused = false;
    int            caretPosition;
    private int    constraints;
    int            contentHeight;
    private String contentText;
    private Font   font;
    private int    iBackgroundColor;
    private int    iForegroundColor;
    private int    iHighlightColor;
    boolean        isVisible;
    int            lineMarginHeight;
    private int    maxSize;
    int            noOfLines;
    private String selectedText;
    private int    visibleContentPos;
    public TextEditor(java.lang.String text, int maxSize, int constraints, int width, int height) {}

    public void setFocus(boolean focused) {
        this.isFocused = focused;
    }
    public boolean getFocus() {
        return isFocused;
    }
    public int getLineMarginHeight() {
        return lineMarginHeight;
    }
    public int getContentHeight() {
        return this.font.getHeight() * noOfLines;
    }
    public void setCaretPosition(int index) {
        this.caretPosition = index;
    }
    public int getCaretPosition() {
        return caretPosition;
    }
    public int getVisibleContentPosition() {
        return visibleContentPos;
    }
    public Font getFont() {
        return this.font;
    }
    public void setFont(Font font) {
        this.font = font;
    }
    public void setBackgroundColor(int alpha, int red, int green, int blue) {}
    public void setForegroundColor(int alpha, int red, int green, int blue) {}
    public void setHighlightColor(int alpha, int red, int green, int blue) {}
    public int getBackgroundColor() {
        return iBackgroundColor;
    }
    public int getForegroundColor() {
        return iForegroundColor;
    }
    public int getHighlightColor() {
        return iHighlightColor;
    }
    public void setString(java.lang.String text) {
        this.contentText = text;
    }
    public java.lang.String getString() {
        return contentText;
    }
    public void insert(java.lang.String text, int position) {}
    public void delete(int offset, int length) {}
    public int getMaxSize() {
        return maxSize;
    }
    public int setMaxSize(int maxSize) {
        this.maxSize = maxSize;

        return maxSize;
    }
    public int size() {
        return this.contentText.length();
    }
    public void setConstraints(int constraints) {
        this.constraints = constraints;
    }
    public int getConstraints() {
        return constraints;
    }
    public void setInitialInputMode(java.lang.String characterSubset) {}
    public void setSelection(int index, int length) {
        selectedText = this.contentText.substring(index, index + length);
    }
    public java.lang.String getSelection() {
        return selectedText;
    }
    public void setTextEditorListener(TextEditorChangeListener listener) {}
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    public boolean getVisible() {
        return isVisible;
    }
}
