package javax.microedition.m3g;

public class CompositingMode extends Object3D
{
  public static final int ALPHA = 64;
  public static final int ALPHA_ADD = 65;
  public static final int MODULATE = 66;
  public static final int MODULATE_X2 = 67;
  public static final int REPLACE = 68;

  CompositingMode(int handle)
  {
    super(handle);
  }



  public CompositingMode()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != CompositingMode.class);
  }

  private static native int create();

  public native float getAlphaThreshold();

  public native int getBlending();

  public native boolean isColorWriteEnabled();

  public native boolean isAlphaWriteEnabled();

  public native boolean isDepthWriteEnabled();

  public native boolean isDepthTestEnabled();

  public native float getDepthOffsetFactor();

  public native float getDepthOffsetUnits();

  public native void setAlphaThreshold(float paramFloat);

  public native void setBlending(int paramInt);

  public native void setColorWriteEnable(boolean paramBoolean);

  public native void setAlphaWriteEnable(boolean paramBoolean);

  public native void setDepthWriteEnable(boolean paramBoolean);

  public native void setDepthTestEnable(boolean paramBoolean);

  public native void setDepthOffset(float paramFloat1, float paramFloat2);
}