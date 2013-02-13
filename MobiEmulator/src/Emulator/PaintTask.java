/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract class PaintTask {
    public final static int DRAW_RECT      = 0;
    public final static int FILL_RECT      = DRAW_RECT + 1;
    public final static int DRAW_LINE      = FILL_RECT + 1;
    public final static int DRAW_ROUNDRECT = DRAW_LINE + 1;
    public final static int FILL_ROUNDRECT = DRAW_ROUNDRECT + 1;
    public final static int DRAW_ARC       = FILL_ROUNDRECT + 1;
    public final static int FILL_ARC       = DRAW_ARC + 1;
    public final static int DRAW_TRIANGLE  = FILL_ARC + 1;
    public final static int FILL_TRIANGLE  = DRAW_TRIANGLE + 1;
    public final static int DRAW_STRING    = FILL_TRIANGLE + 1;
    public final static int DRAW_IMAGE     = DRAW_STRING + 1;
    public final static int DRAW_RGB       = DRAW_IMAGE + 1;
    public final static int DRAW_REGION    = DRAW_RGB + 1;
    public final static int COPY_AREA      = DRAW_REGION + 1;
    public final static int GET_RGB        = COPY_AREA + 1;
    public int              clipHeight;
    public int              clipWidth;
    public int              clipX;
    public int              clipY;
    public int              color;
    private Graphics        paintGraphics;
    public int              translateX;
    public int              translateY;
    public PaintTask() {}

    public void getGraphicsSettings(Graphics g) {
        this.color      = g.getColor();
        this.translateX = g.getTranslateX();
        this.translateY = g.getTranslateY();
        this.clipX      = g.getClipX();
        this.clipY      = g.getClipY();
        this.clipWidth  = g.getClipWidth();
        this.clipHeight = g.getClipHeight();
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
    public void setGraphicsSettings(Graphics g) {
        g.setColor(color);
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        g.translate(translateX, translateY);
        g.setClip(clipX, clipY, clipWidth, clipHeight);
    }
}
