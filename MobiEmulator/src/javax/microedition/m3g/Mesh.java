package javax.microedition.m3g;

public class Mesh extends Node
{
  Mesh(int handle)
  {
    super(handle);
  }



  public Mesh(VertexBuffer vertices, IndexBuffer[] triangles, Appearance[] appearances)
  {
    this(createMultiSubmesh(vertices, Engine.getJavaPeerArrayHandles(triangles), Engine.getJavaPeerArrayHandles(appearances)));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Mesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(triangles);
    Engine.addXOT(appearances);
  }

  private static native int createMultiSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  public Mesh(VertexBuffer vertices, IndexBuffer triangles, Appearance appearance)
  {
    this(createSingleSubmesh(vertices, triangles, appearance));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Mesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(triangles);
    Engine.addXOT(appearance);
  }

  private static native int createSingleSubmesh(VertexBuffer paramVertexBuffer, IndexBuffer paramIndexBuffer, Appearance paramAppearance);

  public native int getSubmeshCount();

  public VertexBuffer getVertexBuffer()
  {
    return (VertexBuffer)Engine.instantiateJavaPeer(getVertexBufferImpl());
  }

  private native int getVertexBufferImpl();

  public IndexBuffer getIndexBuffer(int index)
  {
    IndexBuffer __res = (IndexBuffer)Engine.instantiateJavaPeer(getIndexBufferImpl(index));
    return __res;
  }

  private native int getIndexBufferImpl(int paramInt);

  public Appearance getAppearance(int index)
  {
    Appearance __res = (Appearance)Engine.instantiateJavaPeer(getAppearanceImpl(index));
    return __res;
  }

  private native int getAppearanceImpl(int paramInt);

  public void setAppearance(int index, Appearance Appearance)
  {
    setAppearanceImpl(index, Appearance);
    Engine.addXOT(Appearance);
  }

  private native void setAppearanceImpl(int paramInt, Appearance paramAppearance);
}