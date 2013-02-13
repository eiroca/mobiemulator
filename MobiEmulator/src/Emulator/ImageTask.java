/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ImageTask extends PaintTask {
    public boolean showItemClip = true;
    int            anchor;
    int            at;
    int            destx;
    int            desty;
    int            height;
    Image          image;
    boolean        isAlpha;
    int            offset;
    int            rgb[];
    int            scanlength;
    private String stackTrace;
    int            type;
    int            width;
    int            x;
    int            y;
    public ImageTask(Graphics g) {
        super.getGraphicsSettings(g);
    }

    public ImageTask paintImage(Image image, int x, int y, int anchor) {
        this.x      = x;
        this.y      = y;
        this.width  = image.getWidth();
        this.height = image.getHeight();
        this.anchor = anchor;
        if (anchor == 0) {
            anchor = Graphics.TOP | Graphics.LEFT;
        }
        if ((anchor & Graphics.HCENTER) != 0) {
            this.x -= (this.width >> 1);
        }
        if ((anchor & Graphics.VCENTER) != 0) {
            this.y -= (this.height >> 1);
        }
        if ((anchor & Graphics.BOTTOM) != 0) {
            this.y -= (this.height);
        }
        if ((anchor & Graphics.RIGHT) != 0) {
            this.x -= (this.height);
        }
        this.image = image;

        return this;
    }
    public ImageTask paintRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height,
                              boolean processAlpha) {
        rgb = new int[rgbData.length];
        System.arraycopy(rgbData, 0, rgb, 0, rgbData.length);
        this.offset     = offset;
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
        this.isAlpha    = processAlpha;
        this.scanlength = scanlength;

        return this;
    }
    public ImageTask paintRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest,
                                 int y_dest, int anchor) {
        this.image = src;
        this.at    = transform;
        paintArea(x_src, y_src, width, height, x_dest, y_dest, anchor);

        return this;
    }
    public ImageTask paintArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {
        this.x      = x_src;
        this.y      = y_src;
        this.width  = width;
        this.height = height;
        this.destx  = x_dest;
        this.desty  = y_dest;
        this.anchor = anchor;
        if (anchor == 0) {
            anchor = Graphics.TOP | Graphics.LEFT;
        }
        if ((anchor & Graphics.HCENTER) != 0) {
            this.x -= (this.width >> 1);
        }
        if ((anchor & Graphics.VCENTER) != 0) {
            this.y -= (this.height >> 1);
        }
        if ((anchor & Graphics.BOTTOM) != 0) {
            this.y -= (this.height);
        }
        if ((anchor & Graphics.RIGHT) != 0) {
            this.x -= (this.height);
        }

        return this;
    }
    public ImageTask getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;

        return this;
    }
    public ImageTask setType(int type, StackTraceElement[] dumpstack) {
        this.type = type;
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement aDumpstack : dumpstack) {
            sb.append(aDumpstack);
            sb.append("\n");
        }
        this.stackTrace = sb.toString();
        sb              = null;

        return this;
    }
    public ImageTask setType(int type, String dumpstack) {
        this.type       = type;
        this.stackTrace = dumpstack;

        return this;
    }
    public String getTypeString() {
        switch (this.type) {
        case PaintTask.DRAW_IMAGE :
            return "drawImage";
        case PaintTask.DRAW_REGION :
            return "drawRegion";
        case PaintTask.COPY_AREA :
            return "copyArea";
        case PaintTask.DRAW_RGB :
            return "drawRGB";
        }

        return "";
    }
    public String getStackTrace() {
        return stackTrace;
    }
    @Override
    public void drawTask(Graphics g) {
        setGraphicsSettings(g);
        switch (type) {
        case PaintTask.DRAW_IMAGE :
            g.drawImage(this.image, x, y, anchor);

            break;
        case PaintTask.DRAW_REGION :
            g.drawRegion(image, x, y, width, height, at, destx, desty, anchor);

            break;
        case PaintTask.DRAW_RGB :
            g.drawRGB(rgb, offset, scanlength, x, y, width, height, isAlpha);

            break;
        case PaintTask.COPY_AREA :
            g.copyArea(x, y, width, height, destx, destx, anchor);

            break;
        }
    }
    public void showItemClip(Graphics g, int x, int y, int w, int h) {
        if (showItemClip) {
            g.setColor(0X00FF00);
            g.drawRect(x - 1, y - 1, w + 2, h + 2);
        }
    }
    public String toString() {
        return getTypeString();
    }
}
