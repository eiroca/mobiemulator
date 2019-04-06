package com.nokia.mid.ui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class DirectUtils {

  public static DirectGraphics getDirectGraphics(final javax.microedition.lcdui.Graphics g) {
    return new DirectGraphicsImpl(g) {
    };
  }

  public static javax.microedition.lcdui.Image createImage(final byte[] imageData, final int imageOffset, final int imageLength) {
    final javax.microedition.lcdui.Image img = javax.microedition.lcdui.Image.createImage(imageData, imageOffset, imageLength);
    return img;
  }

  public static javax.microedition.lcdui.Image createImage(final int width, final int height, final int ARGBcolor) {
    final javax.microedition.lcdui.Image img = javax.microedition.lcdui.Image.createImage(width, height);
    return img;
  }

  public static javax.microedition.lcdui.Font getFont(final int face, final int style, final int height) {
    final javax.microedition.lcdui.Font f = new javax.microedition.lcdui.Font(face, style, height);
    return f;
  }

}
