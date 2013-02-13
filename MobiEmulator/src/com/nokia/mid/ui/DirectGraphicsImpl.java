/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.ui;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class DirectGraphicsImpl implements DirectGraphics {
    int     ARGBColor             = 0;
    int[][] MIDP2Manip2NokiaManip = {
        { 0, 24756 }, { 16384, 8372 }, { 8192, 16564 }, { 180, 24576 }, { 16474, 8462 }, { 90, 24846 }, { 270, 24666 },
        { 8282, 16654 }
    };
    public Graphics _NokiaGraphics;

    public DirectGraphicsImpl(Graphics g) {
        _NokiaGraphics = g;
    }

    public void setARGBColor(int argbColor) {
        _NokiaGraphics.setColor(ARGBColor);
    }

    public int getAlphaComponent() {
        return 0xFF;
    }

    public int getNativePixelFormat() {
        return 4444;
    }

    int nokiaManip2MIDP2Manip(int manipulation) {
        int i = 0;
        for (;i < this.MIDP2Manip2NokiaManip.length;++i) {
            for (int j = 0;j < this.MIDP2Manip2NokiaManip[i].length;++j) {
                if (this.MIDP2Manip2NokiaManip[i][j] == manipulation) {
                    return i;
                }
            }
        }

        return 0;
    }

    public void drawImage(Image img, int x, int y, int anchor, int manipulation) {
        // _NokiaGraphics.drawImage(img, x, y, anchor);
        int k = nokiaManip2MIDP2Manip(manipulation);
        // _NokiaGraphics.setColor(0);
        // _NokiaGraphics.fillRect(0, 0, 240, 160);
        _NokiaGraphics.drawRegion(img, 0, 0, img._image.getWidth(), img._image.getHeight(), k, x, y, anchor);
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor) {
        _NokiaGraphics.drawTriangle(x1, y1, x2, y2, x3, y3, argbColor);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor) {
        _NokiaGraphics.setColor(argbColor);
        _NokiaGraphics.fillTriangle(x1, y1, x2, y2, x3, y3);
    }

    public void drawPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
        _NokiaGraphics.drawPolygon(xPoints, xOffset, yPoints, yOffset, nPoints, argbColor);
    }

    public void fillPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
        _NokiaGraphics.fillPolygon(xPoints, xOffset, yPoints, yOffset, nPoints, argbColor);
    }

    public void drawPixels(int[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width,
                           int height, int manipulation, int format) {
        BufferedImage img = new BufferedImage(width, height,
                                transparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        img.setRGB(width, height, y, y, pixels, offset, scanlength);
        AffineTransform at = _NokiaGraphics.getAffineTransform(width, height, manipulation, x, y, 0);
        _NokiaGraphics.drawRegion(img, x, y, width, height, at, x, y, 0);
        System.out.println("in drawpixel int");
    }

    public void drawPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width,
                           int height, int manipulation, int format) {}

    public void drawPixels(short[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width,
                           int height, int manipulation, int format) {
        BufferedImage img = new BufferedImage(width, height,
                                transparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        img.setRGB(width, height, y, y, Short_Two_IntArr(pixels, offset, scanlength, width), offset, scanlength);
        AffineTransform at = _NokiaGraphics.getAffineTransform(width, height, manipulation, x, y, 0);
        _NokiaGraphics.drawRegion(img, x, y, width, height, at, x, y, 0);
        System.out.println("in drawpixel short");
    }

    public void getPixels(int[] pixels, int offset, int scanlength, int x, int y, int width, int height, int format) {}

    public void getPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width,
                          int height, int format) {}

    public void getPixels(short[] pixels, int offset, int scanlength, int x, int y, int width, int height,
                          int format) {}

    private int[] Short_Two_IntArr(short[] pixels, int offset, int scanlength, int width) {
        int arr[] = new int[pixels.length << 1];
        for (int i = offset;i < pixels.length;i += scanlength) {
            for (int j = 0;j < scanlength;j++) {
                int l = i + j;
                arr[i + j] = ((pixels[l] & 0xF000) << 16) | (pixels[l] & 0xF00) << 12 | (pixels[l] & 0xF0) << 8
                             | (pixels[l] & 0xF) << 4;
            }
        }

        return arr;
    }
}
