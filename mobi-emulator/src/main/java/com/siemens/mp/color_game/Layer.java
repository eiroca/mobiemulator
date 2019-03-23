/*    */ package com.siemens.mp.color_game;
/*    */ 
/*    */ import javax.microedition.lcdui.Graphics;
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
/*    */ public abstract class Layer
/*    */ {
/*    */   private int x;
/*    */   private int y;
/*    */   private int width;
/*    */   private int height;
/*    */   private boolean visible;
/*    */   
/*    */   public void setPosition(int i, int j) {}
/*    */   
/*    */   public void move(int i, int j) {}
/*    */   
/*    */   public final int getX()
/*    */   {
/* 32 */     return this.x;
/*    */   }
/*    */   
/*    */   public final int getY()
/*    */   {
/* 37 */     return this.y;
/*    */   }
/*    */   
/*    */   public final int getWidth()
/*    */   {
/* 42 */     return this.width;
/*    */   }
/*    */   
/*    */   public final int getHeight()
/*    */   {
/* 47 */     return this.height;
/*    */   }
/*    */   
/*    */   public void setVisible(boolean flag)
/*    */   {
/* 52 */     this.visible = flag;
/*    */   }
/*    */   
/*    */   public final boolean isVisible()
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public abstract void paint(Graphics paramGraphics);
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\Layer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */