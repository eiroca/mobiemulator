package javax.microedition.m3g;

public class SkinnedMesh extends Mesh {

  SkinnedMesh(final int handle) {
    super(handle);
  }

  public SkinnedMesh(final VertexBuffer vertices, final IndexBuffer[] submeshes, final Appearance[] appearances, final Group skeleton) {
    this(SkinnedMesh.createMultiSubmesh(vertices, Engine.getJavaPeerArrayHandles(submeshes), Engine.getJavaPeerArrayHandles(appearances), skeleton));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != SkinnedMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(submeshes);
    Engine.addXOT(appearances);
    Engine.addXOT(skeleton);
  }

  private static native int createMultiSubmesh(VertexBuffer paramVertexBuffer, int[] paramArrayOfInt1, int[] paramArrayOfInt2, Group paramGroup);

  public SkinnedMesh(final VertexBuffer vertices, final IndexBuffer submesh, final Appearance appearance, final Group skeleton) {
    this(SkinnedMesh.createSingleSubmesh(vertices, submesh, appearance, skeleton));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != SkinnedMesh.class);
    Engine.addXOT(vertices);
    Engine.addXOT(submesh);
    Engine.addXOT(appearance);
    Engine.addXOT(skeleton);
  }

  private static native int createSingleSubmesh(VertexBuffer paramVertexBuffer, IndexBuffer paramIndexBuffer, Appearance paramAppearance, Group paramGroup);

  public Group getSkeleton() {
    return (Group)Engine.instantiateJavaPeer(getSkeletonImpl());
  }

  private native int getSkeletonImpl();

  public void addTransform(final Node bone, final int weight, final int firstVertex, final int numVertices) {
    addTransformImpl(bone, weight, firstVertex, numVertices);
    Engine.addXOT(bone);
  }

  private native void addTransformImpl(Node paramNode, int paramInt1, int paramInt2, int paramInt3);

  public native int getBoneVertices(Node paramNode, int[] paramArrayOfInt, float[] paramArrayOfFloat);

  public native void getBoneTransform(Node paramNode, Transform paramTransform);
}
