package javax.microedition.m3g;

public class MorphingMesh extends Mesh
{
  MorphingMesh(int handle)
  {
    super(handle);
  }



  public MorphingMesh(VertexBuffer vertices, VertexBuffer[] targets, IndexBuffer[] triangles, Appearance[] appearances)
  {
    this(createMultiSubmesh(vertices, Engine.getJavaPeerArrayHandles(targets), Engine.getJavaPeerArrayHandles(triangles), Engine.getJavaPeerArrayHandles(appearances)));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != MorphingMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(targets);
    Engine.addXOT(triangles);
    Engine.addXOT(appearances);
  }

  private static native int createMultiSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);

  public MorphingMesh(VertexBuffer vertices, VertexBuffer[] targets, IndexBuffer triangles, Appearance appearance)
  {
    this(createSingleSubmesh(vertices, Engine.getJavaPeerArrayHandles(targets), triangles, appearance));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != MorphingMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(targets);
    Engine.addXOT(triangles);
    Engine.addXOT(appearance);
  }

  private static native int createSingleSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt, IndexBuffer paramIndexBuffer, Appearance paramAppearance);

  public native int getMorphTargetCount();

  public VertexBuffer getMorphTarget(int index)
  {
    VertexBuffer __res = (VertexBuffer)Engine.instantiateJavaPeer(getMorphTargetImpl(index));
    return __res;
  }

  private native int getMorphTargetImpl(int paramInt);

  public native void getWeights(float[] paramArrayOfFloat);

  public native void setWeights(float[] paramArrayOfFloat);
}