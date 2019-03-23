package com.nokia.mid.ui;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class DirectGraphicsImpl implements DirectGraphics {

  int ARGBColor = 0;
  int[][] MIDP2Manip2NokiaManip = {
      {
          0, 24756
      }, {
          16384, 8372
      }, {
          8192, 16564
      }, {
          180, 24576
      }, {
          16474, 8462
      }, {
          90, 24846
      }, {
          270, 24666
      },
      {
          8282, 16654
      }
  };
  public Graphics _NokiaGraphics;

  public DirectGraphicsImpl(final Graphics g) {
    _NokiaGraphics = g;
  }

  @Override
  public void setARGBColor(final int argbColor) {
    _NokiaGraphics.setColor(ARGBColor);
  }

  @Override
  public int getAlphaComponent() {
    return 0xFF;
  }

  @Override
  public int getNativePixelFormat() {
    return 4444;
  }

  int nokiaManip2MIDP2Manip(final int manipulation) {
    int i = 0;
    for (; i < MIDP2Manip2NokiaManip.length; ++i) {
      for (int j = 0; j < MIDP2Manip2NokiaManip[i].length; ++j) {
        if (MIDP2Manip2NokiaManip[i][j] == manipulation) { return i; }
      }
    }

    return 0;
  }

  @Override
  public void drawImage(final Image img, final int x, final int y, final int anchor, final int manipulation) {
    // _NokiaGraphics.drawImage(img, x, y, anchor);
    final int k = nokiaManip2MIDP2Manip(manipulation);
    // _NokiaGraphics.setColor(0);
    // _NokiaGraphics.fillRect(0, 0, 240, 160);
    _NokiaGraphics.drawRegion(img, 0, 0, img._image.getWidth(), img._image.getHeight(), k, x, y, anchor);
  }

  @Override
  public void drawTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
    _NokiaGraphics.drawTriangle(x1, y1, x2, y2, x3, y3, argbColor);
  }

  @Override
  public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
    _NokiaGraphics.setColor(argbColor);
    _NokiaGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
  }

  @Override
  public void drawPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    _NokiaGraphics.drawPolygon(xPoints, xOffset, yPoints, yOffset, nPoints, argbColor);
  }

  @Override
  public void fillPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    _NokiaGraphics.fillPolygon(xPoints, xOffset, yPoints, yOffset, nPoints, argbColor);
  }

  @Override
  public void drawPixels(final int[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
    final BufferedImage img = new BufferedImage(width, height, transparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    img.setRGB(width, height, y, y, pixels, offset, scanlength);
    final AffineTransform at = _NokiaGraphics.getAffineTransform(width, height, manipulation, x, y, 0);
    _NokiaGraphics.drawRegion(img, x, y, width, height, at, x, y, 0);
    System.out.println("in drawpixel int");
  }

  @Override
  public void drawPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
  }

  @Override
  public void drawPixels(final short[] pixels, final boolean transparency, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int manipulation, final int format) {
    final BufferedImage img = new BufferedImage(width, height, transparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    img.setRGB(width, height, y, y, Short_Two_IntArr(pixels, offset, scanlength, width), offset, scanlength);
    final AffineTransform at = _NokiaGraphics.getAffineTransform(width, height, manipulation, x, y, 0);
    _NokiaGraphics.drawRegion(img, x, y, width, height, at, x, y, 0);
    System.out.println("in drawpixel short");
  }

  @Override
  public void getPixels(final int[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
  }

  @Override
  public void getPixels(final byte[] pixels, final byte[] transparencyMask, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
  }

  @Override
  public void getPixels(final short[] pixels, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final int format) {
  }

  private int[] Short_Two_IntArr(final short[] pixels, final int offset, final int scanlength, final int width) {
    final int arr[] = new int[pixels.length << 1];
    for (int i = offset; i < pixels.length; i += scanlength) {
      for (int j = 0; j < scanlength; j++) {
        final int l = i + j;
        arr[i + j] = ((pixels[l] & 0xF000) << 16) | ((pixels[l] & 0xF00) << 12) | ((pixels[l] & 0xF0) << 8) | ((pixels[l] & 0xF) << 4);
      }
    }

    return arr;
  }

}
