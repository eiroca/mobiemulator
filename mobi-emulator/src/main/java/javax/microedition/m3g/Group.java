package javax.microedition.m3g;

public class Group extends Node {

  Group(final int handle) {
    super(handle);
  }

  public Group() {
    this(Group.create());
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Group.class);
  }

  private static native int create();

  public native int getChildCount();

  public Node getChild(final int index) {
    final Node __res = (Node)Engine.instantiateJavaPeer(getChildImpl(index));
    return __res;
  }

  private native int getChildImpl(int paramInt);

  public void addChild(final Node child) {
    addChildImpl(child);
    Engine.addXOT(child);
  }

  private native void addChildImpl(Node paramNode);

  public native void removeChild(Node paramNode);

  public boolean pick(final int scope, final float ox, final float oy, final float oz, final float dx, final float dy, final float dz, final RayIntersection ri) {
    final boolean __res = pickNode(scope, ox, oy, oz, dx, dy, dz, ri);
    return __res;
  }

  private native boolean pickNode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, RayIntersection paramRayIntersection);

  public boolean pick(final int scope, final float x, final float y, final Camera camera, final RayIntersection ri) {
    final boolean __res = pickCamera(scope, x, y, camera, ri);
    return __res;
  }

  private native boolean pickCamera(int paramInt, float paramFloat1, float paramFloat2, Camera paramCamera, RayIntersection paramRayIntersection);
}
