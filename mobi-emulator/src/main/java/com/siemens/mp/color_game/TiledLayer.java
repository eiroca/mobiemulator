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
/*    */   extends Layer
/*    */ {
/*    */   private int cellWidth;
/*    */   private int cellHeight;
/*    */   private int columns;
/*    */   private int rows;
/*    */   
/*    */   public TiledLayer(int i, int j, Image image, int k, int l) {}
/*    */   
/*    */   public int createAnimatedTile(int i)
/*    */   {
/* 27 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setAnimatedTile(int i, int j) {}
/*    */   
/*    */ 
/*    */   public int getAnimatedTile(int i)
/*    */   {
/* 36 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setCell(int i, int j, int k) {}
/*    */   
/*    */ 
/*    */   public int getCell(int i, int j)
/*    */   {
/* 45 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void fillCells(int i, int j, int k, int l, int i1) {}
/*    */   
/*    */ 
/*    */   public final int getCellWidth()
/*    */   {
/* 54 */     return this.cellWidth;
/*    */   }
/*    */   
/*    */   public final int getCellHeight()
/*    */   {
/* 59 */     return this.cellHeight;
/*    */   }
/*    */   
/*    */   public final int getColumns()
/*    */   {
/* 64 */     return this.columns;
/*    */   }
/*    */   
/*    */   public final int getRows()
/*    */   {
/* 69 */     return this.rows;
/*    */   }
/*    */   
/*    */   public void setStaticTileSet(Image image, int i, int j) {}
/*    */   
/*    */   public final void paint(Graphics g) {}
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\color_game\TiledLayer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */