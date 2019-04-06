package Emulator;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class ImageTask extends PaintTask {

  public boolean showItemClip = true;
  int anchor;
  int at;
  int destx;
  int desty;
  int height;
  Image image;
  boolean isAlpha;
  int offset;
  int rgb[];
  int scanlength;
  private String stackTrace;
  int type;
  int width;
  int x;
  int y;

  public ImageTask(final Graphics g) {
    super.getGraphicsSettings(g);
  }

  public ImageTask paintImage(final Image image, final int x, final int y, int anchor) {
    this.x = x;
    this.y = y;
    width = image.getWidth();
    height = image.getHeight();
    this.anchor = anchor;
    if (anchor == 0) {
      anchor = Graphics.TOP | Graphics.LEFT;
    }
    if ((anchor & Graphics.HCENTER) != 0) {
      this.x -= (width >> 1);
    }
    if ((anchor & Graphics.VCENTER) != 0) {
      this.y -= (height >> 1);
    }
    if ((anchor & Graphics.BOTTOM) != 0) {
      this.y -= (height);
    }
    if ((anchor & Graphics.RIGHT) != 0) {
      this.x -= (height);
    }
    this.image = image;

    return this;
  }

  public ImageTask paintRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final boolean processAlpha) {
    rgb = new int[rgbData.length];
    System.arraycopy(rgbData, 0, rgb, 0, rgbData.length);
    this.offset = offset;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    isAlpha = processAlpha;
    this.scanlength = scanlength;

    return this;
  }

  public ImageTask paintRegion(final Image src, final int x_src, final int y_src, final int width, final int height, final int transform, final int x_dest, final int y_dest, final int anchor) {
    image = src;
    at = transform;
    paintArea(x_src, y_src, width, height, x_dest, y_dest, anchor);

    return this;
  }

  public ImageTask paintArea(final int x_src, final int y_src, final int width, final int height, final int x_dest, final int y_dest, int anchor) {
    x = x_src;
    y = y_src;
    this.width = width;
    this.height = height;
    destx = x_dest;
    desty = y_dest;
    this.anchor = anchor;
    if (anchor == 0) {
      anchor = Graphics.TOP | Graphics.LEFT;
    }
    if ((anchor & Graphics.HCENTER) != 0) {
      x -= (this.width >> 1);
    }
    if ((anchor & Graphics.VCENTER) != 0) {
      y -= (this.height >> 1);
    }
    if ((anchor & Graphics.BOTTOM) != 0) {
      y -= (this.height);
    }
    if ((anchor & Graphics.RIGHT) != 0) {
      x -= (this.height);
    }

    return this;
  }

  public ImageTask getRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    return this;
  }

  public ImageTask setType(final int type, final StackTraceElement[] dumpstack) {
    this.type = type;
    StringBuffer sb = new StringBuffer();
    for (final StackTraceElement aDumpstack : dumpstack) {
      sb.append(aDumpstack);
      sb.append("\n");
    }
    stackTrace = sb.toString();
    sb = null;

    return this;
  }

  public ImageTask setType(final int type, final String dumpstack) {
    this.type = type;
    stackTrace = dumpstack;

    return this;
  }

  public String getTypeString() {
    switch (type) {
      case PaintTask.DRAW_IMAGE:
        return "drawImage";
      case PaintTask.DRAW_REGION:
        return "drawRegion";
      case PaintTask.COPY_AREA:
        return "copyArea";
      case PaintTask.DRAW_RGB:
        return "drawRGB";
    }

    return "";
  }

  public String getStackTrace() {
    return stackTrace;
  }

  @Override
  public void drawTask(final Graphics g) {
    setGraphicsSettings(g);
    switch (type) {
      case PaintTask.DRAW_IMAGE:
        g.drawImage(image, x, y, anchor);
        break;
      case PaintTask.DRAW_REGION:
        g.drawRegion(image, x, y, width, height, at, destx, desty, anchor);
        break;
      case PaintTask.DRAW_RGB:
        g.drawRGB(rgb, offset, scanlength, x, y, width, height, isAlpha);
        break;
      case PaintTask.COPY_AREA:
        g.copyArea(x, y, width, height, destx, destx, anchor);
        break;
    }
  }

  public void showItemClip(final Graphics g, final int x, final int y, final int w, final int h) {
    if (showItemClip) {
      g.setColor(0X00FF00);
      g.drawRect(x - 1, y - 1, w + 2, h + 2);
    }
  }

  @Override
  public String toString() {
    return getTypeString();
  }

}
