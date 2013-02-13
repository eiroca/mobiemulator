package javax.microedition.m3g;

public class PolygonMode extends Object3D
{
  public static final int CULL_BACK = 160;
  public static final int CULL_FRONT = 161;
  public static final int CULL_NONE = 162;
  public static final int SHADE_FLAT = 164;
  public static final int SHADE_SMOOTH = 165;
  public static final int WINDING_CCW = 168;
  public static final int WINDING_CW = 169;

  PolygonMode(int handle)
  {
    super(handle);
  }



  public PolygonMode()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != PolygonMode.class);
  }

  private static native int create();

  public native int getCulling();

  public native int getShading();

  public native int getWinding();

  public native boolean isLocalCameraLightingEnabled();

  public native boolean isPerspectiveCorrectionEnabled();

  public native boolean isTwoSidedLightingEnabled();

  public native void setCulling(int paramInt);

  public native void setShading(int paramInt);

  public native void setWinding(int paramInt);

  public native void setLocalCameraLightingEnable(boolean paramBoolean);

  public native void setPerspectiveCorrectionEnable(boolean paramBoolean);

  public native void setTwoSidedLightingEnable(boolean paramBoolean);
}