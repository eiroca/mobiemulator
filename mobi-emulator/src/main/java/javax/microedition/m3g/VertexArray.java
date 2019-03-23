package javax.microedition.m3g;

public class VertexArray extends Object3D {

  VertexArray(final int handle) {
    super(handle);
  }

  public VertexArray(final int numEntries, final int numComponents, final int componentSize) {
    this(VertexArray.create(numEntries, numComponents, componentSize));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != VertexArray.class);
  }

  private static native int create(int paramInt1, int paramInt2, int paramInt3);

  public native int getVertexCount();

  public native int getComponentCount();

  public native int getComponentType();

  public void get(final int firstVertex, final int numVertices, final short[] values) {
    get16(firstVertex, numVertices, values);
  }

  private native void get16(int paramInt1, int paramInt2, short[] paramArrayOfShort);

  public void set(final int startIndex, final int length, final short[] values) {
    set16(startIndex, length, values);
  }

  private native void set16(int paramInt1, int paramInt2, short[] paramArrayOfShort);

  public void get(final int firstVertex, final int numVertices, final byte[] values) {
    get8(firstVertex, numVertices, values);
  }

  private native void get8(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public void set(final int startIndex, final int length, final byte[] values) {
    set8(startIndex, length, values);
  }

  private native void set8(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}
