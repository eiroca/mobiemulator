/*    */ package com.siemens.mp.color_game;
/*    */ 
/*    */ import javax.microedition.lcdui.Graphics;
/*    */ import javax.microedition.lcdui.Image;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sprite
/*    */   extends Layer
/*    */ {
/*    */   private int refPixelX;
/*    */   private int refPixelY;
/*    */   private int frame;
/*    */   private int rawFrameCount;
/*    */   private int frameSequenceLength;
/*    */   private int[] frameSequence;
/*    */   private int transform;
/*    */   
/*    */   public Sprite(Image image) {}
/*    */   
/*    */   public Sprite(Image image, int i, int j) {}
/*    */   
/*    */   public Sprite(Sprite sprite) {}
/*    */   
/*    */   public void setFrame(int i)
/*    */   {
/* 38 */     this.frame = i;
/*    */   }
/*    */   
/*    */   public final int getFrame()
/*    */   {
/* 43 */     return this.frame;
/*    */   }
/*    */   
/*    */   public int getRawFrameCount()
/*    */   {
/* 48 */     return this.rawFrameCount;
/*    */   }
/*    */   
/*    */   public int getFrameSequenceLength()
/*    */   {
/* 53 */     return this.frameSequenceLength;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void nextFrame() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void prevFrame() {}
/*    */   
/*    */ 
/*    */   public final void paint(Graphics g) {}
/*    */   
/*    */ 
/*    */   public void setFrameSequence(int[] ai)
/*    */   {
/* 70 */     this.frameSequence = ai;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setImage(Image image, int i, int j) {}
/*    */   
/*    */ 
/*    */   public boolean collidesWith(Sprite sprite, boolean flag)
/*    */   {
/* 79 */     return false;
/*    */   }
/*    */   
/*    */   public boolean collidesWith(Image image, int i, int j, boolean flag)
/*    */   {
/* 84 */     return false;
/*    */   }
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\Sprite.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */