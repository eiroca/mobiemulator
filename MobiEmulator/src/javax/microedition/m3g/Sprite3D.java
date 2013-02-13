package javax.microedition.m3g;

public class Sprite3D extends Node
{
  Sprite3D(int handle)
  {
    super(handle);
  }



  public Sprite3D(boolean scaled, Image2D image, Appearance appearance)
  {
    this(create(scaled, image, appearance));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Sprite3D.class);
    Engine.addXOT(image);
    Engine.addXOT(appearance);
  }

  private static native int create(boolean paramBoolean, Image2D paramImage2D, Appearance paramAppearance);

  public Appearance getAppearance()
  {
    return (Appearance)Engine.instantiateJavaPeer(getAppearanceImpl());
  }

  private native int getAppearanceImpl();

  public Image2D getImage()
  {
    return (Image2D)Engine.instantiateJavaPeer(getImageImpl());
  }

  private native int getImageImpl();

  public native int getCropHeight();

  public native int getCropWidth();

  public native int getCropX();

  public native int getCropY();

  public void setAppearance(Appearance Appearance)
  {
    setAppearanceImpl(Appearance);
    Engine.addXOT(Appearance);
  }

  private native void setAppearanceImpl(Appearance paramAppearance);

  public void setImage(Image2D Image)
  {
    setImageImpl(Image);
    Engine.addXOT(Image);
  }

  private native void setImageImpl(Image2D paramImage2D);

  public native boolean isScaled();

  public native void setCrop(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}