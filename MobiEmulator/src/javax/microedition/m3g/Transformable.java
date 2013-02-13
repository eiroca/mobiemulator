package javax.microedition.m3g;

public abstract class Transformable extends Object3D
{
  Transformable(int handle)
  {
    super(handle);
  }

  Transformable()
  {
  }

  public native void getTranslation(float[] paramArrayOfFloat);

  public native void getScale(float[] paramArrayOfFloat);

  public native void getOrientation(float[] paramArrayOfFloat);

  public native void setTranslation(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void setScale(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void setOrientation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void translate(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void scale(float paramFloat1, float paramFloat2, float paramFloat3);

  public native void preRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void postRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

  public native void getTransform(Transform paramTransform);

  public native void getCompositeTransform(Transform paramTransform);

  public native void setTransform(Transform paramTransform);
}