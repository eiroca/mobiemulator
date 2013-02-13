package javax.microedition.m3g;

public class AnimationTrack extends Object3D
{
  public static final int ALPHA = 256;
  public static final int AMBIENT_COLOR = 257;
  public static final int COLOR = 258;
  public static final int CROP = 259;
  public static final int DENSITY = 260;
  public static final int DIFFUSE_COLOR = 261;
  public static final int EMISSIVE_COLOR = 262;
  public static final int FAR_DISTANCE = 263;
  public static final int FIELD_OF_VIEW = 264;
  public static final int INTENSITY = 265;
  public static final int MORPH_WEIGHTS = 266;
  public static final int NEAR_DISTANCE = 267;
  public static final int ORIENTATION = 268;
  public static final int PICKABILITY = 269;
  public static final int SCALE = 270;
  public static final int SHININESS = 271;
  public static final int SPECULAR_COLOR = 272;
  public static final int SPOT_ANGLE = 273;
  public static final int SPOT_EXPONENT = 274;
  public static final int TRANSLATION = 275;
  public static final int VISIBILITY = 276;

  AnimationTrack(int handle)
  {
    super(handle);
  }



  public AnimationTrack(KeyframeSequence sequence, int property)
  {
    this(create(sequence, property));
    Engine.addJavaPeer(this.swerveHandle, this);
    this.ii = (getClass() != AnimationTrack.class);
    Engine.addXOT(sequence);
  }

  private static native int create(KeyframeSequence paramKeyframeSequence, int paramInt);

  public AnimationController getController()
  {
    return (AnimationController)Engine.instantiateJavaPeer(getControllerImpl());
  }

  private native int getControllerImpl();

  public KeyframeSequence getKeyframeSequence()
  {
    return (KeyframeSequence)Engine.instantiateJavaPeer(getKeyframeSequenceImpl());
  }

  private native int getKeyframeSequenceImpl();

  public native int getTargetProperty();

  public void setController(AnimationController Controller)
  {
    setControllerImpl(Controller);
    Engine.addXOT(Controller);
  }

  private native void setControllerImpl(AnimationController paramAnimationController);
}