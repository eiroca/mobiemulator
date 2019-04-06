/*     */ package com.siemens.mp.ui;

/*     */
/*     */ import java.io.IOException;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class Image
/*     */ {

  /*     */ public static final int COLOR_BMP_8BIT = 5;
  /*     */ private int height;
  /*     */ private int width;
  /*     */ private Image nativeImage;

  /*     */
  /*     */ protected Image() {
  }

  /*     */
  /*     */ public Image(final int i, final int j) {
  }

  /*     */
  /*     */ public Image(final Image image) {
  }

  /*     */
  /*     */ public Image(final byte[] abyte0, final int i, final int j) {
  }

  /*     */
  /*     */ public Image(final byte[] abyte0, final int i, final int j, final boolean flag) {
  }

  /*     */
  /*     */ public Image(final String s, final boolean flag)
      /*     */ throws IOException
  /*     */ {
  }

  /*     */
  /*     */ public Image(final byte[] abyte0) {
  }

  /*     */
  /*     */ public Image(final byte[] abyte0, final int i, final int j, final int k)
      /*     */ throws IOException
  /*     */ {
  }

  /*     */
  /*     */ public int getHeight()
  /*     */ {
    /*  53 */ return height;
    /*     */ }

  /*     */
  /*     */ public int getWidth()
  /*     */ {
    /*  58 */ return width;
    /*     */ }

  /*     */
  /*     */ public static Image createImageWithScaling(final String s)
      /*     */ throws IOException
  /*     */ {
    /*  64 */ return null;
    /*     */ }

  /*     */
  /*     */ public static Image createImageWithoutScaling(final String s)
      /*     */ throws IOException
  /*     */ {
    /*  70 */ return null;
    /*     */ }

  /*     */
  /*     */ public static Image createImageFromBitmap(final byte[] abyte0, final int i, final int j)
  /*     */ {
    /*  75 */ return null;
    /*     */ }

  /*     */
  /*     */ public static Image createRGBImage(final byte[] abyte0, final int i, final int j, final int k)
      /*     */ throws ArrayIndexOutOfBoundsException, IOException
  /*     */ {
    /*  81 */ return null;
    /*     */ }

  /*     */
  /*     */ public static Image createTransparentImageFromBitmap(final byte[] abyte0, final int i, final int j)
  /*     */ {
    /*  86 */ return null;
    /*     */ }

  /*     */
  /*     */
  /*     */
  /*     */ public static void mirrorImageHorizontally(final javax.microedition.lcdui.Image image) {
  }

  /*     */
  /*     */
  /*     */
  /*     */ public static void mirrorImageVertically(final javax.microedition.lcdui.Image image) {
  }

  /*     */
  /*     */
  /*     */ protected static void setNativeImage(final javax.microedition.lcdui.Image image, final Image image1) {
  }

  /*     */
  /*     */
  /*     */ public static Image getNativeImage(final javax.microedition.lcdui.Image image)
  /*     */ {
    /* 103 */ return null;
    /*     */ }
  /*     */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\m\\ui\Image.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
