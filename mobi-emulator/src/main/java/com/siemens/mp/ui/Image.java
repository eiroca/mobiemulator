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
/*     */   public static final int COLOR_BMP_8BIT = 5;
/*     */   private int height;
/*     */   private int width;
/*     */   private Image nativeImage;
/*     */   
/*     */   protected Image() {}
/*     */   
/*     */   public Image(int i, int j) {}
/*     */   
/*     */   public Image(Image image) {}
/*     */   
/*     */   public Image(byte[] abyte0, int i, int j) {}
/*     */   
/*     */   public Image(byte[] abyte0, int i, int j, boolean flag) {}
/*     */   
/*     */   public Image(String s, boolean flag)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public Image(byte[] abyte0) {}
/*     */   
/*     */   public Image(byte[] abyte0, int i, int j, int k)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public int getHeight()
/*     */   {
/*  53 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getWidth()
/*     */   {
/*  58 */     return this.width;
/*     */   }
/*     */   
/*     */   public static Image createImageWithScaling(String s)
/*     */     throws IOException
/*     */   {
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   public static Image createImageWithoutScaling(String s)
/*     */     throws IOException
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */   
/*     */   public static Image createImageFromBitmap(byte[] abyte0, int i, int j)
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   public static Image createRGBImage(byte[] abyte0, int i, int j, int k)
/*     */     throws ArrayIndexOutOfBoundsException, IOException
/*     */   {
/*  81 */     return null;
/*     */   }
/*     */   
/*     */   public static Image createTransparentImageFromBitmap(byte[] abyte0, int i, int j)
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void mirrorImageHorizontally(javax.microedition.lcdui.Image image) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public static void mirrorImageVertically(javax.microedition.lcdui.Image image) {}
/*     */   
/*     */ 
/*     */   protected static void setNativeImage(javax.microedition.lcdui.Image image, Image image1) {}
/*     */   
/*     */ 
/*     */   public static Image getNativeImage(javax.microedition.lcdui.Image image)
/*     */   {
/* 103 */     return null;
/*     */   }
/*     */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\m\\ui\Image.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */