/*
 * This program is free software. It comes without any warranty, to the extent permitted by
 * applicable law. You can redistribute it and/or modify it under the terms of the Do What The Fuck
 * You Want To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */

package javax.microedition.lcdui;

public class Font {

  public static final int FACE_MONOSPACE = 32;
  public static final int FACE_PROPORTIONAL = 64;
  public static final int FACE_SYSTEM = 0;
  public static final int SIZE_LARGE = 16;
  public static final int SIZE_MEDIUM = 0;
  public static final int SIZE_SMALL = 8;
  public static final int STYLE_BOLD = 1;
  public static final int STYLE_ITALIC = 2;
  public static final int STYLE_PLAIN = 0;
  public static final int STYLE_UNDERLINED = 4;
  static final Font DEFAULT_FONT = new Font(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);

  /** FontMetrics object does most of the font width and height stuff for us */
  java.awt.FontMetrics _fm;

  /**
   * Reference to an AWT Font object, created based on the MIDP font properties requested
   */
  java.awt.Font _font;
  int face, style, size;

  /**
   * Constructor. Take the MIDP font parameters, and determine an appropriate AWT font to use.
   */
  public Font(final int face, final int style, final int size) {
    this.face = face;
    this.style = style;
    this.size = size;
    String font_name = "SansSerif";
    if (face == Font.FACE_MONOSPACE) {
      font_name = "Monospaced";
    }
    int awt_style = java.awt.Font.PLAIN;
    switch (style) {
      case STYLE_BOLD:
        awt_style = java.awt.Font.BOLD;

        break;
      case STYLE_ITALIC:
        awt_style = java.awt.Font.ITALIC;

        break;
      // Doesn't seem to be any underlined font support in plain old AWT,
      // never mind.
    }
    int points = 11; // medium
    switch (size) {
      case SIZE_SMALL:
        points = 9;

        break;
      case SIZE_LARGE:
        points = 14;

        break;
    }
    _font = new java.awt.Font(font_name, awt_style, points);
    _fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics(_font);
  }

  public static Font getFont(final int face, final int style, final int size) {
    return new Font(face, style, size);
  }

  public static Font getDefaultFont() {
    return Font.DEFAULT_FONT;
  }

  public int charWidth(final char c) {
    return _fm.charWidth(c);
  }

  public int charsWidth(final char[] c, final int ofs, final int len) {
    return _fm.charsWidth(c, ofs, len);
  }

  public int getBaselinePosition() {
    return _fm.getAscent();
  }

  public int getHeight() {
    return _fm.getHeight();
  }

  public int stringWidth(final String s) {
    return _fm.stringWidth(s);
  }

  public int substringWidth(final String s, final int offset, final int length) {
    return _fm.stringWidth(s.substring(offset, offset + length));
  }

  public int getFace() {
    return face;
  }

  public int getSize() {
    return size;
  }

  public int getStyle() {
    return style;
  }

  public boolean isBold() {
    return (style & Font.STYLE_BOLD) != 0;
  }

  public boolean isUnderlined() {
    return (style & Font.STYLE_UNDERLINED) != 0;
  }

  public boolean isItalic() {
    return (style & Font.STYLE_ITALIC) != 0;
  }

  public boolean isPLAIN() {
    return style == 0;
  }
}
