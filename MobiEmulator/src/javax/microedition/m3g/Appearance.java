package javax.microedition.m3g;

public class Appearance extends Object3D
{
  Appearance(int handle)
  {
    super(handle);
  }



  public Appearance()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Appearance.class);
  }

  private static native int create();

  public native int getLayer();

  public Material getMaterial()
  {
    return (Material)Engine.instantiateJavaPeer(getMaterialImpl());
  }

  private native int getMaterialImpl();

  public Fog getFog()
  {
    return (Fog)Engine.instantiateJavaPeer(getFogImpl());
  }

  private native int getFogImpl();

  public CompositingMode getCompositingMode()
  {
    return (CompositingMode)Engine.instantiateJavaPeer(getCompositingModeImpl());
  }

  private native int getCompositingModeImpl();

  public PolygonMode getPolygonMode()
  {
    return (PolygonMode)Engine.instantiateJavaPeer(getPolygonModeImpl());
  }

  private native int getPolygonModeImpl();

  public native void setLayer(int paramInt);

  public void setMaterial(Material Material)
  {
    setMaterialImpl(Material);
    Engine.addXOT(Material);
  }

  private native void setMaterialImpl(Material paramMaterial);

  public void setFog(Fog Fog)
  {
    setFogImpl(Fog);
    Engine.addXOT(Fog);
  }

  private native void setFogImpl(Fog paramFog);

  public void setCompositingMode(CompositingMode CompositingMode)
  {
    setCompositingModeImpl(CompositingMode);
    Engine.addXOT(CompositingMode);
  }

  private native void setCompositingModeImpl(CompositingMode paramCompositingMode);

  public void setPolygonMode(PolygonMode PolygonMode)
  {
    setPolygonModeImpl(PolygonMode);
    Engine.addXOT(PolygonMode);
  }

  private native void setPolygonModeImpl(PolygonMode paramPolygonMode);

  public Texture2D getTexture(int index)
  {
    Texture2D __res = (Texture2D)Engine.instantiateJavaPeer(getTextureImpl(index));
    return __res;
  }

  private native int getTextureImpl(int paramInt);

  public void setTexture(int index, Texture2D texture)
  {
    setTextureImpl(index, texture);
    Engine.addXOT(texture);
  }

  private native void setTextureImpl(int paramInt, Texture2D paramTexture2D);
}