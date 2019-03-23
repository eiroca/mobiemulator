
package javax.microedition.m3g;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
/**
 *
 * @author Ashok Kumar Gujarathi
 */
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MidletUtils;

class Helpers {

  private static int[] pixels = null;
  private static int w;
  private static int h;

  static InputStream openInputStream(final String paramString)
      throws IOException {
    try {
      if (paramString.charAt(0) == '/') { return MidletUtils.getInstance().getMidletListener().getResourceStream(paramString); }
      InputConnection localInputConnection;
      String str;
      if ((((localInputConnection = (InputConnection)Connector.open(paramString)) instanceof HttpConnection)) && ((str = ((HttpConnection)localInputConnection).getHeaderField("Content-Type")) != null) && (!str.equals("application/m3g")) && (!str.equals("image/png"))) {
        throw new IOException("Wrong MIME type: " + str);
      }
      return localInputConnection.openInputStream();
    }
    catch (final MalformedURLException localMalformedURLException) {
    }
    throw new IOException("Malformed URL: \"" + paramString + "\"");
  }

  static int getTranslateX(final Graphics paramGraphics) {
    return paramGraphics.getTranslateX();
  }

  static int getTranslateY(final Graphics paramGraphics) {
    return paramGraphics.getTranslateY();
  }

  static int getClipX(final Graphics paramGraphics) {
    return paramGraphics.getClipX();
  }

  static int getClipY(final Graphics paramGraphics) {
    return paramGraphics.getClipY();
  }

  static int getClipWidth(final Graphics paramGraphics) {
    return paramGraphics.getClipWidth();
  }

  static int getClipHeight(final Graphics paramGraphics) {
    return paramGraphics.getClipHeight();
  }

  static void bindTarget(final Graphics3D paramGraphics3D, final Graphics paramGraphics, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4) {
    Helpers.w = paramGraphics.getGraphicsImage().getWidth();
    Helpers.h = paramGraphics.getGraphicsImage().getHeight();
    Helpers.pixels = paramGraphics.getGraphicsImage().getRaster().getPixels(0, 0, Helpers.w, Helpers.h, Helpers.pixels);
    Helpers.bindTarget(paramGraphics3D, Helpers.w, Helpers.h, Helpers.pixels, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  static void releaseTarget(final Graphics3D paramGraphics3D, final Graphics paramGraphics) {
    paramGraphics.getGraphicsImage().getRaster().setPixels(0, 0, Helpers.w, Helpers.h, Helpers.pixels);
    Helpers.pixels = null;
  }

  private static native void bindTarget(Graphics3D paramGraphics3D, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

  static Image createImage(final byte[] paramArrayOfByte, final int paramInt1, final int paramInt2) throws IOException {
    throw new IOException("Corrupt PNG or unsupported image type");
  }

  static Image createImage(final String paramString) throws IOException {
    throw new IOException("Corrupt PNG or unsupported image type");
  }

  static int getImageWidth(final Image paramImage) {
    return paramImage.getWidth();
  }

  static int getImageHeight(final Image paramImage) {
    return paramImage.getHeight();
  }

  static boolean isOpaque(final Image paramImage) {
    return false;
  }

  static void getImageRGB(final Image paramImage, final int[] paramArrayOfInt, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final int paramInt5, final int paramInt6) {
    paramImage.getRGB(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
  }

}
