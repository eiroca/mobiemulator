package javax.microedition.m3g;

public class Camera extends Node {

  public static final int GENERIC = 48;
  public static final int PARALLEL = 49;
  public static final int PERSPECTIVE = 50;

  Camera(final int handle) {
    super(handle);
  }

  public Camera() {
    this(Camera.create());
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Camera.class);
  }

  private static native int create();

  public int getProjection(final float[] params) {
    final int __res = getProjectionParams(params);
    return __res;
  }

  private native int getProjectionParams(float[] paramArrayOfFloat);

  public int getProjection(final Transform transform) {
    final int __res = getProjectionTransform(transform);
    return __res;
  }

  private native int getProjectionTransform(Transform paramTransform);

  public native void setGeneric(Transform paramTransform);

  public native void setParallel(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void setPerspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
}
