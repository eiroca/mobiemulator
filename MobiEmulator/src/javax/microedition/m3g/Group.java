package javax.microedition.m3g;

public class Group extends Node
{
  Group(int handle)
  {
    super(handle);
  }



  public Group()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Group.class);
  }

  private static native int create();

  public native int getChildCount();

  public Node getChild(int index)
  {
    Node __res = (Node)Engine.instantiateJavaPeer(getChildImpl(index));
    return __res;
  }

  private native int getChildImpl(int paramInt);

  public void addChild(Node child)
  {
    addChildImpl(child);
    Engine.addXOT(child);
  }

  private native void addChildImpl(Node paramNode);

  public native void removeChild(Node paramNode);

  public boolean pick(int scope, float ox, float oy, float oz, float dx, float dy, float dz, RayIntersection ri)
  {
    boolean __res = pickNode(scope, ox, oy, oz, dx, dy, dz, ri);
    return __res;
  }

  private native boolean pickNode(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, RayIntersection paramRayIntersection);

  public boolean pick(int scope, float x, float y, Camera camera, RayIntersection ri)
  {
    boolean __res = pickCamera(scope, x, y, camera, ri);
    return __res;
  }

  private native boolean pickCamera(int paramInt, float paramFloat1, float paramFloat2, Camera paramCamera, RayIntersection paramRayIntersection);
}