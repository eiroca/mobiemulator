package javax.microedition.m3g;

public class VertexBuffer extends Object3D {

  VertexBuffer(final int handle) {
    super(handle);
  }

  public VertexBuffer() {
    this(VertexBuffer.create());
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != VertexBuffer.class);
  }

  private static native int create();

  public VertexArray getNormals() {
    return (VertexArray)Engine.instantiateJavaPeer(getNormalsImpl());
  }

  private native int getNormalsImpl();

  public VertexArray getColors() {
    return (VertexArray)Engine.instantiateJavaPeer(getColorsImpl());
  }

  private native int getColorsImpl();

  public native int getDefaultColor();

  public native int getVertexCount();

  public void setNormals(final VertexArray Normals) {
    setNormalsImpl(Normals);
    Engine.addXOT(Normals);
  }

  private native void setNormalsImpl(VertexArray paramVertexArray);

  public void setColors(final VertexArray Colors) {
    setColorsImpl(Colors);
    Engine.addXOT(Colors);
  }

  private native void setColorsImpl(VertexArray paramVertexArray);

  public native void setDefaultColor(int paramInt);

  public VertexArray getPositions(final float[] scaleBias) {
    final VertexArray __res = (VertexArray)Engine.instantiateJavaPeer(getPositionsImpl(scaleBias));
    return __res;
  }

  private native int getPositionsImpl(float[] paramArrayOfFloat);

  public void setPositions(final VertexArray positions, final float scale, final float[] bias) {
    setPositionsImpl(positions, scale, bias);
    Engine.addXOT(positions);
  }

  private native void setPositionsImpl(VertexArray paramVertexArray, float paramFloat, float[] paramArrayOfFloat);

  public VertexArray getTexCoords(final int index, final float[] scaleBias) {
    final VertexArray __res = (VertexArray)Engine.instantiateJavaPeer(getTexCoordsImpl(index, scaleBias));
    return __res;
  }

  private native int getTexCoordsImpl(int paramInt, float[] paramArrayOfFloat);

  public void setTexCoords(final int index, final VertexArray texCoords, final float scale, final float[] bias) {
    setTexCoordsImpl(index, texCoords, scale, bias);
    Engine.addXOT(texCoords);
  }

  private native void setTexCoordsImpl(int paramInt, VertexArray paramVertexArray, float paramFloat, float[] paramArrayOfFloat);
}
