package javax.microedition.m3g;

public abstract class Object3D {

  int swerveHandle;
  Object uo = null;

  boolean ii = false;

  Object3D() {
  }

  Object3D(final int handle) {
    swerveHandle = handle;
  }

  public native int getUserID();

  public native int getAnimationTrackCount();

  public native void setUserID(int paramInt);

  public AnimationTrack getAnimationTrack(final int index) {
    final AnimationTrack __res = (AnimationTrack)Engine.instantiateJavaPeer(getAnimationTrackImpl(index));
    return __res;
  }

  private native int getAnimationTrackImpl(int paramInt);

  public void addAnimationTrack(final AnimationTrack animationTrack) {
    addAnimationTrackImpl(animationTrack);
    Engine.addXOT(animationTrack);
  }

  private native void addAnimationTrackImpl(AnimationTrack paramAnimationTrack);

  public native void removeAnimationTrack(AnimationTrack paramAnimationTrack);

  public final native int animate(int paramInt);

  public Object3D find(final int userID) {
    final Object3D __res = (Object3D)Engine.instantiateJavaPeer(findImpl(userID));
    return __res;
  }

  private native int findImpl(int paramInt);

  private native int getReferencesImpl(int[] paramArrayOfInt);

  public int getReferences(final Object3D[] references) {
    final int cReferences = getReferencesImpl(null);
    if (references == null) { return cReferences; }

    if (references.length < cReferences) { throw new IllegalArgumentException(); }
    final int[] handles = new int[references.length];
    int len = getReferencesImpl(handles);

    if (len > cReferences) {
      len = cReferences;
    }
    for (int i = 0; i < len; i++) {
      references[i] = ((Object3D)Engine.instantiateJavaPeer(handles[i]));
    }
    return len;
  }

  native void removeUserParameters();

  native int getUserParameterID(int paramInt);

  native int getUserParameterValue(int paramInt, byte[] paramArrayOfByte);

  private native int duplicateImpl();

  public final Object3D duplicate() {
    final Object3D duplicate = (Object3D)Engine.instantiateJavaPeer(duplicateImpl());

    Object3D.duplicateHelper(duplicate, this);

    return duplicate;
  }

  private static void duplicateHelper(final Object3D dst, final Object3D src) {
    dst.setUserObject(src.uo);

    if ((src instanceof Group)) {
      final Group srcGroup = (Group)src;
      final Group dstGroup = (Group)dst;
      final int children = srcGroup.getChildCount();

      for (int child = 0; child < children; child++) {
        try {
          Object3D.duplicateHelper(dstGroup.getChild(child), srcGroup.getChild(child));
        }
        catch (final IndexOutOfBoundsException e) {
          break;
        }
      }
    }
    else if ((src instanceof SkinnedMesh)) {
      Object3D.duplicateHelper(((SkinnedMesh)dst).getSkeleton(), ((SkinnedMesh)src).getSkeleton());
    }
  }

  public Object getUserObject() {
    return uo;
  }

  public void setUserObject(final Object userObject) {
    uo = userObject;

    if (uo != null) {
      Engine.addXOT(this);
    }
  }

  static {
    try {
      System.load("m3g.dll");
    }
    catch (final UnsatisfiedLinkError e) {
      System.loadLibrary("m3g");

    }
    Engine.cacheFID(Object3D.class, 0);
  }
}
