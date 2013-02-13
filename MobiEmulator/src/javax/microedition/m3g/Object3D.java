package javax.microedition.m3g;

public abstract class Object3D
{
  int swerveHandle;
  Object uo = null;

  boolean ii = false;

  Object3D()
  {
  }

  Object3D(int handle)
  {
    this.swerveHandle = handle;
  }

  public native int getUserID();

  public native int getAnimationTrackCount();

  public native void setUserID(int paramInt);

  public AnimationTrack getAnimationTrack(int index)
  {
    AnimationTrack __res = (AnimationTrack)Engine.instantiateJavaPeer(getAnimationTrackImpl(index));
    return __res;
  }

  private native int getAnimationTrackImpl(int paramInt);

  public void addAnimationTrack(AnimationTrack animationTrack)
  {
    addAnimationTrackImpl(animationTrack);
    Engine.addXOT(animationTrack);
  }

  private native void addAnimationTrackImpl(AnimationTrack paramAnimationTrack);

  public native void removeAnimationTrack(AnimationTrack paramAnimationTrack);

  public final native int animate(int paramInt);

  public Object3D find(int userID)
  {
    Object3D __res = (Object3D)Engine.instantiateJavaPeer(findImpl(userID));
    return __res;
  }

  private native int findImpl(int paramInt);

  private native int getReferencesImpl(int[] paramArrayOfInt);

  public int getReferences(Object3D[] references)
  {
    int cReferences = getReferencesImpl(null);
    if (references == null)
    {
      return cReferences;
    }

    if (references.length < cReferences) {
      throw new IllegalArgumentException();
    }
    int[] handles = new int[references.length];
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

  public final Object3D duplicate()
  {
    Object3D duplicate = (Object3D)Engine.instantiateJavaPeer(duplicateImpl());

    duplicateHelper(duplicate, this);

    return duplicate;
  }

  private static void duplicateHelper(Object3D dst, Object3D src)
  {
    dst.setUserObject(src.uo);

    if ((src instanceof Group))
    {
      Group srcGroup = (Group)src;
      Group dstGroup = (Group)dst;
      int children = srcGroup.getChildCount();

      for (int child = 0; child < children; child++)
      {
        try
        {
          duplicateHelper(dstGroup.getChild(child), srcGroup.getChild(child));
        }
        catch (IndexOutOfBoundsException e)
        {
          break;
        }
      }
    }
    else if ((src instanceof SkinnedMesh))
    {
      duplicateHelper(((SkinnedMesh)dst).getSkeleton(), ((SkinnedMesh)src).getSkeleton());
    }
  }

  public Object getUserObject()
  {
    return this.uo;
  }

  public void setUserObject(Object userObject)
  {
    this.uo = userObject;

    if (this.uo != null)
      Engine.addXOT(this);
  }

  static
  {
      try
	{
		System.load("m3g.dll");
	}
	catch (UnsatisfiedLinkError e)
	{
	     System.loadLibrary("m3g");
		
	}
    Engine.cacheFID(Object3D.class, 0);
  }
}