package javax.microedition.m3g;

public class AnimationController extends Object3D {

  AnimationController(final int handle) {
    super(handle);
  }

  public AnimationController() {
    this(AnimationController.create());
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != AnimationController.class);
  }

  private static native int create();

  public native float getWeight();

  public native int getActiveIntervalStart();

  public native int getActiveIntervalEnd();

  public native float getSpeed();

  public native int getRefWorldTime();

  public native void setWeight(float paramFloat);

  public native void setActiveInterval(int paramInt1, int paramInt2);

  public native float getPosition(int paramInt);

  public native void setPosition(float paramFloat, int paramInt);

  public native void setSpeed(float paramFloat, int paramInt);
}
