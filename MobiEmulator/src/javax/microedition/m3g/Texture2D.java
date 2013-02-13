package javax.microedition.m3g;

public class Texture2D extends Transformable
{
  public static final int FILTER_BASE_LEVEL = 208;
  public static final int FILTER_LINEAR = 209;
  public static final int FILTER_NEAREST = 210;
  public static final int FUNC_ADD = 224;
  public static final int FUNC_BLEND = 225;
  public static final int FUNC_DECAL = 226;
  public static final int FUNC_MODULATE = 227;
  public static final int FUNC_REPLACE = 228;
  public static final int WRAP_CLAMP = 240;
  public static final int WRAP_REPEAT = 241;

  Texture2D(int handle)
  {
    super(handle);
  }



  public Texture2D(Image2D image)
  {
    this(create(image));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Texture2D.class);
    Engine.addXOT(image);
  }

  private static native int create(Image2D paramImage2D);

  public Image2D getImage()
  {
    return (Image2D)Engine.instantiateJavaPeer(getImageImpl());
  }

  private native int getImageImpl();

  public native int getBlending();

  public native int getBlendColor();

  public native int getImageFilter();

  public native int getLevelFilter();

  public void setImage(Image2D Image)
  {
    setImageImpl(Image);
    Engine.addXOT(Image);
  }

  private native void setImageImpl(Image2D paramImage2D);

  public native void setBlending(int paramInt);

  public native void setBlendColor(int paramInt);

  public native int getWrappingS();

  public native int getWrappingT();

  public native void setWrapping(int paramInt1, int paramInt2);

  public native void setFiltering(int paramInt1, int paramInt2);
}