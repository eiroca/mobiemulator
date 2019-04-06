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
    /*    */ extends Layer
/*    */ {

  /*    */ private int refPixelX;
  /*    */ private int refPixelY;
  /*    */ private int frame;
  /*    */ private int rawFrameCount;
  /*    */ private int frameSequenceLength;
  /*    */ private int[] frameSequence;
  /*    */ private int transform;

  /*    */
  /*    */ public Sprite(final Image image) {
  }

  /*    */
  /*    */ public Sprite(final Image image, final int i, final int j) {
  }

  /*    */
  /*    */ public Sprite(final Sprite sprite) {
  }

  /*    */
  /*    */ public void setFrame(final int i)
  /*    */ {
    /* 38 */ frame = i;
    /*    */ }

  /*    */
  /*    */ public final int getFrame()
  /*    */ {
    /* 43 */ return frame;
    /*    */ }

  /*    */
  /*    */ public int getRawFrameCount()
  /*    */ {
    /* 48 */ return rawFrameCount;
    /*    */ }

  /*    */
  /*    */ public int getFrameSequenceLength()
  /*    */ {
    /* 53 */ return frameSequenceLength;
    /*    */ }

  /*    */
  /*    */
  /*    */
  /*    */ public void nextFrame() {
  }

  /*    */
  /*    */
  /*    */
  /*    */ public void prevFrame() {
  }

  /*    */
  /*    */
  /*    */ @Override
  public final void paint(final Graphics g) {
  }

  /*    */
  /*    */
  /*    */ public void setFrameSequence(final int[] ai)
  /*    */ {
    /* 70 */ frameSequence = ai;
    /*    */ }

  /*    */
  /*    */
  /*    */ public void setImage(final Image image, final int i, final int j) {
  }

  /*    */
  /*    */
  /*    */ public boolean collidesWith(final Sprite sprite, final boolean flag)
  /*    */ {
    /* 79 */ return false;
    /*    */ }

  /*    */
  /*    */ public boolean collidesWith(final Image image, final int i, final int j, final boolean flag)
  /*    */ {
    /* 84 */ return false;
    /*    */ }
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\Sprite.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
