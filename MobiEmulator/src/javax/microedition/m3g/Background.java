package javax.microedition.m3g;

public class Background extends Object3D
{
  public static final int BORDER = 32;
  public static final int REPEAT = 33;

  Background(int handle)
  {
    super(handle);
  }



  public Background()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Background.class);
  }

  private static native int create();

  public native int getColor();

  public Image2D getImage()
  {
    return (Image2D)Engine.instantiateJavaPeer(getImageImpl());
  }

  private native int getImageImpl();

  public native int getImageModeX();

  public native int getImageModeY();

  public native int getCropX();

  public native int getCropY();

  public native int getCropWidth();

  public native int getCropHeight();

  public native boolean isColorClearEnabled();

  public native boolean isDepthClearEnabled();

  public native void setColor(int paramInt);

  public void setImage(Image2D Image)
  {
    setImageImpl(Image);
    Engine.addXOT(Image);
  }

  private native void setImageImpl(Image2D paramImage2D);

  public native void setColorClearEnable(boolean paramBoolean);

  public native void setDepthClearEnable(boolean paramBoolean);

  public native void setImageMode(int paramInt1, int paramInt2);

  public native void setCrop(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}