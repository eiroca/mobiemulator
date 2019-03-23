package Emulator;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class PaintTask {

  public final static int DRAW_RECT = 0;
  public final static int FILL_RECT = PaintTask.DRAW_RECT + 1;
  public final static int DRAW_LINE = PaintTask.FILL_RECT + 1;
  public final static int DRAW_ROUNDRECT = PaintTask.DRAW_LINE + 1;
  public final static int FILL_ROUNDRECT = PaintTask.DRAW_ROUNDRECT + 1;
  public final static int DRAW_ARC = PaintTask.FILL_ROUNDRECT + 1;
  public final static int FILL_ARC = PaintTask.DRAW_ARC + 1;
  public final static int DRAW_TRIANGLE = PaintTask.FILL_ARC + 1;
  public final static int FILL_TRIANGLE = PaintTask.DRAW_TRIANGLE + 1;
  public final static int DRAW_STRING = PaintTask.FILL_TRIANGLE + 1;
  public final static int DRAW_IMAGE = PaintTask.DRAW_STRING + 1;
  public final static int DRAW_RGB = PaintTask.DRAW_IMAGE + 1;
  public final static int DRAW_REGION = PaintTask.DRAW_RGB + 1;
  public final static int COPY_AREA = PaintTask.DRAW_REGION + 1;
  public final static int GET_RGB = PaintTask.COPY_AREA + 1;
  public int clipHeight;
  public int clipWidth;
  public int clipX;
  public int clipY;
  public int color;
  private Graphics paintGraphics;
  public int translateX;
  public int translateY;

  public PaintTask() {
  }

  public void getGraphicsSettings(final Graphics g) {
    color = g.getColor();
    translateX = g.getTranslateX();
    translateY = g.getTranslateY();
    clipX = g.getClipX();
    clipY = g.getClipY();
    clipWidth = g.getClipWidth();
    clipHeight = g.getClipHeight();
  }

  //  public void setPaintGraphics(Graphics g)
  //  {
  //      paintGraphics=g;
  //  }
  //  public Graphics getPaintGraphics()
  //  {
  //      return paintGraphics;
  //  }
  public abstract void drawTask(Graphics g);

  public void setGraphicsSettings(final Graphics g) {
    g.setColor(color);
    g.translate(-g.getTranslateX(), -g.getTranslateY());
    g.translate(translateX, translateY);
    g.setClip(clipX, clipY, clipWidth, clipHeight);
  }

}
