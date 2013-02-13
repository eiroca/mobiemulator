package javax.microedition.m3g;

public class VertexArray extends Object3D
{
  VertexArray(int handle)
  {
    super(handle);
  }



  public VertexArray(int numEntries, int numComponents, int componentSize)
  {
    this(create(numEntries, numComponents, componentSize));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != VertexArray.class);
  }

  private static native int create(int paramInt1, int paramInt2, int paramInt3);

  public native int getVertexCount();

  public native int getComponentCount();

  public native int getComponentType();

  public void get(int firstVertex, int numVertices, short[] values)
  {
    get16(firstVertex, numVertices, values);
  }

  private native void get16(int paramInt1, int paramInt2, short[] paramArrayOfShort);

  public void set(int startIndex, int length, short[] values)
  {
    set16(startIndex, length, values);
  }

  private native void set16(int paramInt1, int paramInt2, short[] paramArrayOfShort);

  public void get(int firstVertex, int numVertices, byte[] values)
  {
    get8(firstVertex, numVertices, values);
  }

  private native void get8(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public void set(int startIndex, int length, byte[] values)
  {
    set8(startIndex, length, values);
  }

  private native void set8(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}