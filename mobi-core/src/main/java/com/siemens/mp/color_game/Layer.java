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

  /*    */ private int x;
  /*    */ private int y;
  /*    */ private int width;
  /*    */ private int height;
  /*    */ private boolean visible;

  /*    */
  /*    */ public void setPosition(final int i, final int j) {
  }

  /*    */
  /*    */ public void move(final int i, final int j) {
  }

  /*    */
  /*    */ public final int getX()
  /*    */ {
    /* 32 */ return x;
    /*    */ }

  /*    */
  /*    */ public final int getY()
  /*    */ {
    /* 37 */ return y;
    /*    */ }

  /*    */
  /*    */ public final int getWidth()
  /*    */ {
    /* 42 */ return width;
    /*    */ }

  /*    */
  /*    */ public final int getHeight()
  /*    */ {
    /* 47 */ return height;
    /*    */ }

  /*    */
  /*    */ public void setVisible(final boolean flag)
  /*    */ {
    /* 52 */ visible = flag;
    /*    */ }

  /*    */
  /*    */ public final boolean isVisible()
  /*    */ {
    /* 57 */ return false;
    /*    */ }

  /*    */
  /*    */ public abstract void paint(Graphics paramGraphics);
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\Layer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
