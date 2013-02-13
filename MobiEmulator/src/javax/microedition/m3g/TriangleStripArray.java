package javax.microedition.m3g;

public class TriangleStripArray extends IndexBuffer
{
  TriangleStripArray(int handle)
  {
    super(handle);
  }



  public TriangleStripArray(int firstIndex, int[] stripLengths)
  {
    this(createImplicit(firstIndex, stripLengths));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != TriangleStripArray.class);
  }

  private static native int createImplicit(int paramInt, int[] paramArrayOfInt);

  public TriangleStripArray(int[] indices, int[] stripLengths)
  {
    this(createExplicit(indices, stripLengths));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != TriangleStripArray.class);
  }

  private static native int createExplicit(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  public int getIndexCount()
  {
    return getIndexCountImpl();
  }

  public void getIndices(int[] indices)
  {
    getIndicesImpl(indices);
  }
}