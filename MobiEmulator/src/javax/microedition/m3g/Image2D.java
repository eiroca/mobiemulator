package javax.microedition.m3g;

import javax.microedition.lcdui.Image;

public class Image2D extends Object3D
{
  public static final int ALPHA = 96;
  public static final int LUMINANCE = 97;
  public static final int LUMINANCE_ALPHA = 98;
  public static final int RGB = 99;
  public static final int RGBA = 100;

  Image2D(int handle)
  {
    super(handle);
  }



  public Image2D(int format, int width, int height, byte[] image)
  {
    this(create(format, width, height, image));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);
  }

  private static native int create(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

  public Image2D(int format, int width, int height, byte[] image, byte[] palette)
  {
    this(createPalettized(format, width, height, image, palette));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);
  }

  private static native int createPalettized(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

  public Image2D(int format, int width, int height)
  {
    this(createMutable(format, width, height));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);
  }

  private static native int createMutable(int paramInt1, int paramInt2, int paramInt3);

  private Image2D(int format, int width, int height, boolean immutable)
  {
    this(createUninitialized(format, width, height, immutable));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);
  }

  private static native int createUninitialized(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);

  public Image2D(int format, Object image)
  {
    this(createImage(format, image));

    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);

    Image img = (Image)image;

    int width = getWidth();
    int height = getHeight();
    boolean isOpaque = isOpaque(img);

    if (width <= height)
    {
      int[] rgb = new int[width];

      for (int row = 0; row < height; row++)
      {
        getImageRGB(img, rgb, 0, width, 0, row, width, 1);
        setRGB(rgb, 0, row, width, 1, isOpaque);
      }
    }
    else
    {
      int[] rgb = new int[height];

      for (int col = 0; col < width; col++)
      {
        getImageRGB(img, rgb, 0, 1, col, 0, 1, height);
        setRGB(rgb, col, 0, 1, height, isOpaque);
      }
    }
  }

  Image2D(byte[] data, int offset, int length)
  {
    this(createImageImpl(data, offset, length));

    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Image2D.class);
  }

  public native int getFormat();

  public native int getWidth();

  public native int getHeight();

  public native boolean isMutable();

  public native void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);

  private native void setRGB(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);

  private static int createImage(int format, Object image)
  {
    if (image == null) {
      throw new NullPointerException();
    }
    if (!(image instanceof Image)) {
      throw new IllegalArgumentException();
    }
    Image img = (Image)image;
    int width = getImageWidth(img);
    int height = getImageHeight(img);

    return createUninitialized(format, width, height, true);
  }

  private static native int createImageImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  private static int getImageWidth(Image img)
  {
    return img.getWidth();
  }

  private static int getImageHeight(Image img)
  {
    return img.getHeight();
  }

  static void getImageRGB(Image img, int[] rgbData, int offset, int scanlength, int x, int y, int width, int height)
  {
    img.getRGB(rgbData, offset, scanlength, x, y, width, height);
  }

  static boolean isOpaque(Image img)
  {
    return img.isMutable();
  }
}