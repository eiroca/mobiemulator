package javax.microedition.m3g;

public class Material extends Object3D
{
  public static final int AMBIENT = 1024;
  public static final int DIFFUSE = 2048;
  public static final int EMISSIVE = 4096;
  public static final int SPECULAR = 8192;

  Material(int handle)
  {
    super(handle);
  }



  public Material()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Material.class);
  }

  private static native int create();

  public native float getShininess();

  public native boolean isVertexColorTrackingEnabled();

  public native void setShininess(float paramFloat);

  public native void setVertexColorTrackingEnable(boolean paramBoolean);

  public native int getColor(int paramInt);

  public native void setColor(int paramInt1, int paramInt2);
}