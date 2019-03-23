// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov Date: 3/18/2011 12:49:53 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check
// often for new version!
// Decompiler options: packimports(3)
// Source File Name: TiledLayer.java

package javax.microedition.lcdui.game;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// Referenced classes of package javax.microedition.lcdui.game:
// Layer
public class TiledLayer extends Layer {

  private int anim_to_static[];
  private int cellHeight;
  private int cellMatrix[][];
  private int cellWidth;
  private int columns;
  private int numOfAnimTiles;
  private int numberOfTiles;
  private int rows;
  Image sourceImage;
  private int tileArrayLength;
  int tileSetX[];
  int tileSetY[];
  private boolean updated;
  private int x_dest[];
  private int x_src[];
  private int y_dest[];
  private int y_src[];

  public TiledLayer(final int columns, final int rows, final Image image, final int tileWidth, final int tileHeight) {
    super(((columns >= 1) && (tileWidth >= 1)) ? columns * tileWidth
        : -1,
        ((rows >= 1) && (tileHeight >= 1)) ? rows * tileHeight
            : -1);
    updated = true;
    x_src = null;
    y_src = null;
    x_dest = null;
    y_dest = null;
    if (((image.getWidth() % tileWidth) != 0) || ((image.getHeight() % tileHeight) != 0)) {
      throw new IllegalArgumentException();
    }
    else {
      this.columns = columns;
      this.rows = rows;
      cellMatrix = new int[rows][columns];
      final int noOfFrames = (image.getWidth() / tileWidth) * (image.getHeight() / tileHeight);
      createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, true);

      return;
    }
  }

  public int createAnimatedTile(final int staticTileIndex) {
    if ((staticTileIndex < 0) || (staticTileIndex >= numberOfTiles)) { throw new IndexOutOfBoundsException(); }
    if (anim_to_static == null) {
      anim_to_static = new int[4];
      numOfAnimTiles = 1;
    }
    else if (numOfAnimTiles == anim_to_static.length) {
      final int new_anim_tbl[] = new int[anim_to_static.length * 2];
      System.arraycopy(anim_to_static, 0, new_anim_tbl, 0, anim_to_static.length);
      anim_to_static = new_anim_tbl;
    }
    anim_to_static[numOfAnimTiles] = staticTileIndex;
    numOfAnimTiles++;

    return -(numOfAnimTiles - 1);
  }

  public void setAnimatedTile(int animatedTileIndex, final int staticTileIndex) {
    if ((staticTileIndex < 0) || (staticTileIndex >= numberOfTiles)) { throw new IndexOutOfBoundsException(); }
    animatedTileIndex = -animatedTileIndex;
    if ((anim_to_static == null) || (animatedTileIndex <= 0) || (animatedTileIndex >= numOfAnimTiles)) {
      throw new IndexOutOfBoundsException();
    }
    else {
      anim_to_static[animatedTileIndex] = staticTileIndex;

      return;
    }
  }

  public int getAnimatedTile(int animatedTileIndex) {
    animatedTileIndex = -animatedTileIndex;
    if ((anim_to_static == null) || (animatedTileIndex <= 0) || (animatedTileIndex >= numOfAnimTiles)) {
      throw new IndexOutOfBoundsException();
    }
    else {
      return anim_to_static[animatedTileIndex];
    }
  }

  public void setCell(final int col, final int row, final int tileIndex) {
    if ((col < 0) || (col >= columns) || (row < 0) || (row >= rows)) { throw new IndexOutOfBoundsException(); }
    if (tileIndex > 0) {
      if (tileIndex >= numberOfTiles) { throw new IndexOutOfBoundsException(); }
    }
    else if ((tileIndex < 0) && ((anim_to_static == null) || (-tileIndex >= numOfAnimTiles))) { throw new IndexOutOfBoundsException(); }
    cellMatrix[row][col] = tileIndex;
    updated = true;
  }

  public int getCell(final int col, final int row) {
    if ((col < 0) || (col >= columns) || (row < 0) || (row >= rows)) {
      throw new IndexOutOfBoundsException();
    }
    else {
      return cellMatrix[row][col];
    }
  }

  public void fillCells(final int col, final int row, final int numCols, final int numRows, final int tileIndex) {
    if ((numCols < 0) || (numRows < 0)) { throw new IllegalArgumentException(); }
    if ((col < 0) || (col >= columns) || (row < 0) || (row >= rows) || ((col + numCols) > columns)
        || ((row + numRows) > rows)) { throw new IndexOutOfBoundsException(); }
    if (tileIndex > 0) {
      if (tileIndex >= numberOfTiles) { throw new IndexOutOfBoundsException(); }
    }
    else if ((tileIndex < 0) && ((anim_to_static == null) || (-tileIndex >= numOfAnimTiles))) { throw new IndexOutOfBoundsException(); }
    for (int rowCount = row; rowCount < (row + numRows); rowCount++) {
      for (int columnCount = col; columnCount < (col + numCols); columnCount++) {
        cellMatrix[rowCount][columnCount] = tileIndex;
      }
    }
    updated = true;
  }

  public final int getCellWidth() {
    return cellWidth;
  }

  public final int getCellHeight() {
    return cellHeight;
  }

  public final int getColumns() {
    return columns;
  }

  public final int getRows() {
    return rows;
  }

  public void setStaticTileSet(final Image image, final int tileWidth, final int tileHeight) {
    updated = true;
    if ((tileWidth < 1) || (tileHeight < 1) || ((image.getWidth() % tileWidth) != 0)
        || ((image.getHeight() % tileHeight) != 0)) { throw new IllegalArgumentException(); }
    setWidthImpl(columns * tileWidth);
    setHeightImpl(rows * tileHeight);
    final int noOfFrames = (image.getWidth() / tileWidth) * (image.getHeight() / tileHeight);
    if (noOfFrames >= (numberOfTiles - 1)) {
      createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, true);
    }
    else {
      createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, false);
    }
  }

  @Override
  public final void paint(final Graphics g) {
    if (g == null) { throw new NullPointerException(); }
    if (visible) {
      int tileIndex = 0;
      if (updated) {
        int arrayLength = 0;
        for (final int[] element : cellMatrix) {
          final int totalCols = element.length;
          for (int column = 0; column < totalCols; column++) {
            tileIndex = element[column];
            if (tileIndex != 0) {
              arrayLength++;
            }
          }
        }
        if ((x_src == null) || (arrayLength > x_src.length)) {
          x_src = new int[arrayLength];
          y_src = new int[arrayLength];
          x_dest = new int[arrayLength];
          y_dest = new int[arrayLength];
        }
        tileArrayLength = arrayLength;
        updated = false;
      }
      int ty = y;
      int arrayIndex = 0;
      for (int row = 0; row < cellMatrix.length;) {
        int tx = x;
        final int totalCols = cellMatrix[row].length;
        for (int column = 0; column < totalCols;) {
          tileIndex = cellMatrix[row][column];
          if (tileIndex != 0) {
            if (tileIndex < 0) {
              tileIndex = getAnimatedTile(tileIndex);
            }
            x_src[arrayIndex] = tileSetX[tileIndex];
            y_src[arrayIndex] = tileSetY[tileIndex];
            x_dest[arrayIndex] = tx;
            y_dest[arrayIndex] = ty;
            arrayIndex++;
          }
          column++;
          tx += cellWidth;
        }
        row++;
        ty += cellHeight;
      }
      drawTiledRegion(g, sourceImage, tileArrayLength, x_src, y_src, cellWidth, cellHeight, 0, x_dest, y_dest,
          20);
    }
  }

  private void drawTiledRegion(final Graphics g, final Image image, final int tileArrayLength, final int[] x_src, final int[] y_src, final int cellWidth, final int cellHeight, final int offset, final int[] x_dest, final int[] y_dest, final int anchor) {
    for (int i = 0; i < tileArrayLength; i++) {
      g.drawRegion(image, x_src[i], y_src[i], cellWidth, cellHeight, 0, x_dest[i], y_dest[i], anchor);
    }
  }

  private void createStaticSet(final Image image, final int noOfFrames, final int tileWidth, final int tileHeight, final boolean maintainIndices) {
    cellWidth = tileWidth;
    cellHeight = tileHeight;
    final int imageW = image.getWidth();
    final int imageH = image.getHeight();
    sourceImage = image;
    numberOfTiles = noOfFrames;
    tileSetX = new int[numberOfTiles];
    tileSetY = new int[numberOfTiles];
    if (!maintainIndices) {
      for (rows = 0; rows < cellMatrix.length; rows++) {
        final int totalCols = cellMatrix[rows].length;
        for (columns = 0; columns < totalCols; columns++) {
          cellMatrix[rows][columns] = 0;
        }
      }
      anim_to_static = null;
    }
    int currentTile = 1;
    for (int y = 0; y < imageH; y += tileHeight) {
      for (int x = 0; x < imageW; x += tileWidth) {
        tileSetX[currentTile] = x;
        tileSetY[currentTile] = y;
        currentTile++;
      }
    }
  }
}
