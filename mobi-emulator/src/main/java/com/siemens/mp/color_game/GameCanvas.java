/*    */ package com.siemens.mp.color_game;
/*    */ 
/*    */ import javax.microedition.lcdui.Canvas;
/*    */ import javax.microedition.lcdui.Graphics;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GameCanvas
/*    */   extends Canvas
/*    */ {
/*    */   public static final int UP_PRESSED = 2;
/*    */   public static final int DOWN_PRESSED = 64;
/*    */   public static final int LEFT_PRESSED = 4;
/*    */   public static final int RIGHT_PRESSED = 32;
/*    */   public static final int FIRE_PRESSED = 256;
/*    */   public static final int GAME_A_PRESSED = 512;
/*    */   public static final int GAME_B_PRESSED = 1024;
/*    */   public static final int GAME_C_PRESSED = 2048;
/*    */   public static final int GAME_D_PRESSED = 4096;
/*    */   private Graphics graphics;
/*    */   private int keyStates;
/*    */   
/*    */   protected GameCanvas(boolean flag) {}
/*    */   
/*    */   protected Graphics getGraphics()
/*    */   {
/* 31 */     return this.graphics;
/*    */   }
/*    */   
/*    */   public int getKeyStates()
/*    */   {
/* 36 */     return this.keyStates;
/*    */   }
/*    */   
/*    */   public void paint(Graphics g) {}
/*    */   
/*    */   public void flushGraphics(int i, int j, int k, int l) {}
/*    */   
/*    */   public void flushGraphics() {}
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\GameCanvas.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */