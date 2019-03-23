package javax.microedition.m3g;

import javax.microedition.lcdui.Image;

public class Image2D extends Object3D {

  public static final int ALPHA = 96;
  public static final int LUMINANCE = 97;
  public static final int LUMINANCE_ALPHA = 98;
  public static final int RGB = 99;
  public static final int RGBA = 100;

  Image2D(final int handle) {
    super(handle);
  }

  public Image2D(final int format, final int width, final int height, final byte[] image) {
    this(Image2D.create(format, width, height, image));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);
  }

  private static native int create(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

  public Image2D(final int format, final int width, final int height, final byte[] image, final byte[] palette) {
    this(Image2D.createPalettized(format, width, height, image, palette));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);
  }

  private static native int createPalettized(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

  public Image2D(final int format, final int width, final int height) {
    this(Image2D.createMutable(format, width, height));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);
  }

  private static native int createMutable(int paramInt1, int paramInt2, int paramInt3);

  private Image2D(final int format, final int width, final int height, final boolean immutable) {
    this(Image2D.createUninitialized(format, width, height, immutable));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);
  }

  private static native int createUninitialized(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);

  public Image2D(final int format, final Object image) {
    this(Image2D.createImage(format, image));

    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);

    final Image img = (Image)image;

    final int width = getWidth();
    final int height = getHeight();
    final boolean isOpaque = Image2D.isOpaque(img);

    if (width <= height) {
      final int[] rgb = new int[width];

      for (int row = 0; row < height; row++) {
        Image2D.getImageRGB(img, rgb, 0, width, 0, row, width, 1);
        setRGB(rgb, 0, row, width, 1, isOpaque);
      }
    }
    else {
      final int[] rgb = new int[height];

      for (int col = 0; col < width; col++) {
        Image2D.getImageRGB(img, rgb, 0, 1, col, 0, 1, height);
        setRGB(rgb, col, 0, 1, height, isOpaque);
      }
    }
  }

  Image2D(final byte[] data, final int offset, final int length) {
    this(Image2D.createImageImpl(data, offset, length));

    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Image2D.class);
  }

  public native int getFormat();

  public native int getWidth();

  public native int getHeight();

  public native boolean isMutable();

  public native void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);

  private native void setRGB(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);

  private static int createImage(final int format, final Object image) {
    if (image == null) { throw new NullPointerException(); }
    if (!(image instanceof Image)) { throw new IllegalArgumentException(); }
    final Image img = (Image)image;
    final int width = Image2D.getImageWidth(img);
    final int height = Image2D.getImageHeight(img);

    return Image2D.createUninitialized(format, width, height, true);
  }

  private static native int createImageImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  private static int getImageWidth(final Image img) {
    return img.getWidth();
  }

  private static int getImageHeight(final Image img) {
    return img.getHeight();
  }

  static void getImageRGB(final Image img, final int[] rgbData, final int offset, final int scanlength, final int x, final int y, final int width, final int height) {
    img.getRGB(rgbData, offset, scanlength, x, y, width, height);
  }

  static boolean isOpaque(final Image img) {
    return img.isMutable();
  }
}
