package javax.microedition.m3g;

public class TriangleStripArray extends IndexBuffer {

  TriangleStripArray(final int handle) {
    super(handle);
  }

  public TriangleStripArray(final int firstIndex, final int[] stripLengths) {
    this(TriangleStripArray.createImplicit(firstIndex, stripLengths));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != TriangleStripArray.class);
  }

  private static native int createImplicit(int paramInt, int[] paramArrayOfInt);

  public TriangleStripArray(final int[] indices, final int[] stripLengths) {
    this(TriangleStripArray.createExplicit(indices, stripLengths));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != TriangleStripArray.class);
  }

  private static native int createExplicit(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  @Override
  public int getIndexCount() {
    return getIndexCountImpl();
  }

  @Override
  public void getIndices(final int[] indices) {
    getIndicesImpl(indices);
  }
}
