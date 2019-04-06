package javax.microedition.m3g;

public class KeyframeSequence extends Object3D {

  public static final int LINEAR = 176;
  public static final int SLERP = 177;
  public static final int SPLINE = 178;
  public static final int SQUAD = 179;
  public static final int STEP = 180;
  public static final int CONSTANT = 192;
  public static final int LOOP = 193;

  KeyframeSequence(final int handle) {
    super(handle);
  }

  public KeyframeSequence(final int numKeyframes, final int numComponents, final int interpolation) {
    this(KeyframeSequence.create(numKeyframes, numComponents, interpolation));
    Engine.addJavaPeer(swerveHandle, this);
    ii = (getClass() != KeyframeSequence.class);
  }

  private static native int create(int paramInt1, int paramInt2, int paramInt3);

  public native int getDuration();

  public native int getRepeatMode();

  public native int getKeyframeCount();

  public native int getComponentCount();

  public native int getInterpolationType();

  public native int getValidRangeFirst();

  public native int getValidRangeLast();

  public native void setDuration(int paramInt);

  public native void setRepeatMode(int paramInt);

  public native int getKeyframe(int paramInt, float[] paramArrayOfFloat);

  public native void setKeyframe(int paramInt1, int paramInt2, float[] paramArrayOfFloat);

  public native void setValidRange(int paramInt1, int paramInt2);
}
