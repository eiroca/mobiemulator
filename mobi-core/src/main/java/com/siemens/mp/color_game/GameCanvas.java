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
    /*    */ extends Canvas
/*    */ {

  /*    */ public static final int UP_PRESSED = 2;
  /*    */ public static final int DOWN_PRESSED = 64;
  /*    */ public static final int LEFT_PRESSED = 4;
  /*    */ public static final int RIGHT_PRESSED = 32;
  /*    */ public static final int FIRE_PRESSED = 256;
  /*    */ public static final int GAME_A_PRESSED = 512;
  /*    */ public static final int GAME_B_PRESSED = 1024;
  /*    */ public static final int GAME_C_PRESSED = 2048;
  /*    */ public static final int GAME_D_PRESSED = 4096;
  /*    */ private Graphics graphics;
  /*    */ private int keyStates;

  /*    */
  /*    */ protected GameCanvas(final boolean flag) {
  }

  /*    */
  /*    */ protected Graphics getGraphics()
  /*    */ {
    /* 31 */ return graphics;
    /*    */ }

  /*    */
  /*    */ public int getKeyStates()
  /*    */ {
    /* 36 */ return keyStates;
    /*    */ }

  /*    */
  /*    */ @Override
  public void paint(final Graphics g) {
  }

  /*    */
  /*    */ public void flushGraphics(final int i, final int j, final int k, final int l) {
  }

  /*    */
  /*    */ public void flushGraphics() {
  }
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\GameCanvas.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
