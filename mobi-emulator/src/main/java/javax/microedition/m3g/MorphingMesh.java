package javax.microedition.m3g;

public class MorphingMesh extends Mesh {

  MorphingMesh(final int handle) {
    super(handle);
  }

  public MorphingMesh(final VertexBuffer vertices, final VertexBuffer[] targets, final IndexBuffer[] triangles, final Appearance[] appearances) {
    this(MorphingMesh.createMultiSubmesh(vertices, Engine.getJavaPeerArrayHandles(targets), Engine.getJavaPeerArrayHandles(triangles), Engine.getJavaPeerArrayHandles(appearances)));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != MorphingMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(targets);
    Engine.addXOT(triangles);
    Engine.addXOT(appearances);
  }

  private static native int createMultiSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);

  public MorphingMesh(final VertexBuffer vertices, final VertexBuffer[] targets, final IndexBuffer triangles, final Appearance appearance) {
    this(MorphingMesh.createSingleSubmesh(vertices, Engine.getJavaPeerArrayHandles(targets), triangles, appearance));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != MorphingMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(targets);
    Engine.addXOT(triangles);
    Engine.addXOT(appearance);
  }

  private static native int createSingleSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt, IndexBuffer paramIndexBuffer, Appearance paramAppearance);

  public native int getMorphTargetCount();

  public VertexBuffer getMorphTarget(final int index) {
    final VertexBuffer __res = (VertexBuffer)Engine.instantiateJavaPeer(getMorphTargetImpl(index));
    return __res;
  }

  private native int getMorphTargetImpl(int paramInt);

  public native void getWeights(float[] paramArrayOfFloat);

  public native void setWeights(float[] paramArrayOfFloat);
}
