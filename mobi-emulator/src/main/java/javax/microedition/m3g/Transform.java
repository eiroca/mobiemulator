package javax.microedition.m3g;

public class Transform {

  int swerveHandle;

  Transform(final int handle) {
    swerveHandle = handle;
  }

  public Transform() {
    swerveHandle = Transform.create();
    Engine.addJavaPeer(swerveHandle, this);
  }

  private static native int create();

  public Transform(final Transform transform) {
    swerveHandle = Transform.createCopy(transform);
    Engine.addJavaPeer(swerveHandle, this);
  }

  private static native int createCopy(Transform paramTransform);

  public native void get(float[] paramArrayOfFloat);

  public void set(final float[] matrix) {
    setMatrix(matrix);
  }

  private native void setMatrix(float[] paramArrayOfFloat);

  public void set(final Transform transform) {
    setTransform(transform);
  }

  private native void setTransform(Transform paramTransform);

  public native void setIdentity();

  public native void invert();

  public native void postMultiply(Transform paramTransform);

  public native void postRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void postRotateQuat(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void postScale(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void postTranslate(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void transform(VertexArray paramVertexArray, float[] paramArrayOfFloat, boolean paramBoolean);

  public void transform(final float[] points) {
    transformPoints(points);
  }

  private native void transformPoints(float[] paramArrayOfFloat);

  public native void transpose();

  static {
    try {
      System.load("m3g.dll");
    }
    catch (final UnsatisfiedLinkError e) {
      System.loadLibrary("m3g");

    }
    Engine.cacheFID(Transform.class, 4);
  }
}
