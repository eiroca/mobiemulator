package com.nec.mascotcapsule.v3;

public class Effect3D {

  public static final int NORMAL_SHADING = 0;
  public static final int TOON_SHADING = 1;

  public Effect3D() {
  }

  public Effect3D(final Light light, final int shading, final boolean isEnableTrans, final Texture tex) {
  }

  public final Light getLight() {
    return null;
  }

  public final void setLight(final Light light1) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getShading() {
    return 0;
  }

  public final int getShadingType() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setShading(final int i) {
  }

  public final void setShadingType(final int i) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThreshold() {
    return 0;
  }

  public final int getToonThreshold() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThresholdHigh() {
    return 0;
  }

  public final int getToonHigh() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final int getThresholdLow() {
    return 0;
  }

  public final int getToonLow() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setThreshold(final int i, final int j, final int k) {
  }

  public final void setToonParams(final int i, final int j, final int k) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final boolean isSemiTransparentEnabled() {
    return false;
  }

  public final boolean isTransparency() {
    return false;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setSemiTransparentEnabled(final boolean flag) {
  }

  public final void setTransparency(final boolean flag) {
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final Texture getSphereMap() {
    return null;
  }

  public final Texture getSphereTexture() {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public final void setSphereMap(final Texture texture) {
  }

  public final void setSphereTexture(final Texture texture) {
  }

}
