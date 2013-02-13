package javax.microedition.m3g;

public class VertexBuffer extends Object3D
{
  VertexBuffer(int handle)
  {
    super(handle);
  }



  public VertexBuffer()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != VertexBuffer.class);
  }

  private static native int create();

  public VertexArray getNormals()
  {
    return (VertexArray)Engine.instantiateJavaPeer(getNormalsImpl());
  }

  private native int getNormalsImpl();

  public VertexArray getColors()
  {
    return (VertexArray)Engine.instantiateJavaPeer(getColorsImpl());
  }

  private native int getColorsImpl();

  public native int getDefaultColor();

  public native int getVertexCount();

  public void setNormals(VertexArray Normals)
  {
    setNormalsImpl(Normals);
    Engine.addXOT(Normals);
  }

  private native void setNormalsImpl(VertexArray paramVertexArray);

  public void setColors(VertexArray Colors)
  {
    setColorsImpl(Colors);
    Engine.addXOT(Colors);
  }

  private native void setColorsImpl(VertexArray paramVertexArray);

  public native void setDefaultColor(int paramInt);

  public VertexArray getPositions(float[] scaleBias)
  {
    VertexArray __res = (VertexArray)Engine.instantiateJavaPeer(getPositionsImpl(scaleBias));
    return __res;
  }

  private native int getPositionsImpl(float[] paramArrayOfFloat);

  public void setPositions(VertexArray positions, float scale, float[] bias)
  {
    setPositionsImpl(positions, scale, bias);
    Engine.addXOT(positions);
  }

  private native void setPositionsImpl(VertexArray paramVertexArray, float paramFloat, float[] paramArrayOfFloat);

  public VertexArray getTexCoords(int index, float[] scaleBias)
  {
    VertexArray __res = (VertexArray)Engine.instantiateJavaPeer(getTexCoordsImpl(index, scaleBias));
    return __res;
  }

  private native int getTexCoordsImpl(int paramInt, float[] paramArrayOfFloat);

  public void setTexCoords(int index, VertexArray texCoords, float scale, float[] bias)
  {
    setTexCoordsImpl(index, texCoords, scale, bias);
    Engine.addXOT(texCoords);
  }

  private native void setTexCoordsImpl(int paramInt, VertexArray paramVertexArray, float paramFloat, float[] paramArrayOfFloat);
}