/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.microedition.m3g;

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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

class Helpers
{
  private static int[] pixels = null;
    private static int w;
private static int h;
  static InputStream openInputStream(String paramString)
    throws IOException
  {
    try
    {
      if (paramString.charAt(0) == '/')
        return MidletUtils.getInstance().getMidletListener().getResourceStream(paramString);
      InputConnection localInputConnection;
      HttpConnection localHttpConnection;
      String str;
      if ((((localInputConnection = (InputConnection)Connector.open(paramString)) instanceof HttpConnection)) && ((str = (localHttpConnection = (HttpConnection)localInputConnection).getHeaderField("Content-Type")) != null) && (!str.equals("application/m3g")) && (!str.equals("image/png")))
        throw new IOException("Wrong MIME type: " + str);
      return localInputConnection.openInputStream();
    }
    catch (MalformedURLException localMalformedURLException)
    {
    }
    throw new IOException("Malformed URL: \"" + paramString + "\"");
  }

  static int getTranslateX(Graphics paramGraphics)
  {
    return paramGraphics.getTranslateX();
  }

  static int getTranslateY(Graphics paramGraphics)
  {
    return paramGraphics.getTranslateY();
  }

  static int getClipX(Graphics paramGraphics)
  {
    return paramGraphics.getClipX();
  }

  static int getClipY(Graphics paramGraphics)
  {
    return paramGraphics.getClipY();
  }

  static int getClipWidth(Graphics paramGraphics)
  {
    return paramGraphics.getClipWidth();
  }

  static int getClipHeight(Graphics paramGraphics)
  {
    return paramGraphics.getClipHeight();
  }

  static void bindTarget(Graphics3D paramGraphics3D, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    w = paramGraphics.getGraphicsImage().getWidth();
    h = paramGraphics.getGraphicsImage().getHeight();
    pixels = paramGraphics.getGraphicsImage().getRaster().getPixels(0, 0, w, h, pixels);
    bindTarget(paramGraphics3D, w, h, pixels, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  static void releaseTarget(Graphics3D paramGraphics3D, Graphics paramGraphics)
  {
    paramGraphics.getGraphicsImage().getRaster().setPixels(0, 0, w, h, pixels);
    pixels = null;
  }

  private static native void bindTarget(Graphics3D paramGraphics3D, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

  static Image createImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    throw new IOException("Corrupt PNG or unsupported image type");
  }

  static Image createImage(String paramString)
    throws IOException
  {
    throw new IOException("Corrupt PNG or unsupported image type");
  }

  static int getImageWidth(Image paramImage)
  {
    return paramImage.getWidth();
  }

  static int getImageHeight(Image paramImage)
  {
    return paramImage.getHeight();
  }

  static boolean isOpaque(Image paramImage)
  {
    return false;
  }

  static void getImageRGB(Image paramImage, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    paramImage.getRGB(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
  }
}

