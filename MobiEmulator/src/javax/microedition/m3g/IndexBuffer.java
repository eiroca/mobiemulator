package javax.microedition.m3g;

public abstract class IndexBuffer extends Object3D
{
  IndexBuffer(int handle)
  {
    super(handle);
  }

  IndexBuffer()
  {
  }

  public abstract int getIndexCount();

  native int getIndexCountImpl();

  public abstract void getIndices(int[] paramArrayOfInt);

  native void getIndicesImpl(int[] paramArrayOfInt);
}