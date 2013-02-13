package javax.microedition.m3g;

public class Light extends Node
{
  public static final int AMBIENT = 128;
  public static final int DIRECTIONAL = 129;
  public static final int OMNI = 130;
  public static final int SPOT = 131;

  Light(int handle)
  {
    super(handle);
  }



  public Light()
  {
    this(create());
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != Light.class);
  }

  private static native int create();

  public native int getMode();

  public native int getColor();

  public native float getIntensity();

  public native float getSpotAngle();

  public native float getSpotExponent();

  public native float getConstantAttenuation();

  public native float getLinearAttenuation();

  public native float getQuadraticAttenuation();

  public native void setMode(int paramInt);

  public native void setColor(int paramInt);

  public native void setIntensity(float paramFloat);

  public native void setSpotAngle(float paramFloat);

  public native void setSpotExponent(float paramFloat);

  public native void setAttenuation(float paramFloat1, float paramFloat2, float paramFloat3);
}