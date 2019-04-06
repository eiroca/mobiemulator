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
/*    */ public class TiledLayer
    /*    */ extends Layer
/*    */ {

  /*    */ private int cellWidth;
  /*    */ private int cellHeight;
  /*    */ private int columns;
  /*    */ private int rows;

  /*    */
  /*    */ public TiledLayer(final int i, final int j, final Image image, final int k, final int l) {
  }

  /*    */
  /*    */ public int createAnimatedTile(final int i)
  /*    */ {
    /* 27 */ return 0;
    /*    */ }

  /*    */
  /*    */
  /*    */ public void setAnimatedTile(final int i, final int j) {
  }

  /*    */
  /*    */
  /*    */ public int getAnimatedTile(final int i)
  /*    */ {
    /* 36 */ return 0;
    /*    */ }

  /*    */
  /*    */
  /*    */ public void setCell(final int i, final int j, final int k) {
  }

  /*    */
  /*    */
  /*    */ public int getCell(final int i, final int j)
  /*    */ {
    /* 45 */ return 0;
    /*    */ }

  /*    */
  /*    */
  /*    */ public void fillCells(final int i, final int j, final int k, final int l, final int i1) {
  }

  /*    */
  /*    */
  /*    */ public final int getCellWidth()
  /*    */ {
    /* 54 */ return cellWidth;
    /*    */ }

  /*    */
  /*    */ public final int getCellHeight()
  /*    */ {
    /* 59 */ return cellHeight;
    /*    */ }

  /*    */
  /*    */ public final int getColumns()
  /*    */ {
    /* 64 */ return columns;
    /*    */ }

  /*    */
  /*    */ public final int getRows()
  /*    */ {
    /* 69 */ return rows;
    /*    */ }

  /*    */
  /*    */ public void setStaticTileSet(final Image image, final int i, final int j) {
  }

  /*    */
  /*    */ @Override
  public final void paint(final Graphics g) {
  }
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\TiledLayer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
