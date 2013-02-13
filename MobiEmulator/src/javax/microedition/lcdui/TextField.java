/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class TextField extends Item {
    public static final int ANY                   = 0;
    public static final int CONSTRAINT_MASK       = 65535;
    public static final int DECIMAL               = 5;
    public static final int EMAILADDR             = 1;
    public static final int INITIAL_CAPS_SENTENCE = 2097152;
    public static final int INITIAL_CAPS_WORD     = 1048576;
    public static final int NON_PREDICTIVE        = 524288;
    public static final int NUMERIC               = 2;
    public static final int PASSWORD              = 65536;
    public static final int PHONENUMBER           = 3;
    public static final int SENSITIVE             = 262144;
    public static final int UNEDITABLE            = 131072;
    public static final int URL                   = 4;
    public static boolean   isKeyPad              = false;
    public static boolean   isFocused             = false;
    private int             blink                 = 0;
    private int             curLine               = 0;
    private int             cursorPos             = 0;
    public int              cursorPosX            = 0;
    public int              cursorPosY            = 0;
    private String[]        keyChars              = {
        "0 ", "1.@/", "2abc", "3def", "4ghi", "5jkl", "6mno", "7pqrs", "8tuv", "9wxyz"
    };
    private int             lineSpacing           = 2;
    private int             padding               = 2;
    int                     tickerHeight          = 0;
    private int             totalLines            = 0;
    public boolean          isTickerPresent       = false;
    private boolean         isMask                = false;
    private boolean         isFullScreen          = false;
    private boolean         isCursorVisible       = false;
    private int             constraints;
    private int             count;
    private String          label;
    public ArrayList        lines;
    private int             maxSize;
    private char            previousKey;
    private long            previoustime;
    private StringBuffer    textFieldText;
    public TextField(String label, String text, int maxSize, int constraints) {
        this.label = label;
        if (text == null) {
            text = "";
        }
        // this.isSelected=true;
        this.textFieldText = new StringBuffer(text);
        this.cursorPos     = textFieldText.length();
        lines              = StringUtils.wrapToLines(text, font, getPreferredWidth());
        totalLines         = lines.size();
        curLine            = totalLines - 1;
        cursorPosX         = getCursorX(cursorPos);
        cursorPosY         = (curLine) * (fontHeight + lineSpacing);
        this.maxSize       = maxSize;
        this.constraints   = constraints;
    }

    public void delete(int offset, int length) {
        if ((offset < 0) || (length <= 0)) {
            throw new IllegalArgumentException();
        }
        textFieldText.delete(offset, offset + length);
    }
    public int getCaretPosition() {
        return cursorPos;
    }
    public void setCaretPosition(int pos) {
        cursorPos = pos;
    }
    public int getChars(char[] data) {
        int noofCharsCopied = Math.max(data.length, textFieldText.length());
        if (noofCharsCopied > textFieldText.length())    // array is more size
        {
            textFieldText.getChars(0, textFieldText.length(), data, 0);

            return textFieldText.length();
        } else {
            textFieldText.getChars(0, data.length, data, 0);

            return data.length;
        }
    }
    public int getConstraints() {
        return this.constraints;
    }
    public int getMaxSize() {
        return maxSize;
    }
    public void setFullScreen(boolean value) {
        this.isFullScreen = value;
    }
    public String getString() {
        return textFieldText.toString();
    }
    public void insert(char[] data, int offset, int length, int position) {
        if (data == null) {
            throw new NullPointerException();
        }
        if ((offset < 0) || (length <= 0)) {
            throw new IllegalArgumentException();
        }
        if (data.length < (offset + length)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (textFieldText.length() + length > maxSize) {
            throw new IllegalArgumentException("Beyond the MaxSize of TextField");
        }
        textFieldText.insert(position, data, offset, length);
    }
    public void insert(String src, int position) {
        if (textFieldText.length() + src.length() > maxSize) {
            System.out.println("Beyond the MaxSize of TextField");

            return;
        }
        textFieldText.insert(position, src);
        cursorPos += src.length();
    }
    public void setChars(char[] data, int offset, int length) {
        if (data == null) {
            throw new NullPointerException();
        }
        if ((offset < 0) || (length <= 0)) {
            throw new IllegalArgumentException();
        }
        if (data.length < (offset + length)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        textFieldText = null;
        textFieldText = new StringBuffer();
        textFieldText.append(data, offset, length);
    }
    public void setConstraints(int constraints) {
        this.constraints = constraints;
    }
    public void setInitialInputMode(String characterSubset) {}
    public void setLabel(String label) {
        this.label = label;
    }
    public int setMaxSize(int value) {
        this.maxSize = value;

        return value;
    }
    public void setString(String text) {
        this.textFieldText = new StringBuffer(text);
    }
    public int size() {
        return textFieldText.length();
    }
    @Override
    public void paint(Graphics g) {
        // draw Textfield
        int y = yPos;
        synchronized (Screen.globalSync) {
            isFocused = false;
            if (isSelected) {
                isFocused = isSelected;
                updateTextField();
            }
            lines      = StringUtils.wrapToLines(textFieldText.toString(), font, getPreferredWidth() - padding - 4);
            totalLines = lines.size();
            if (lines == null) {
                lines = StringUtils.wrapToLines(textFieldText.toString(), font, getPreferredWidth() - padding - 4);
            }
            if (isSelected) {
                isFocused = isSelected;
                g.setColor(0);    // 0XFF33CCCC
                int selectedRectY = y;
                int h             = ((label != null) ? (selectedRectY + fontHeight + 4)
                        : selectedRectY) + (lines.size()) * (font.getHeight() + lineSpacing) + 4;
                g.drawRect(1, y + 2, getPreferredWidth(), h - selectedRectY);
                isCursorVisible = true;
            }
            if (label != null) {
                g.setColor(0);
                g.drawString(label, 2, y, Graphics.LEFT | Graphics.TOP);
                y += (fontHeight + 4);
            }
            cursorPosX = padding + 2 + getCursorX(cursorPos);
            cursorPosY = getCursorY(cursorPos);    // (((totalLines!=1)?totalLines-getCurLine():getCurLine())*(fontHeight+lineSpacing));
            g.setColor(0);
            if ((blink % 5 == 0) && isCursorVisible) {
                g.drawLine(cursorPosX, y + cursorPosY, cursorPosX, y + cursorPosY + font.getHeight());
            }
            blink++;
            if (blink > 10) {
                blink = 0;
            }
            if (isKeyPad) {
                if (System.currentTimeMillis() - previoustime > 2000) {
                    count        = 0;
                    previoustime = System.currentTimeMillis();
                }
            }
            // draw box
            int textBoxHeight = fontHeight + lineSpacing;
            if (isFullScreen) {
                int hght = getPreferredHeight() - (fontHeight + 5) - (isTickerPresent ? tickerHeight
                        : 0) - ((label != null) ? (fontHeight + 5)
                                                : 0);
                if ((lines.size() * (fontHeight + lineSpacing)) > hght) {
                    textBoxHeight = lines.size() * (fontHeight + lineSpacing);
                } else {
                    textBoxHeight = hght;
                }
            } else {
                textBoxHeight = lines.size() * (fontHeight + lineSpacing);
            }
            g.setColor(0XFFFFFF);
            g.drawRoundRect(padding, y, getPreferredWidth() - (padding << 1) - 2, textBoxHeight, 8, 8);
            g.setColor(0);
            g.drawRoundRect(padding, y, getPreferredWidth() - (padding << 1) - 2, textBoxHeight, 8, 8);
            y += 2;
            if (textFieldText != null) {
                g.setFont(font);
                g.setColor(0);
                for (int i = 0;i < lines.size();i++) {
                    String str = (String) lines.get(i);
                    if (!isMask) {
                        g.drawString(str, padding + 2, y, Graphics.TOP | Graphics.LEFT);
                    } else {
                        g.drawString(maskedStr(str), padding + 2, y, Graphics.TOP | Graphics.LEFT);
                    }
                    y += ((i != lines.size()) ? (fontHeight + lineSpacing)
                                              : 0);
                }
            }
            if (isSelected) {
                y += 4;
            }
            itemHeight = y - yPos;
        }
    }
    private String maskedStr(String text) {
        int    len    = text.length();
        StringBuffer sb=new StringBuffer();
        for (int i = 0;i < len;i++) {
            sb.append("*");
        }

        return sb.toString();
    }
    public int getCursorX(int cursorPos) {
        for (int i = 0;i < lines.size();i++) {
            String str    = ((String) (lines.get(i)));
            int    strLen = str.length();
            if (strLen >= cursorPos) {
                if (!isMask) {
                    return (font.stringWidth(str.substring(0, cursorPos)));
                } else {
                    return (font.stringWidth(maskedStr(str.substring(0, cursorPos))));
                }
            }
            cursorPos -= strLen;
        }

        return 0;
    }
    public int getCursorY(int cursorPos) {
        int charcount = 0;
        for (int i = 0;i < lines.size();i++) {
            String str    = ((String) (lines.get(i)));
            int    strLen = str.length();
            if (charcount + strLen >= cursorPos) {
                return i * (fontHeight + lineSpacing);
            }
            charcount += strLen;
        }

        return 0;
    }
    @Override
    public void doLayout() {
        int y = yPos;
        if (label != null) {
            y += font.getHeight();
        }
        lines = StringUtils.wrapToLines(textFieldText.toString(), font, Displayable.SCREEN_WIDTH);
        System.out.println("lines size is " + lines.size());
        itemHeight = y + lineSpacing + lines.size() * (font.getHeight() + lineSpacing);
    }
    public int getCurLine() {
        return curLine;
    }
    public int getCurOffset() {
        int    lineno         = getCurLine();
        String line           = (String) lines.get(lineno);
        int    curLineLength  = line.length();
        int    totalCharWidth = 0;
        char[] charArray      = line.toCharArray();
        if (cursorPosX > font.stringWidth(line)) {
            cursorPosX = font.stringWidth(line);

            return curLineLength;
        } else if ((cursorPosX == 0) && (curLineLength != 0)) {
            cursorPosX = font.stringWidth(line);

            return curLineLength;
        }
        for (int i = 0;i < curLineLength;i++) {
            if ((totalCharWidth += font.charWidth(charArray[i])) > cursorPosX) {
                cursorPosX = Math.min(totalCharWidth, cursorPosX);

                return --i;
            }
        }

        return 0;
    }
    public int getCursorPos(int lineno, int letterofsset) {
        int lettercount = 0;
        for (int i = 0;i < lineno;i++) {
            lettercount += ((String) lines.get(i)).length();
        }

        return lettercount + letterofsset;
    }
//  private int getProperCursorPos(int cursorPosX, int curLine) {
//      if (lines.size() == 0) {
//          return 0;
//      }
//      int lettercount = 0;
//      for (int i = 0; i < lines.size(); i++) {
//         if (cursorPosY == (textBoxY + i * (fontHeight + lineSpacing))) // getting line in box
//          {
//              String line = (String) lines.get(i);
//              int lineWidth = font.stringWidth(line);
//              if (cursorPosX < lineWidth) {
//                  int charWidth = 0;
//                  int count = 0;
//                  char charArray[] = line.toCharArray();
//                  while ((charWidth += font.charWidth(charArray[count])) < cursorPosX) {
//                      count++;
//                  }
//                  cursorPosX = charWidth;   //setting proper cursorPosX 
//
//                  cursorPos = lettercount + count; //setting cursorpos relative to offset of total string
//
//                  return cursorPosX;
//              } else {
//                  return lineWidth;
//              }
//          }
//          lettercount += ((String) lines.get(i)).length();
//      }
//      return cursorPosX;
//  }
    private void handleTextField() {
        if (isSelected) {
            isCursorVisible = true;
            if (Displayable.isKeyPressed) {
                switch (Displayable.j2seKeyPressedValue) {
                case KeyEvent.VK_LEFT :
                    cursorPos--;
                    handledKey = true;

                    break;
                case KeyEvent.VK_RIGHT :
                    cursorPos++;
                    handledKey = true;

                    break;
                case KeyEvent.VK_UP :
                    if (curLine > 0) {
                        curLine    -= 1;
                        cursorPos  = getCursorPos(getCurLine(), getCurOffset());
                        handledKey = true;
                    } else {
                        handledKey      = false;
                        isCursorVisible = false;
                    }

                    break;
                case KeyEvent.VK_DOWN :
                    if (curLine < totalLines - 1) {
                        curLine++;
                        cursorPos  = getCursorPos(getCurLine(), getCurOffset());
                        handledKey = true;
                    } else {
                        handledKey      = false;
                        isCursorVisible = false;
                    }

                    break;
                case KeyEvent.VK_DELETE :
                    System.out.println("delete pressed");
                    if (cursorPos < textFieldText.length()) {
                        delete(cursorPos, 1);
                    }
                    handledKey = true;

                    break;
                case KeyEvent.VK_BACK_SPACE :
                    if (cursorPos > 0) {
                        cursorPos--;
                        delete(cursorPos, 1);
                    }
                    handledKey = true;

                    break;
                case KeyEvent.VK_HOME :
                    cursorPos  = 0;
                    handledKey = true;

                    break;
                case KeyEvent.VK_END :
                    cursorPos  = textFieldText.length();
                    handledKey = true;

                    break;
                }
                if (cursorPos < 0) {
                    cursorPos = 0;
                }
                if (cursorPos > textFieldText.length()) {
                    cursorPos = textFieldText.length();
                }
            }
        } else {
            isCursorVisible = false;
        }
        lines = StringUtils.wrapToLines(textFieldText.toString(), font, getPreferredWidth() - padding - 4);
    }
    private void updateTextField() {
        if ((Displayable.j2seKeyPressedValue >= 32) && (Displayable.j2seKeyPressedValue <= 126)
                && Displayable.isKeyPressed)    // Textfield any character
        {
            System.out.println("printed is " + Displayable.keyText);
            if (Displayable.keyText != KeyEvent.CHAR_UNDEFINED) {
                switch (constraints) {
                case TextField.ANY :
                case TextField.EMAILADDR :
                case TextField.URL :
                    insert("" + Displayable.keyText, getCaretPosition());

                    break;
                case TextField.NUMERIC :
                case TextField.PHONENUMBER :
                case TextField.DECIMAL :
                    if ((Displayable.keyText >= 48) && (Displayable.keyText <= 57)) {
                        if (isKeyPad) {
                            String keys = keyChars[Displayable.keyText - 48];
                            int    len  = keys.length();
                            if (previousKey != Displayable.keyText) {
                                count = 0;
                                insert("" + keys.charAt(count), getCaretPosition());
                            } else {
                                count = count % len;
                                delete(cursorPos, 1);
                                insert("" + keys.charAt(count), getCaretPosition());
                            }
                            count++;
                            previousKey = Displayable.keyText;
                        } else {
                            insert("" + Displayable.keyText, getCaretPosition());
                        }
                    }

                    break;
                case TextField.UNEDITABLE :
                    isCursorVisible = false;

                    break;
                case TextField.PASSWORD :
                    isMask = true;
                }
            }
        }
    }
    @Override
    public void handleKey(int keyCode) {
        handleTextField();
    }
    public static boolean isFocused() {
        return isFocused;
    }
}
