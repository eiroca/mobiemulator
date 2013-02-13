/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.ui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface DirectGraphics {
    public static final int FLIP_HORIZONTAL           = 8192;
    public static final int FLIP_VERTICAL             = 16384;
    public static final int ROTATE_180                = 180;
    public static final int ROTATE_270                = 270;
    public static final int ROTATE_90                 = 90;
    public static final int TYPE_BYTE_1_GRAY          = 1;
    public static final int TYPE_BYTE_1_GRAY_VERTICAL = -1;
    public static final int TYPE_BYTE_2_GRAY          = 2;
    public static final int TYPE_BYTE_332_RGB         = 332;
    public static final int TYPE_BYTE_4_GRAY          = 4;
    public static final int TYPE_BYTE_8_GRAY          = 8;
    public static final int TYPE_INT_8888_ARGB        = 8888;
    public static final int TYPE_INT_888_RGB          = 888;
    public static final int TYPE_USHORT_1555_ARGB     = 1555;
    public static final int TYPE_USHORT_4444_ARGB     = 4444;
    public static final int TYPE_USHORT_444_RGB       = 444;
    public static final int TYPE_USHORT_555_RGB       = 555;
    public static final int TYPE_USHORT_565_RGB       = 565;

    void setARGBColor(int argbColor);

    int getAlphaComponent();

    int getNativePixelFormat();

    void drawImage(javax.microedition.lcdui.Image img, int x, int y, int anchor, int manipulation);

    void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor);

    void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor);

    void drawPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor);

    void fillPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor);

    void drawPixels(int[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width,
                    int height, int manipulation, int format);

    void drawPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width,
                    int height, int manipulation, int format);

    void drawPixels(short[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width,
                    int height, int manipulation, int format);

    void getPixels(int[] pixels, int offset, int scanlength, int x, int y, int width, int height, int format);

    void getPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width,
                   int height, int format);

    void getPixels(short[] pixels, int offset, int scanlength, int x, int y, int width, int height, int format);
}
