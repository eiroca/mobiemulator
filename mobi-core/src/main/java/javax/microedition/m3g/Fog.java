package javax.microedition.m3g;

public class Fog extends Object3D {

  public static final int EXPONENTIAL = 80;
  public static final int LINEAR = 81;

  Fog(final int handle) {
    super(handle);
  }

  public Fog() {
    this(Fog.create());
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Fog.class);
  }

  private static native int create();

  public native int getColor();

  public native float getDensity();

  public native int getMode();

  public native float getNearDistance();

  public native float getFarDistance();

  public native void setColor(int paramInt);

  public native void setDensity(float paramFloat);

  public native void setMode(int paramInt);

  public native void setLinear(float paramFloat1, float paramFloat2);
}
