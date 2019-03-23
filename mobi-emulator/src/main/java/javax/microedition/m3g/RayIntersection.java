package javax.microedition.m3g;

public class RayIntersection {

  int swerveHandle;

  RayIntersection(final int handle) {
    swerveHandle = handle;
  }

  public RayIntersection() {
    swerveHandle = RayIntersection.create();
    Engine.addJavaPeer(swerveHandle, this);
  }

  private static native int create();

  public native float getDistance();

  public Node getIntersected() {
    return (Node)Engine.instantiateJavaPeer(getIntersectedImpl());
  }

  private native int getIntersectedImpl();

  public native int getSubmeshIndex();

  public native float getNormalX();

  public native float getNormalY();

  public native float getNormalZ();

  public native float getTextureS(int paramInt);

  public native float getTextureT(int paramInt);

  public native void getRay(float[] paramArrayOfFloat);

  static {
    try {
      System.load("m3g.dll");
    }
    catch (final UnsatisfiedLinkError e) {
      System.loadLibrary("m3g");

    }
    Engine.cacheFID(RayIntersection.class, 3);
  }
}
