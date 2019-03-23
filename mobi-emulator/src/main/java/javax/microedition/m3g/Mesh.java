package javax.microedition.m3g;

public class Mesh extends Node {

  Mesh(final int handle) {
    super(handle);
  }

  public Mesh(final VertexBuffer vertices, final IndexBuffer[] triangles, final Appearance[] appearances) {
    this(Mesh.createMultiSubmesh(vertices, Engine.getJavaPeerArrayHandles(triangles), Engine.getJavaPeerArrayHandles(appearances)));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Mesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(triangles);
    Engine.addXOT(appearances);
  }

  private static native int createMultiSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  public Mesh(final VertexBuffer vertices, final IndexBuffer triangles, final Appearance appearance) {
    this(Mesh.createSingleSubmesh(vertices, triangles, appearance));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != Mesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(triangles);
    Engine.addXOT(appearance);
  }

  private static native int createSingleSubmesh(VertexBuffer paramVertexBuffer, IndexBuffer paramIndexBuffer, Appearance paramAppearance);

  public native int getSubmeshCount();

  public VertexBuffer getVertexBuffer() {
    return (VertexBuffer)Engine.instantiateJavaPeer(getVertexBufferImpl());
  }

  private native int getVertexBufferImpl();

  public IndexBuffer getIndexBuffer(final int index) {
    final IndexBuffer __res = (IndexBuffer)Engine.instantiateJavaPeer(getIndexBufferImpl(index));
    return __res;
  }

  private native int getIndexBufferImpl(int paramInt);

  public Appearance getAppearance(final int index) {
    final Appearance __res = (Appearance)Engine.instantiateJavaPeer(getAppearanceImpl(index));
    return __res;
  }

  private native int getAppearanceImpl(int paramInt);

  public void setAppearance(final int index, final Appearance Appearance) {
    setAppearanceImpl(index, Appearance);
    Engine.addXOT(Appearance);
  }

  private native void setAppearanceImpl(int paramInt, Appearance paramAppearance);
}
