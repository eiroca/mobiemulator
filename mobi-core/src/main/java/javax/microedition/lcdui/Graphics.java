
package javax.microedition.lcdui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
// ~--- non-JDK imports --------------------------------------------------------
import javax.microedition.midlet.MidletListener;
import javax.microedition.midlet.MidletUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */

public class Graphics {

  public static final int BASELINE = 64;
  public static final int BOTTOM = 32;
  public static final int DOTTED = 1;
  public static final int HCENTER = 1;
  public static final int LEFT = 4;
  public static final int RIGHT = 8;
  public static final int SOLID = 0;
  public static final int TOP = 16;
  public static final int VCENTER = 2;
  public static boolean XRayView = true;
  final static float dash1[] = {
      10.0f
  };
  final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
      Graphics.dash1, 0.0f);

  public java.awt.Graphics2D XRayGraphics;

  private final int DrawImageColor = 0x0FF0F0F0; // 0xCDCECA;
  private final int DrawRGBColor = 0x0FDF1911;
  private final int DrawRectColor = 0X08D3D9C4;
  private final int DrawRegionColor = 0xF80000FF;
  private final int DrawStringColor = 0X08E3DDE3;
  private final int FillRectColor = 0X0F919B87;
  private final Color xRayImageColor = new java.awt.Color(220, 250, 0, 20);
  /**
   * Rectangle for getting the AWT clip information into
   */
  java.awt.Rectangle _clip = new Rectangle(0, 0, 1000, 1000);

  /**
   * MIDP font associated with this graphics context
   */
  public Font font = Font.getDefaultFont();
  private final int setClipColor = 0x0F7BEB0F;
  public Font f = javax.microedition.lcdui.Font.getDefaultFont();
  public int font_Height = f.getHeight();
  private final java.awt.Graphics2D _graphics2D;
  private final BufferedImage graphicsImage;
  private BufferedImage stackImage;
  private Graphics2D stackImageGraphics;
  private int stroke, translate_x, translate_y;
  private final MidletListener midletListener;

  public Graphics(final BufferedImage midp_screen_image) {
    graphicsImage = midp_screen_image;
    _graphics2D = midp_screen_image.createGraphics();
    midletListener = MidletUtils.getInstance().getMidletListener();
    XRayGraphics = midletListener.getXrayScreen().createGraphics();
    XRayGraphics.setColor(new Color(0, 0, 0, 0));
    XRayGraphics.fillRect(0, 0, midletListener.getXrayScreen().getWidth(),
        midletListener.getXrayScreen().getHeight());
    translate(-getTranslateX(), -getTranslateY());
    setClip(0, 0, midletListener.getCanvasWidth(), midletListener.getCanvasHeight());
    setColor(0xffffffff); // black
    setFont(Font.getDefaultFont());
    setStrokeStyle(Graphics.SOLID);
  }

  public Graphics(final java.awt.Graphics2D g, final BufferedImage image) {
    graphicsImage = image;
    _graphics2D = g;
    midletListener = MidletUtils.getInstance().getMidletListener();
    translate(-getTranslateX(), -getTranslateY());
    XRayGraphics = midletListener.getXrayScreen().createGraphics();
    XRayGraphics.setColor(new Color(0, 0, 0, 0));
    XRayGraphics.fillRect(0, 0, midletListener.getXrayScreen().getWidth(),
        midletListener.getXrayScreen().getHeight());
    setColor(0xff000000); // black
    setFont(Font.getDefaultFont());
    setStrokeStyle(Graphics.SOLID);
  }

  public BufferedImage getGraphicsImage() {
    return graphicsImage;
  }

  public java.awt.Graphics2D getGraphics() {
    // GraphicsImage=javax.microedition.lcdui.Image.createImage(Main.canvas_width,
    // Main.canvas_height);
    return _graphics2D;
  }

  public static void setXrayGraphics(final java.awt.Graphics2D XRay_Graphics) {
    // XRayGraphics = xrayGraphics;
  }

  public void copyArea(final int x_src, final int y_src, final int width, final int height, final int x_dest, final int y_dest, final int anchor) {

    midletListener.performancePaintArea(this, x_src, y_src, width, height, x_dest, y_dest, anchor, "copyarea", stackTrace());
    final AffineTransform localAffineTransform = getAffineTransform(width, height, 0, x_dest, y_dest, anchor);
    drawRegion(graphicsImage, x_src, y_src, width, height, localAffineTransform, x_dest, y_dest, anchor);
  }

  public void drawChar(final char character, final int x, final int y, final int anchor) {
    drawString("" + character, x, y, anchor);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawStringColor));
      final int w = f.substringWidth("" + character, 0, 1);
      XRayGraphics.fillRect(x, y - (font_Height >> 1), w, font_Height);
    }
  }

  public void drawChars(final char[] data, final int offset, final int length, final int x, final int y, final int anchor) {
    drawString(new String(data, offset, length), x, y, anchor);
  }

  public void drawSubstring(final String str, final int offset, final int len, final int x, final int y, final int anchor) {
    drawString(str.substring(offset, offset + len), x, y, anchor);
  }

  public void drawString(final String str, int x, int y, int anchor) {

    midletListener.performancePaintString(this, getFont(), str, x, y, anchor, "drawstring", stackTrace());
    // default anchor
    if (anchor == 0) {
      anchor = Graphics.TOP | Graphics.LEFT;
    }
    switch (anchor & (Graphics.TOP | Graphics.BOTTOM | Graphics.BASELINE | Graphics.VCENTER)) {
      case TOP:
        y += font._fm.getAscent();

        break;
      case BOTTOM:
        y -= font._fm.getDescent();

        break;
      case VCENTER:
        throw new IllegalArgumentException("Illegal VCENTER anchor in drawString");
    }
    switch (anchor & (Graphics.LEFT | Graphics.RIGHT | Graphics.HCENTER)) {
      case RIGHT:
        x -= font.stringWidth(str);

        break;
      case HCENTER:
        x -= (font.stringWidth(str) >> 1);

        break;
    }
    _graphics2D.drawString(str, x, y);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new Color(DrawStringColor));
      final int w = f.substringWidth(str, 0, str.length());
      XRayGraphics.fillRect(x, y - (font_Height >> 1), w, font_Height);
    }
  }

  public void drawRGB(final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height, final boolean processAlpha) {

    midletListener.performancePaintRGB(this, rgbData, offset, scanlength, x, y, width, height, processAlpha, "drawrgb", stackTrace());
    getAffineTransform(width, height, 0, x, y, 0);
    MemoryImageSource mis = new MemoryImageSource(width, height, rgbData, offset, scanlength);
    java.awt.Image img = Toolkit.getDefaultToolkit().createImage(mis);
    mis = null;
    _graphics2D.drawImage(img, x, y, null);
    midletListener.incrDrawRGBCount(width * height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRGBColor));
      XRayGraphics.fillRect(x, y, width, height);
    }

    img = null;
  }

  public void drawImage(final Image img, int x, int y, int anchor) {
    midletListener.performancePaintImage(this, img, x, y, anchor, "drawimage", stackTrace());
    // default anchor
    final int height = img.getHeight();
    final int width = img.getWidth();
    if (anchor == 0) {
      anchor = Graphics.TOP | Graphics.LEFT;
    }
    switch (anchor & (Graphics.TOP | Graphics.BOTTOM | Graphics.BASELINE | Graphics.VCENTER)) {
      case BASELINE:
      case BOTTOM:
        y -= height;

        break;
      case VCENTER:
        y -= height >> 1;

        break;
      case TOP:
      default:
        break;
    }
    switch (anchor & (Graphics.LEFT | Graphics.RIGHT | Graphics.HCENTER)) {
      case RIGHT:
        x -= width;

        break;
      case HCENTER:
        x -= width >> 1;

        break;
      case LEFT:
      default:
        break;
    }
    // / verifyRectInsideImage(x, y, img._image.getWidth(),
    // img._image.getHeight());
    img.drawCount += 1;
    _graphics2D.drawImage(img._image, x, y, null);
    // Rectangle localRectangle = getEffectiveRect(x, y,
    // img._image.getWidth(), img._image.getHeight());
    midletListener.incrDrawRGBCount(width * height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(xRayImageColor);
      XRayGraphics.fillRect(x, y, img._image.getWidth(null), img._image.getHeight(null));
    }
  }

  public void fillRect(final int x, final int y, final int width, final int height) {
    midletListener.performancePaintRect(this, x, y, width, height, "fillrect", stackTrace());
    _graphics2D.fillRect(x, y, width, height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(FillRectColor));
      XRayGraphics.fillRect(x, y, width, height);
    }
  }

  public void drawRect(final int x, final int y, final int width, final int height) {

    midletListener.performancePaintRect(this, x, y, width, height, "drawrect", stackTrace());
    _graphics2D.drawRect(x, y, width, height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRectColor));
      XRayGraphics.fillRect(x, y, width, height);
    }
  }

  public void drawLine(final int x1, final int y1, final int x2, final int y2) {
    midletListener.performancePaintLine(this, x1, y1, x2, y2, "drawline", stackTrace());
    _graphics2D.drawLine(x1, y1, x2, y2);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRectColor));
      XRayGraphics.fillRect(x1, y1, x2, y2);
    }
  }

  public void drawRoundRect(final int x, final int y, final int w, final int h, final int r1, final int r2) {

    midletListener.performanceRoundRect(this, x, y, w, h, r1, r2, "drawroundrect", stackTrace());
    _graphics2D.drawRoundRect(x, y, w, h, r1, r2);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRectColor));
      XRayGraphics.drawRoundRect(x, y, w, h, r1, r2);
    }
  }

  public void fillArc(final int x, final int y, final int w, final int h, final int sa, final int aa) {
    midletListener.performancePaintArc(this, x, y, w, h, sa, aa, "fillarc", stackTrace());
    _graphics2D.fillArc(x, y, w, h, sa, aa);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(FillRectColor));
      XRayGraphics.fillArc(x, y, w, h, sa, aa);
    }
  }

  public void drawArc(final int x, final int y, final int w, final int h, final int sa, final int aa) {
    midletListener.performancePaintArc(this, x, y, w, h, sa, aa, "drawarc", stackTrace());
    _graphics2D.drawArc(x, y, w, h, sa, aa);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRectColor));
      XRayGraphics.drawArc(x, y, w, h, sa, aa);
    }
  }

  public void drawTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3, final int argbColor) {
    setColor(argbColor);
    midletListener.performancePaintTriangle(this, x1, y1, x2, y2, x3, y3, "drawtriangle", stackTrace());
    _graphics2D.setColor(new Color(argbColor));
    _graphics2D.drawPolygon(new int[] {
        x1, x2, x3
    }, new int[] {
        y1, y2, y3
    }, 3);
  }

  public void fillRoundRect(final int x, final int y, final int w, final int h, final int r1, final int r2) {
    midletListener.performanceRoundRect(this, x, y, w, h, r1, r2, "fillroundrect", stackTrace());
    _graphics2D.fillRoundRect(x, y, w, h, r1, r2);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(DrawRectColor));
      XRayGraphics.fillRoundRect(x, y, w, h, r1, r2);
    }
  }

  public void drawPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    _graphics2D.setColor(new Color(argbColor));
    final int xpoints[] = new int[xPoints.length];
    for (int i = 0; i < xPoints.length; i++) {
      xpoints[i] = xPoints[i] + xOffset;
    }
    final int ypoints[] = new int[yPoints.length];
    for (int i = 0; i < yPoints.length; i++) {
      ypoints[i] = yPoints[i] + yOffset;
    }
    _graphics2D.drawPolygon(xpoints, ypoints, nPoints);
  }

  public void fillPolygon(final int[] xPoints, final int xOffset, final int[] yPoints, final int yOffset, final int nPoints, final int argbColor) {
    _graphics2D.setColor(new Color(argbColor));
    final int xpoints[] = new int[xPoints.length];
    for (int i = 0; i < xPoints.length; i++) {
      xpoints[i] = xPoints[i] + xOffset;
    }
    final int ypoints[] = new int[yPoints.length];
    for (int i = 0; i < yPoints.length; i++) {
      ypoints[i] = yPoints[i] + yOffset;
    }
    _graphics2D.fillPolygon(xpoints, ypoints, nPoints);
  }

  public void fillTriangle(final int x1, final int y1, final int x2, final int y2, final int x3, final int y3) {
    midletListener.performancePaintTriangle(this, x1, y1, x2, y2, x3, y3, "filltriangle", stackTrace());
    _graphics2D.fillPolygon(new int[] {
        x1, x2, x3
    }, new int[] {
        y1, y2, y3
    }, 3);
  }

  public void setColor(final int RGB) {
    _graphics2D.setColor(new java.awt.Color(RGB));
  }

  public void setColor(final int red, final int green, final int blue) {
    _graphics2D.setColor(new java.awt.Color(red, green, blue));
  }

  public void setClip(final int x, final int y, final int width, final int height) {
    _graphics2D.setClip(x, y, width, height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(setClipColor));
      XRayGraphics.drawRect(x, y, width, height);
    }
  }

  public void clipRect(final int x, final int y, final int width, final int height) {
    _graphics2D.clipRect(x, y, width, height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(setClipColor));
      XRayGraphics.drawRect(x, y, width, height);
    }
  }

  public int getColor() {
    return _graphics2D.getColor().getRGB();
  }

  public int getDisplayColor(final int Color) {
    return Color;
  }

  public int getRedComponent() {
    return _graphics2D.getColor().getRed();
  }

  public int getBlueComponent() {
    return _graphics2D.getColor().getBlue();
  }

  public int getGreenComponent() {
    return _graphics2D.getColor().getGreen();
  }

  public int getGrayScale() {
    final int graycolor = (getRedComponent() + getGreenComponent() + getBlueComponent()) / 3;

    return graycolor;
  }

  public void setGrayScale(final int value) {
    if ((value & 0XFF00) > 0) {
      new IllegalArgumentException();
    }
    _graphics2D.setColor(new Color(value, value, value));
  }

  public void setFont(final Font font) {
    this.font = font;
    if (this.font == null) {
      this.font = Font.getDefaultFont();
    }
    _graphics2D.setFont(this.font._font);
  }

  public Font getFont() {
    return font;
  }

  public int getClipX() {
    _graphics2D.getClipBounds(_clip);

    return _clip.x;
  }

  public int getClipY() {
    _graphics2D.getClipBounds(_clip);

    return _clip.y;
  }

  public int getClipWidth() {
    _graphics2D.getClipBounds(_clip);

    return _clip.width;
  }

  public int getClipHeight() {
    _graphics2D.getClipBounds(_clip);

    return _clip.height;
  }

  /**
   * translate needs to remember the current translation, since AWT doesn't provide getTranslate
   * methods.
   */
  public void translate(final int x, final int y) {
    _graphics2D.translate(-translate_x, -translate_y);
    translate_x += x;
    translate_y += y;
    _graphics2D.translate(translate_x, translate_y);
  }

  public int getTranslateX() {
    return translate_x;
  }

  public int getTranslateY() {
    return translate_y;
  }

  /**
   * Stroke style for line drawing isn't something AWT supports, but it's only a small visual thing
   * that won't hurt us to ignore!
   */
  public void setStrokeStyle(final int style) {
    stroke = style;
    if (style == Graphics.DOTTED) {
      _graphics2D.setStroke(Graphics.dashed);
    }
    if (style == Graphics.SOLID) {
      _graphics2D.setStroke(new BasicStroke());
    }
  }

  public int getStrokeStyle() {
    return stroke;
  }

  // temp
  private int getAnchorX(final int width, final int height, final int transform, final int anchor) {
    if (transform <= 3) {
      if ((anchor & 0x1) != 0) { return -(width >> 1); }
      if ((anchor & 0x8) != 0) { return -width; }
    }
    else {
      if ((anchor & 0x1) != 0) { return -(height >> 1); }
      if ((anchor & 0x8) != 0) { return -height; }
    }

    return 0;
  }

  private int getAnchorY(final int width, final int height, final int transform, final int anchor) {
    if (transform <= 3) {
      if ((anchor & 0x2) != 0) { return -(height >> 1); }
      if ((anchor & 0x20) != 0) { return -height; }
    }
    else {
      if ((anchor & 0x2) != 0) { return -(width >> 1); }
      if ((anchor & 0x20) != 0) { return -width; }
    }

    return 0;
  }

  public AffineTransform getAffineTransform(final int width, final int height, final int transform, int x_dest, int y_dest, final int anchor) {
    x_dest += getAnchorX(width, height, transform, anchor);
    y_dest += getAnchorY(width, height, transform, anchor);
    final AffineTransform at = new AffineTransform();
    // at.setToIdentity();
    at.translate(x_dest, y_dest);
    if (transform == 6) {
      at.translate(0.0D, width);
      at.rotate(-Math.PI / 2);
    }
    else if (transform == 4) {
      at.rotate(-Math.PI / 2);
      at.scale(-1.0D, 1.0D);
    }
    else if (transform == 5) {
      at.translate(height, 0.0D);
      at.rotate(Math.PI / 2);
    }
    else if (transform == 7) {
      at.translate(height, width);
      at.rotate(Math.PI / 2);
      at.scale(-1.0D, 1.0D);
    }
    else if (transform == 3) {
      at.translate(width, height);
      at.rotate(Math.PI);
    }
    else if (transform == 2) {
      at.translate(width, 0.0D);
      at.scale(-1.0D, 1.0D);
    }
    else if (transform == 1) {
      at.translate(0.0D, height);
      at.scale(1.0D, -1.0D);
    }

    return at;
  }

  public void drawRegion(final Image src, final int x_src, final int y_src, final int width, final int height, final int transform, final int x_dest, final int y_dest, final int anchor)
      throws IllegalArgumentException {

    midletListener.performancePaintRegion(this, src, x_src, y_src, width,
        height, transform, x_dest, y_dest, anchor, "drawregion", stackTrace());
    AffineTransform at = getAffineTransform(width, height, transform, x_dest, y_dest, anchor);
    AffineTransform beforeAt = _graphics2D.getTransform();
    _graphics2D.transform(at);
    // verifyRectInsideImage(0, 0, width, height);
    _graphics2D.drawImage(src._image, 0, 0, width, height, x_src, y_src, x_src + width, y_src + height, null);
    _graphics2D.setTransform(beforeAt);
    midletListener.incrDrawRGBCount(width * height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(200, 0, 0, 250));
      XRayGraphics.fillRect(x_dest, y_dest, width, height);
    }
    beforeAt = null;
    at = null;
  }

  public void drawRegion(final BufferedImage src, final int x_src, final int y_src, final int width, final int height, final AffineTransform transform, final int x_dest, final int y_dest, final int anchor)
      throws IllegalArgumentException {

    AffineTransform beforeAt = _graphics2D.getTransform();
    _graphics2D.transform(transform);
    // verifyRectInsideImage(0, 0, width, height);
    _graphics2D.drawImage(src, 0, 0, width, height, x_src, y_src, x_src + width, y_src + height, null);
    _graphics2D.setTransform(beforeAt);
    midletListener.incrDrawRGBCount(width * height);
    if (Graphics.XRayView && (XRayGraphics != null)) {
      XRayGraphics.setColor(new java.awt.Color(200, 0, 0, 250));
      XRayGraphics.fillRect(x_dest, y_dest, width, height);
    }
    beforeAt = null;
  }

  public static String getStackTrace(final Throwable aThrowable) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    aThrowable.printStackTrace(printWriter);
    printWriter = null;
    final String str = result.toString();
    result = null;

    return str;
  }

  private String stackTrace() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    Throwable t = new Throwable("");
    t.printStackTrace(pw);
    String s = sw.toString();
    final int index = s.indexOf("at");
    s = s.substring(s.indexOf("at", index + 4));
    sw = null;
    pw = null;
    t = null;

    return s;
  }

}
