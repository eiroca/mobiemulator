package javax.microedition.m3g;


import javax.microedition.io.Connector;
import javax.microedition.lcdui.Image;
import java.io.IOException;
import java.io.InputStream;

public class Loader
{
  int swerveHandle;
  static final int ERROR_NONE = -1;
  static final int ERROR_ABORT = -2;
  static final int ERROR_NOTFOUND = -3;
  static final int ERROR_READ = -4;
  static final int ERROR_UNABLETOCREATE = -5;
  static final int ERROR_UNABLETOFREE = -6;
  static final int ERROR_INVALIDHEADER = -7;
  static final int ERROR_INVALIDBODY = -8;
  static final int ERROR_INVALIDCHECKSUM = -9;
  static final int ERROR_UNKNOWN = -10;
  static final int ERROR_DATAPASTEOF = -11;
  static byte[] pngIdentifier;
  static int m3gIdentifierLength;
  static int BLOCK_SIZE;



  Loader()
  {
  }

  Loader(int handle)
  {
    this.swerveHandle = handle;
  }

  private static native int createImpl();

  native boolean getbContainsExtensions();

  native int onDataStart(boolean paramBoolean);

  native int onDataEnd();

  native int onData(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  native void onError(int paramInt);

  native int getRootCount();

  private native int getRootImpl(int paramInt);

  native int getXREFName(byte[] paramArrayOfByte);

  native void resolveXREF(Object3D paramObject3D);

  public static Object3D[] load(byte[] data, int offset)
    throws IOException
  {
    if ((offset < 0) || (offset >= data.length)) {
      throw new IndexOutOfBoundsException();
    }

    return loadHelper(data, offset, true);
  }

  private static Object3D[] loadHelper(byte[] data, int offset, boolean bPermitExtensions) throws IOException
  {
    try
    {
      Loader loader = (Loader)Engine.instantiateJavaPeer(createImpl());

      int offsetSave = offset;
      int error;
      try {
        error = loader.onDataStart(bPermitExtensions);

        int length = data.length - offset;

        if ((error == -1) && (length > m3gIdentifierLength))
        {
          error = loader.onData(data, offset, m3gIdentifierLength);
          length -= m3gIdentifierLength;
          offset += m3gIdentifierLength;
        }

        if (error == -1)
        {
          error = loader.onData(data, offset, length);
        }

        if (error >= 0)
        {
          loader.processXREFs(null);

          offset += error;
          length -= error;

          error = loader.onData(data, offset, length);
        }

        if (error == -11) {
          error = -1;
        }

        if (error == -1)
        {
          error = loader.onDataEnd();
        }

      }
      catch (SecurityException e)
      {
        loader.onError(-10);

        if (e.getMessage() != null) {
          throw new SecurityException(e.getMessage());
        }
        throw new SecurityException();
      }
      catch (Exception e)
      {
        loader.onError(-10);

        if (e.getMessage() != null) {
          throw new IOException(e.getMessage());
        }
        throw new IOException();
      }

      if (error == -1)
      {
        Object3D[] roots = loader.createRoots();

        return roots;
      }

      Image2D img = createPNGImage2D(data, offsetSave, data.length);

      if (img == null)
      {
        img = createImage2D(data, offsetSave, data.length);
      }

      if (img != null)
      {
        Object3D[] roots = { img };

        return roots;
      }

      return null;
    }
    catch (SecurityException e)
    {
      if (e.getMessage() != null) {
        throw new SecurityException(e.getMessage());
      }
      throw new SecurityException();
    }
    catch (Exception e)
    {
      if (e.getMessage() != null)
        throw new IOException(e.getMessage());
    }
    throw new IOException();
  }

  public static Object3D[] load(String name)
    throws IOException
  {
    if (name == null) {
      throw new NullPointerException();
    }

    return loadHelper(name, true);
  }

  private static Object3D[] loadHelper(String name, boolean bPermitExtensions)
    throws IOException
  {
    InputStream is = null;
    try
    {
      Loader loader = (Loader)Engine.instantiateJavaPeer(createImpl());
      int error;
      int length;
      try
      {
        is = loader.getClass().getResourceAsStream(name);

        if (is == null)
        {
          is = openInputStream(name);
        }

        error = loader.onDataStart(bPermitExtensions);

        byte[] data = new byte[BLOCK_SIZE];
        
        if ((error == -1) && ((length = is.read(data, 0, m3gIdentifierLength)) != -1))
        {
          error = loader.onData(data, 0, length);
        }

        while ((error == -1) && ((length = is.read(data, 0, BLOCK_SIZE)) != -1))
        {
          error = loader.onData(data, 0, length);

          if (error < 0) {
            continue;
          }
          loader.processXREFs(name);

          error = loader.onData(data, error, length - error);
        }

        if (error == -1)
        {
          error = loader.onDataEnd();
        }

      }
      catch (SecurityException e)
      {
        loader.onError(-10);

        if (e.getMessage() != null) {
          throw new SecurityException(e.getMessage());
        }
        throw new SecurityException();
      }
      catch (Exception e)
      {
        loader.onError(-10);

        if (e.getMessage() != null) {
          throw new IOException(e.getMessage());
        }
        throw new IOException();
      }

      if (error == -1)
      {
        Object3D[] roots = loader.createRoots();
        return roots;
      }

      if (is != null)
      {
        is.close();
        is = null;
      }

      Image2D img = createPNGImage2D(name);

      if (img == null)
      {
        img = createImage2D(name);
      }

      if (img != null)
      {
        Object3D[] roots = { img };

        Object3D[] arrayOfObject3D1 = roots;
        return arrayOfObject3D1;
      }

      Object3D[] roots = null;
     
      return roots;
    }
    catch (SecurityException e)
    {
      if (e.getMessage() != null) {
        throw new SecurityException(e.getMessage());
      }
      throw new SecurityException();
    }
    catch (Exception e)
    {
      if (e.getMessage() != null) {
        throw new IOException(e.getMessage());
      }
      throw new IOException();
    }
    finally
    {
      if (is != null)
        is.close();
    }
  }

  private void processXREFs(String name) throws IOException
  {
    byte[] xrefNameUTF8 = null;
    int len;
    do {
      len = getXREFName(null);

      if (len == -1)
        continue;
      if ((xrefNameUTF8 == null) || (xrefNameUTF8.length != len))
      {
        xrefNameUTF8 = new byte[len];
      }

      getXREFName(xrefNameUTF8);

      String xrefName = new String(xrefNameUTF8, "UTF-8");

      if (isRelative(xrefName))
      {
        if (name == null)
        {
          throw new IOException("XREF resource [" + xrefName + "] has relative URI.");
        }

        xrefName = resolveURI(name, xrefName);
      }

      Object3D[] xrefs = loadHelper(xrefName, getbContainsExtensions());

      if (xrefs == null)
      {
        throw new IOException("XREF resource [" + xrefName + "] contained no roots.");
      }

      resolveXREF(xrefs[0]);
    }

    while (len != -1);
  }

  private static boolean isRelative(String URI)
  {
    int len = URI.length();

    if ((len == 0) || (URI.startsWith("//"))) {
      return true;
    }
    if (URI.startsWith("/")) {
      return false;
    }

    int i = 0;
    char c;
    do {
      c = URI.charAt(i);

      if ((c == ':') || (c == '/') || (c == '?') || (c == '#')) break; i++; } while (i != len);

    return c != ':';
  }

  private static String resolveURI(String baseURI, String relativeURI)
  {
    int baseURILength = baseURI.length();
    int relativeURILength = relativeURI.length();

    while ((baseURILength > 0) && (baseURI.charAt(baseURILength - 1) != '/')) {
      baseURILength--;
    }
    if (baseURILength > 0) {
      return baseURI.substring(0, baseURILength) + relativeURI;
    }
    return '/' + relativeURI;
  }

  private Object3D[] createRoots()
  {
    int rootCount = getRootCount();

    if (rootCount == 0) {
      return null;
    }
    Object3D[] roots = new Object3D[rootCount];

    while (rootCount-- > 0) {
      roots[rootCount] = ((Object3D)Engine.instantiateJavaPeer(getRootImpl(rootCount)));
    }
    return roots;
  }

  static InputStream openInputStream(String name)
    throws IOException
  {
    return Connector.openInputStream(name);
  }

  private static boolean hasPNGIdentifier(byte[] data, int offset, int length)
  {
    if (pngIdentifier.length > length) {
      return false;
    }
    for (int i = 0; i < pngIdentifier.length; i++) {
      if (pngIdentifier[i] != data[(offset + i)])
        return false;
    }
    return true;
  }

  static Image2D createPNGImage2D(String name)
    throws IOException
  {
    InputStream is = null;
    try
    {
      is = Loader.class.getResourceAsStream(name);

      if (is == null) {
        is = openInputStream(name);
      }
      byte[] data = new byte[BLOCK_SIZE];
      int length;
      byte tmp[];
      if (((length = is.read(data, 0, pngIdentifier.length)) != -1) &&
        (!hasPNGIdentifier(data, 0, length))) {
        return null;
      }
      byte[] pngdata = new byte[length];
      System.arraycopy(data, 0, pngdata, 0, length);

      while ((length = is.read(data, 0, BLOCK_SIZE)) != -1)
      {
        tmp = new byte[pngdata.length + length];
        System.arraycopy(pngdata, 0, tmp, 0, pngdata.length);
        System.arraycopy(data, 0, tmp, pngdata.length, length);
        pngdata = tmp;
      }

     return new Image2D(pngdata, 0, pngdata.length);
      
    }
    finally
    {
      if (is != null)
        is.close();
    }
    
  }

  static Image2D createPNGImage2D(byte[] imageData, int imageOffset, int imageLength)
  {
    if (!hasPNGIdentifier(imageData, imageOffset, imageLength)) {
      return null;
    }
    return new Image2D(imageData, imageOffset, imageLength);
  }

  static Image2D createImage2D(String name)
    throws IOException
  {
    Image img = null;
    try
    {
      img = Image.createImage(name);
    }
    catch (IOException io)
    {
      InputStream is = openInputStream(name);
      try
      {
        img = Image.createImage(is);
      }
      finally
      {
        if (is != null) {
          is.close();
        }
      }
    }
    return img != null ? createImage2D(img) : null;
  }

  static Image2D createImage2D(byte[] imageData, int imageOffset, int imageLength)
  {
    Image img = Image.createImage(imageData, imageOffset, imageLength);

    return img != null ? createImage2D(img) : null;
  }

  private static Image2D createImage2D(Image image)
  {
    int imageType = 100;//ImageType.getImageType(image);
    int image2DType;
    switch (imageType)
    {
    case 97:
    case 98:
    case 99:
    case 100:
      image2DType = imageType;
      break;
    default:
      image2DType = 100;
    }

    return new Image2D(image2DType, image);
  }

  static
  {
      try
	{
	    System.load("m3g.dll");
	}
	catch (UnsatisfiedLinkError e)
	{
	     System.loadLibrary("m3g");
		
	}
    Engine.cacheFID(Loader.class, 2);

    pngIdentifier = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };

    m3gIdentifierLength = 12;

    BLOCK_SIZE = 4096;
  }
}