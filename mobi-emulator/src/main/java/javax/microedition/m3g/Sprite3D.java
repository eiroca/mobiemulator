package javax.microedition.m3g;

public class Sprite3D extends Node {

  Sprite3D(final int handle) {
    super(handle);
  }

  public Sprite3D(final boolean scaled, final Image2D image, final Appearance appearance) {
    this(Sprite3D.create(scaled, image, appearance));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Sprite3D.class);
    Engine.addXOT(image);
    Engine.addXOT(appearance);
  }

  private static native int create(boolean paramBoolean, Image2D paramImage2D, Appearance paramAppearance);

  public Appearance getAppearance() {
    return (Appearance)Engine.instantiateJavaPeer(getAppearanceImpl());
  }

  private native int getAppearanceImpl();

  public Image2D getImage() {
    return (Image2D)Engine.instantiateJavaPeer(getImageImpl());
  }

  private native int getImageImpl();

  public native int getCropHeight();

  public native int getCropWidth();

  public native int getCropX();

  public native int getCropY();

  public void setAppearance(final Appearance Appearance) {
    setAppearanceImpl(Appearance);
    Engine.addXOT(Appearance);
  }

  private native void setAppearanceImpl(Appearance paramAppearance);

  public void setImage(final Image2D Image) {
    setImageImpl(Image);
    Engine.addXOT(Image);
  }

  private native void setImageImpl(Image2D paramImage2D);

  public native boolean isScaled();

  public native void setCrop(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}
